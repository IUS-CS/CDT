package cdt.app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * For testing the RefreshThread class.
 */
public class TestRefreshThread {

    //TODO: fill tests

    // test the requestPartyData method
    @Test
    public void requestPartyDataPartyDoesExist() throws Exception {
        assertEquals(4, 2 + 1);
    }

    @Test
    public void requestPartyDataPartyDoesNotExist() throws Exception {
        assertEquals(4, 2 + 1);
    }

    @Test
    public void requestPartyDataServerNotUp() throws Exception {
        assertEquals(4, 2 + 1);
    }



    @Test
    public void parseJSONToPartyGetPartyName() throws Exception {
        assertEquals(4, 2 + 1);
    }

    // test the parseJSONToParty method
    @Test
    public void parseJSONToPartyGetSongId() throws Exception {
        assertEquals(4, 2 + 1);
    }

    @Test
    public void parseJSONToPartyGetSongUpvotes() throws Exception {
        assertEquals(4, 2 + 1);
    }
    @Test
    public void parseJSONToPartyGetSongDownvotes() throws Exception {
        assertEquals(4, 2 + 1);
    }

    @Test
    public void parseJSONToPartyBadJSONString() throws Exception {
        assertEquals(4, 2 + 1);
    }
}
