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

    // creates a party on the server
    public static int createParty(String user, String partyName) throws IOException{
        return request("POST", SERVER_ADDRESS + user + "/" + partyName);
    }

    // deletes a party on the server
    public static int deleteParty(String user, String partyName) throws IOException{
        return request("DELETE", SERVER_ADDRESS + user + "/" + partyName);
    }

    // adds a song to the server
    public static int addSong(String user, String partyName, String songID) throws IOException{
        return request("POST", SERVER_ADDRESS + user + "/" + partyName + "/" + songID);
    }

    // delete a song from the server
    public static int deleteSong(String user, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + user + "/" + partyName + "/" + songID);
    }

    // adds an upvote to a song
    public static int upvoteSong(String user, String partyName, String songID) throws IOException{
        return request("POST", SERVER_ADDRESS + user + "/" + partyName + "/" + songID + "/upvote");
    }

    // removes the upvote for a song
    public static int deleteUpvoteSong(String user, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + user + "/" + partyName + "/" + songID + "/upvote");
    }

    // adds a downvote to a song
    public static int downvoteSong(String user, String partyName, String songID) throws IOException{
        return request("POST", SERVER_ADDRESS + user + "/" + partyName + "/" + songID + "/downvote");
    }

    // deletes a downvote to a song
    public static int deleteDownvoteSong(String user, String partyName, String songID) throws IOException{
        return request("DELETE", SERVER_ADDRESS + user + "/" + partyName + "/" + songID + "/downvote");
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
