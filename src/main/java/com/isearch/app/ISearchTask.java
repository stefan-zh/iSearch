package com.isearch.app;

import com.sun.istack.internal.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISearchTask implements Callable<Boolean> {

    private final String url;
    private final String searchTerm;

    /**
     * Constructor.
     *
     * @param url - the URL for the page where we are looking for the search term
     * @param searchTerm - a regular expression that we are looking for on the page
     */
    ISearchTask(@NotNull String url, @NotNull String searchTerm) {
        this.url = url;
        this.searchTerm = searchTerm;
    }

    /**
     * Perform case-insensitive pattern matching.
     *
     * @return true if the page contains the search term; false otherwise
     */
    @Override
    public Boolean call() throws IOException {
        Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Document page = Jsoup.connect(url).get();
        String contents = page.text();
        Matcher matcher = pattern.matcher(contents);
        return matcher.find();
    }
}
