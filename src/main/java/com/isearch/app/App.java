package com.isearch.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    // thread-safe logger using System.out
    static Logger LOGGER = new Logger(System.out);

    /**
     * Main method
     *
     * @param args - the arguments need to be a source file with a list of links followed by search terms
     */
    public static void main(String[] args) throws IOException {
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
        List<String> urls = iSearch.search(searchTerm);

        // combine all URLs into a string that will be written to a file
        String resultUrls = String.join(System.lineSeparator(), urls);

        // write the results to a file
        BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"));
        writer.write(resultUrls);
        writer.newLine();
        writer.close();

        // exit
        LOGGER.println(System.currentTimeMillis() - start);
        System.exit(0);
    }

}
