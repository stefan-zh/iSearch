package com.isearch.app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

class ISearch {

    private static final String URL_COLUMN = "URL";

    private List<String> urls;

    ISearch() {
        urls = Collections.emptyList();
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

    /**
     * Searches for the search term on the list of URL's that are loaded in the class.
     *
     * @param searchTerm - a regular expression
     */
    void search(String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            throw new IllegalArgumentException("The search string cannot be null or empty.");
        }

        // create the working queue with tasks
        BlockingQueue<FutureTask<String>> workQueue = urls.stream()
            .map(url -> new ISearchTask(url, searchTerm))
            // wrap in a FutureTask, so that results can be collected on it
            .map(FutureTask::new)
            // place in a thread-safe queue
            .collect(Collectors.toCollection(LinkedBlockingQueue::new));

        // create concurrent http requester
        ConcurrentHttpRequester<String> httpRequester = new ConcurrentHttpRequester<>(workQueue);

        // the tasks will be executed asynchronously
        List<String> listUrls = httpRequester.execute();

        // combine all URLs that passed into a string that will be written to a file
        String resultUrls = listUrls.stream()
            .filter(Objects::nonNull)
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
