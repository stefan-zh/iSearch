package com.isearch.app;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ISearchTaskTest {

    @Test
    public void testSearchString_Simple() throws IOException {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "Website Searcher";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertTrue(task.call());
    }

    @Test
    public void testSearchString_RegEx() throws IOException {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "(\\w+)\\.txt";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertTrue(task.call());
    }

    @Test
    public void testSearchString_IgnoreCase() throws IOException {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "jdk"; // appears only once capitalized
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertTrue(task.call());
    }

    @Test
    public void testSearchString_NotFound() throws IOException {
        String url = "http://www.google.com";
        String searchTerm = "sakjhask";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertFalse(task.call());
    }

}
