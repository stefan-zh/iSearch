package com.isearch.app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class ISearch {

    private static final String URL_COLUMN = "URL";

    private List<String> urls;
    private BoundedHTTPRequester httpRequester;

    ISearch() {
        urls = Collections.emptyList();
        httpRequester = new BoundedHTTPRequester();
    }

    /**
     * Loads the URLs from the given file into an internal list.
     *
     * @param filePath - the path to the file with URL links
     */
    void loadFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("File '%s' does not exist", filePath));
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            CSVParser csvParser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(br);
            urls = csvParser.getRecords().stream()
                .map(r -> r.get(URL_COLUMN))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // don't need to close try-with-resource
    }

    void search(String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            throw new IllegalArgumentException("The search string cannot be null or empty.");
        }
        List<String> listUrls = new LinkedList<>();
        ExecutorService executorService = new ThreadPoolExecutor(4, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        List<Callable<String>> tasks = urls.stream()
            .map(url -> new ISearchTask(url, searchTerm))
            .collect(Collectors.toList());

        try {
            // launch task execution asynchronously
            List<Future<String>> futures = executorService.invokeAll(tasks);
            // collect future results
            for (Future<String> future : futures) {
                String url = future.get();
                if (url != null) {
                    listUrls.add(url);
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            // do nothing
        }

        // close the executor service
        // https://stackoverflow.com/a/1250655
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        // combine all URLs that passed into a string that will be written to a file
        String resultUrls = listUrls.stream()
            .collect(Collectors.joining(System.lineSeparator()));

        // write the results to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            writer.write(resultUrls);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the urls loaded into this iSearch instance
     */
    List<String> getUrls() {
        return this.urls;
    }

}
