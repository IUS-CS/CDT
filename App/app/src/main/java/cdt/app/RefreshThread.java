package cdt.app;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * This class is responsible establishing a network connection to the server and refreshing data
 * network communications cannot be accomplished on the main thread because
 * it would greatly slow down the responsiveness of the app
 */

public class RefreshThread extends Thread {


    private static final String TAG = "Refresh";


    // the amount of time between party data requests (~=onRefreshEvent time)
    private int refreshTime;

    // constructor sets onRefreshEvent time
    public RefreshThread(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    // executes when thread is ran
    public void run() {

        while (true) {


            // request an parse json data into a part object
            Party p = parseJSONToParty(requestPartyData("Damir"));

            // notify the main thread or other listeners of the new data
            RefreshManager.notifyRefresh(p);

            // attempt sleep for onRefreshEvent time
            try {
                sleep(refreshTime * 1000);
            } catch (java.lang.InterruptedException e) {
                Log.e(TAG, "Thread was interrupted while attempting onRefreshEvent", e);
                break;
            }
        }
    }


    // gets a string of the json data from the server
    // returns null
    public String requestPartyData(String partyName) {

        String url = "http://www.solidaycl.com:8080/party/" + partyName;

        InputStream is;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            is.close();
            return jsonText;
        }
        catch (IOException e) {
            Log.e(TAG, "IO exception when requesting party data", e);
        }
        return null;
    }

    // builds a string from a buffered reader
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /*
     * Parses the json string and returns the resulting Party object
     */
    public Party parseJSONToParty(String jsonData) {

        Party party = new Party();
        try {
            JSONObject jsonPartyData = new JSONObject(jsonData);
            party.name = jsonPartyData.getString("name");
            //party.lastChange = jsonPartyData.getString("lastChange");
            //party.creator = jsonPartyData.getString("creator");
            JSONArray jsonSongs = jsonPartyData.getJSONArray("songs");
            party.songs = new Song[jsonSongs.length()];
            for(int i = 0; i < party.songs.length; i++) {
                party.songs[i] = new Song();
                party.songs[i].id = jsonSongs.getJSONObject(i).getString("id");
                party.songs[i].upvotes = jsonSongs.getJSONObject(i).getInt("upvotes");
                party.songs[i].downvotes = jsonSongs.getJSONObject(i).getInt("downvotes");
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "error when parsing json string", e);
        }
        return party;
    }
}
