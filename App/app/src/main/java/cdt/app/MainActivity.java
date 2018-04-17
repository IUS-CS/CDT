package cdt.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.GeoPoint;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static android.content.ContentValues.TAG;

public class MainActivity extends YouTubeBaseActivity {

    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    YouTubePlayer myYouTubePlayer;
    boolean youTubeInitialized = false;

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param //args command line args.
     */
    public String searchVideo() {
        // Read the developer key from the properties file.
        Properties properties = new Properties();
        String videoResult = "";

        /*
        try {
            InputStream in = GeolocationSearch.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }
*/
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-geolocationsearch-sample").build();

            // Prompt the user to enter a query term.
            String queryTerm = getInputQuery();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = properties.getProperty(YouTubeConfig.getApiKey());
            search.setKey(apiKey);
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // As a best practice, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/videoId)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            List<String> videoIds = new ArrayList<String>();

            if (searchResultList != null) {

                // Merge video IDs
                for (SearchResult searchResult : searchResultList) {
                    videoIds.add(searchResult.getId().getVideoId());
                }
                Joiner stringJoiner = Joiner.on(',');
                String videoId = stringJoiner.join(videoIds);

                // Call the YouTube Data API's youtube.videos.list method to
                // retrieve the resources that represent the specified videos.
                YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet, recordingDetails").setId(videoId);
                VideoListResponse listResponse = listVideosRequest.execute();

                List<Video> videoList = listResponse.getItems();
                videoResult = videoList.get(0).getId();

                if (videoList != null) {
                    prettyPrint(videoList.iterator(), queryTerm);
                }
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Log.d("SearchResult =", videoResult);
        return videoResult;
    }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
    private String getInputQuery() throws IOException {

        EditText data = (EditText)findViewById(R.id.searchVideoTextBox_id);
        String video = data.getText().toString();
        String inputQuery = video;

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // Use the string "YouTube Developers Live" as a default.
            inputQuery = "Surfing";
        }
        return inputQuery;
    }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, location, and thumbnail.
     *
     * @param iteratorVideoResults Iterator of Videos to print
     *
     * @param query Search query (String)
     */
    private static void prettyPrint(Iterator<Video> iteratorVideoResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorVideoResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorVideoResults.hasNext()) {

            Video singleVideo = iteratorVideoResults.next();

            Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
            GeoPoint location = singleVideo.getRecordingDetails().getLocation();

            System.out.println(" Video Id" + singleVideo.getId());
            System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
            System.out.println(" Location: " + location.getLatitude() + ", " + location.getLongitude());
            System.out.println(" Thumbnail: " + thumbnail.getUrl());
            System.out.println("\n-------------------------------------------------------------\n");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Starting");



        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTubePlayer);

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess: Done initializing.");
                myYouTubePlayer = youTubePlayer;
                youTubeInitialized = true;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: Failed to initialize.");
            }
        };

        mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener);



        final Button settingsButton = findViewById(R.id.settingsButton_id);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

            }
        });

        final Button searchVideoButton = findViewById(R.id.searchVideoButton_id);
        searchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(youTubeInitialized) {
                    myYouTubePlayer.loadVideo(searchVideo());
                }


            }
        });

        final Button addVideoButton = findViewById(R.id.addVideoButton_id);
        addVideoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(youTubeInitialized) {
                    EditText data = (EditText)findViewById(R.id.addVideoTextBox_id);
                    String video = data.getText().toString();
                    myYouTubePlayer.loadVideo(video);
                }
                Log.d("hey", "pressed the button");

            }
        });

        Party party = getPartyInfo();

        /* example of how to get info from party object
        Toast t = Toast.makeText(this, party.songs[0].title, 7);
        t.show();
        */

    }

    /*
     * Parses the example json file in the assets folder and puts it into a Party object
     */
    public Party getPartyInfo() {

        Party party = new Party();
        try {
            JSONObject jsonPartyData = new JSONObject(loadFileFromAsset("Party.json"));
            party.name = jsonPartyData.getString("name");
            party.lastChange = jsonPartyData.getString("lastChange");
            party.creator = jsonPartyData.getString("creator");
            JSONArray jsonSongs = jsonPartyData.getJSONArray("songs");
            party.songs = new Song[jsonSongs.length()];
            for(int i = 0; i < party.songs.length; i++) {
                party.songs[i] = new Song();
                party.songs[i].title = jsonSongs.getJSONObject(0).getString("title");
                party.songs[i].id = jsonSongs.getJSONObject(0).getString("id");
                party.songs[i].img = jsonSongs.getJSONObject(0).getString("img");
                party.songs[i].upvotes = jsonSongs.getJSONObject(0).getInt("upvotes");
                party.songs[i].downvotes = jsonSongs.getJSONObject(0).getInt("downvotes");
            }

        } catch (org.json.JSONException e) {
            Toast toast = Toast.makeText(this, "could not parse example json file", Toast.LENGTH_SHORT);
            toast.show();

            e.printStackTrace();
        }
        return party;
    }

    /*
     * Loads a file from the assets folder as a string
     */
    public String loadFileFromAsset(String filename) {
        String json = null;
        try {

            // print assets in assets folder
            try {
                Log.d("TAG", Arrays.toString(getAssets().list(".")));
            } catch (IOException e) {
                Log.e("TAG", e.getLocalizedMessage(), e);
            }

            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast toast = Toast.makeText(this, "could not read file", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
        return json;
    }
}


class Party {
   String name;
   String lastChange;
   String creator;
   Song[] songs;
}


class Song {
    public String title;
    public String id;
    public String img;
    public int upvotes;
    public int downvotes;
}