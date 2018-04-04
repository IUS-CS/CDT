package cdt.app;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 * tests network communications
 * class only responsible for testing error codes, not if server was performing correctly in adding and removing data
 *
 *
 * Error Codes Used:
 * 200 (OK)
 * 201 (Resource Created)
 * 403 (Forbidden)
 * 404 (NotFound)
 *
 */
public class TestServerRequest {

    private final String PARTY_NAME = "TestParty";
    private final String EXAMPLE_SONG1 = "IPfJnp1guPc";
    private final String DNESONG = "lkajsfohaewjrandomnoexist";


    // create a test party
    @Test
    public void TestCreateParty() throws Exception {
        int responseCode = ServerRequest.createParty(PARTY_NAME);
        assertEquals(201, responseCode);
    }


    @Test
    public void TestCreatePartyPartyAlreadyExists() throws Exception {
        int responseCode = ServerRequest.createParty(PARTY_NAME);
        assertEquals(403, responseCode);
    }


    // add a song to the party
    @Test
    public void TestAddSong() throws Exception {
        int responseCode  = ServerRequest.upvoteSong(PARTY_NAME, EXAMPLE_SONG1);
        assertEquals(201, responseCode);
    }

    // add a duplicate song to the party (should just upvote the current song)
    @Test
    public void TestAddSongDuplicate() throws Exception {
        int responseCode  = ServerRequest.upvoteSong(PARTY_NAME, EXAMPLE_SONG1);
        assertEquals(200, responseCode);
    }

    // upvote that song that was added
    @Test
    public void TestUpvoteSongValidSongID() throws Exception {
        int responseCode  = ServerRequest.upvoteSong(PARTY_NAME, EXAMPLE_SONG1);
        assertEquals(200, responseCode);
    }

    @Test
    public void TestUpvoteSongInvalidSongID() throws Exception {
        int responseCode  = ServerRequest.upvoteSong(PARTY_NAME, DNESONG);
        assertEquals(404, responseCode);
    }

    // delete the test party
    @Test
    public void TestDeleteParty() throws Exception {
        int responseCode = ServerRequest.deleteParty(PARTY_NAME);
        assertEquals(200, responseCode);
    }

    @Test
    public void TestDeletePartyAlreadyDeleted() throws Exception {
        int responseCode = ServerRequest.deleteParty(PARTY_NAME);
        assertEquals(404, responseCode);
    }

}

