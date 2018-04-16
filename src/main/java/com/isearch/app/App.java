package com.isearch.app;

import java.util.Arrays;
import java.util.stream.Collectors;

public class App {

    // thread-safe logger using System.out
    static Logger LOGGER = new Logger(System.out);

    /**
     * Main method
     *
     * @param args - the arguments need to be a source file with a list of links followed by search terms
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (args == null || args.length < 2) {
            LOGGER.println("You need to provide arguments: source file with a list of links, followed by search terms");
            System.exit(1);
        }

        String fileName = args[0];
        String searchTerm = Arrays.stream(args, 1, args.length)
            .collect(Collectors.joining(" "));

        // use iSearch
        ISearch iSearch = new ISearch();
        iSearch.loadFile(fileName);
        iSearch.search(searchTerm);

        // exit
        LOGGER.println(System.currentTimeMillis() - start);
        System.exit(0);
    }

}
