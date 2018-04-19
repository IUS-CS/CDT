package cdt.app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by solidaycl on 4/17/18.
 */
public class YouTubeSearchTest {

    @Test
    public void testSearchSongsByQueryNotEmptyID() {
        Song s = YouTubeSearch.Search("Adele hello");
        assertNotEquals("", s.id);
    }
    @Test
    public void testSearchSongsByQueryNotEmptyTitle() {
        Song s = YouTubeSearch.Search("Adele hello");
        assertNotEquals("", s.title);
    }
    @Test
    public void testSearchSongsByQueryNotEmptyImagePath() {
        Song s = YouTubeSearch.Search("Adele hello");
        assertNotEquals("", s.imageUrl);
    }


    // these songs will fail after Adele's song is less popular
    @Test
    public void testSearchSongsByQueryID() {
        Song s = YouTubeSearch.Search("Adele Hello");
        assertEquals("YQHsXMglC9A", s.id);
    }

    @Test
    public void testSearchSongsByQueryTitle() {
        Song s = YouTubeSearch.Search("Adele Hello");
        assertEquals("Adele - Hello", s.title);
    }

    @Test
    public void testSearchSongsByQueryThumbnail() {
        Song s = YouTubeSearch.Search("Adele Hello");
        assertEquals("https://i.ytimg.com/vi/YQHsXMglC9A/default.jpg", s.imageUrl);
    }
}