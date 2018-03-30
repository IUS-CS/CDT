package cdt.app;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class contains static methods for adding and removing party data from the server
 */

public class ServerRequest {

    public static final String SERVER_ADDRESS = "http://www.solidaycl.com:8080/party/";

    // creates a party on the server"
    public static int createParty(String userId, String partyName) throws IOException{
        return request("POST", SERVER_ADDRESS + partyName + "?id=" + userId);
    }

    // deletes a party on the server
    public static int deleteParty(String userId, String partyName) throws IOException{
        return request("DELETE", SERVER_ADDRESS + partyName + "?id=" + userId);
    }

    // adds a song to the server
    public static int addSong(String userId, String partyName, String songID, String title, String imageUrl) throws IOException{
        return request("POST", SERVER_ADDRESS + partyName + "/" + songID + "&title=" + title + "&imageUrl=" + imageUrl);
    }

    // delete a song from the server
    public static int deleteSong(String userId, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + partyName + "/" + songID + "?id=" + userId);
    }

    // adds an upvote to a song
    public static int upvoteSong(String userId, String partyName, String songID) throws IOException{
        return request("POST", SERVER_ADDRESS + partyName + "/" + songID + "/upvote" + "?id=" + userId);
    }

    // removes the upvote for a song
    public static int deleteUpvoteSong(String userId, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + partyName + "/" + songID + "/upvote" + "?id=" + userId);
    }

    // adds a downvote to a song
    public static int downvoteSong(String userId, String partyName, String songID) throws IOException{
        return request("POST", SERVER_ADDRESS + partyName + "/" + songID + "/downvote" + "?id=" + userId);
    }

    // deletes a downvote to a song
    public static int deleteDownvoteSong(String userId, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + partyName + "/" + songID + "/downvote" + "?id=" + userId);
    }

    // make a request to the server. returns the response code of the request
    private static int request(String method, String url) throws IOException{

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod(method);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        return con.getResponseCode();
    }
}
