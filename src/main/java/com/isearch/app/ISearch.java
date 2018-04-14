package com.isearch.app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
                .map(url -> url.matches("^htt(p|ps)://.*") ? url : "http://" + url)
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
//        urls.stream()
//            .map(url -> new ISearchTask(url, searchTerm))
//            .map(task -> httpRequester.submit(task))
//            .filter(Future::get)
//            .collect()
    }

    /**
     * @return the urls loaded into this iSearch instance
     */
    List<String> getUrls() {
        return this.urls;
    }

}
