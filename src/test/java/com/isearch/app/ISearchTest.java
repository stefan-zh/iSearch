package com.isearch.app;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ISearchTest {

    private ISearch iSearch;
    private static ClassLoader classLoader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpOnce() {
        classLoader = ISearchTest.class.getClassLoader();
    }

    @Before
    public void setUp() {
        iSearch = new ISearch();
    }

    @Test
    public void testLoadFile_NonExistentFile() {
        // set up expectations
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("File 'non_existent.txt' does not exist");

        // method under test
        iSearch.loadFile("non_existent.txt");
    }

    @Test
    public void testLoadFile_EmptyFile() {
        String filePath = classLoader.getResource("test_urls_empty.txt").getPath();
        // method under test
        iSearch.loadFile(filePath);

        // assertions
        assertTrue(iSearch.getUrls().isEmpty());
    }

    @Test
    public void testLoadFile_Valid() {
        String filePath = classLoader.getResource("test_urls.txt").getPath();
        // method under test
        iSearch.loadFile(filePath);

        // assertions
        List<String> urls = iSearch.getUrls();
        ImmutableList<String> expectedUrls = ImmutableList.of(
            "google.com",
            "https://www.facebook.com",
            "bing.com",
            "yahoo.com"
        );
        assertTrue(CollectionUtils.isEqualCollection(expectedUrls, urls));
    }

    @Test
    public void testSearch_NullRegEx() {
        // set up expectations
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The search string cannot be null or empty.");

        // method under test
        iSearch.search(null);
    }

    @Test
    public void testSearch_EmptyRegEx() {
        // set up expectations
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The search string cannot be null or empty.");

        // method under test
        iSearch.search("");
    }

    @Test
    public void testSearch_ValidRegExGoogle() {
        String filePath = classLoader.getResource("test_urls.txt").getPath();
        iSearch.loadFile(filePath);

        // method under test
        List<String> urls = iSearch.search("g(o)+gle");

        // assertions
        assertEquals(Collections.singletonList("google.com"), urls);
    }
}
