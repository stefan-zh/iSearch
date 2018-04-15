package com.isearch.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ISearchTaskTest {

    @Test
    public void testSearchString_Simple() {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "Website Searcher";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertEquals(url, task.call());
    }

    @Test
    public void testSearchString_RegEx() {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "(\\w+)\\.txt";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertEquals(url, task.call());
    }

    @Test
    public void testSearchString_IgnoreCase() {
        String url = "https://s3.amazonaws.com/fieldlens-public/Website+Searcher.html";
        String searchTerm = "jdk"; // appears only once capitalized
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertEquals(url, task.call());
    }

    @Test
    public void testSearchString_NotFound() {
        String url = "http://www.google.com";
        String searchTerm = "sakjhask";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertNull(task.call());
    }

    @Test
    public void testSearchString_ProtocolNotSpecified() {
        String url = "google.com";
        String searchTerm = "google";
        ISearchTask task = new ISearchTask(url, searchTerm);
        assertEquals(url, task.call());
    }

}
