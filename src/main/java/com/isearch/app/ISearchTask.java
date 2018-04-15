package com.isearch.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISearchTask implements Callable<String> {

    private final String url;
    private final String searchTerm;

    /**
     * Constructor.
     *
     * @param url - the URL for the page where we are looking for the search term
     * @param searchTerm - a regular expression that we are looking for on the page
     */
    ISearchTask(String url, String searchTerm) {
        this.url = url;
        this.searchTerm = searchTerm;
    }

    /**
     * Perform case-insensitive pattern matching.
     *
     * @return the URL if the pableattern is matched, null otherwise
     */
    @Override
    public String call() {
        long start = System.currentTimeMillis();
        // urls need a prefix if they don't have one
        String address = url.matches("^htt(p|ps)://.*") ? url : "http://" + url;
        Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

        try {
            // issue a GET request to the page
            Document page = Jsoup.connect(address).get();
            String contents = page.text();
            Matcher matcher = pattern.matcher(contents);
            String result = matcher.find() ? url : null;
            App.LOGGER.println(String.format("%s: %dms", url, System.currentTimeMillis() - start));
            return result;
        } catch (IOException ex) {
            App.LOGGER.println(String.format("%s: did not complete due to %s", url, ex.toString()));
            return null;
        }
    }
}
