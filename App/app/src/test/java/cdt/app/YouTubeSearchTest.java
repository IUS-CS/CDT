package cdt.app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by solidaycl on 4/17/18.
 */
public class YouTubeSearchTest {

    @Test
    public void testSearchsongsByQuery() {
        YouTubeSearch.Search("hello");
    }

}