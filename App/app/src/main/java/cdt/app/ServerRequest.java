package cdt.app;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * These methods are used to generate the proper string array to be used by the RequestTask class.
 * The descriptions do not actually perform the operations but describe how they should be used for
 * the request class.
 */

public class ServerRequest {

    public static final String SERVER_ADDRESS = "http://www.solidaycl.com:8080/party/";

    // creates a party on the server
    public static String[] createParty(String userId, String partyName) {
        return new String[] {"POST", SERVER_ADDRESS + partyName + "?id=" + userId};
    }

    // deletes a party on the server
    public static String[] deleteParty(String userId, String partyName) {
        return new String[] {"DELETE", SERVER_ADDRESS + partyName + "?id=" + userId};
    }

    // adds a song to the server
    public static String[] addSong(String userId, String partyName, String songID, String title, String imageUrl) {
        return new String[] {"POST", SERVER_ADDRESS + partyName + "/" + songID + "&title=" + title + "&imageUrl=" + imageUrl};
    }

    // delete a song from the server
    public static String[] deleteSong(String userId, String partyName, String songID) {
        return new String[] {"DELETE", SERVER_ADDRESS + partyName + "/" + songID + "?id=" + userId};
    }

    // adds an upvote to a song
    public static String[] upvoteSong(String userId, String partyName, String songID) {
        return new String[] {"POST", SERVER_ADDRESS + partyName + "/" + songID + "/upvote" + "?id=" + userId};
    }

    // removes the upvote for a song
    public static String[] deleteUpvoteSong(String userId, String partyName, String songID) {
        return new String[] {"DELETE", SERVER_ADDRESS + partyName + "/" + songID + "/upvote" + "?id=" + userId};
    }

    // adds a downvote to a song
    public static String[] downvoteSong(String userId, String partyName, String songID) {
        return new String[] {"POST", SERVER_ADDRESS + partyName + "/" + songID + "/downvote" + "?id=" + userId};
    }

    // deletes a downvote to a song
    public static String[] deleteDownvoteSong(String userId, String partyName, String songID) {
        return new String[] {"DELETE", SERVER_ADDRESS + partyName + "/" + songID + "/downvote" + "?id=" + userId};
    }
}
