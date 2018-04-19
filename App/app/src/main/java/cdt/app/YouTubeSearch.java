package cdt.app;

/**
 * Created by Damir on 4/1/2018.
 */


        import com.google.android.gms.auth.api.Auth;
        import com.google.api.client.googleapis.json.GoogleJsonResponseException;
        import com.google.api.client.http.HttpRequest;
        import com.google.api.client.http.HttpRequestInitializer;
        import com.google.api.client.http.javanet.NetHttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.jackson2.JacksonFactory;
        import com.google.api.client.util.Joiner;
        //import com.google.api.services.samples.youtube.cmdline.Auth;
        import com.google.api.services.youtube.YouTube;
        import com.google.api.services.youtube.model.GeoPoint;
        import com.google.api.services.youtube.model.SearchListResponse;
        import com.google.api.services.youtube.model.SearchResult;
        import com.google.api.services.youtube.model.SearchResultSnippet;
        import com.google.api.services.youtube.model.Thumbnail;
        import com.google.api.services.youtube.model.Video;
        import com.google.api.services.youtube.model.VideoListResponse;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Properties;

public class YouTubeSearch {

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
     */
    public static Song Search(String searchQuery) {
        // Read the developer key from the properties file.
        Properties properties = new Properties();

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("The People's Playlist").build();


            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = YouTubeConfig.getApiKey();
            search.setKey(apiKey);
            search.setQ(searchQuery);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // As a best practice, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            List<String> videoIds = new ArrayList<String>();

            if (searchResultList != null) {

                // Merge video IDs
                Song song = new Song();

                for (SearchResult searchResult : searchResultList) {
                    String id = searchResult.getId().getVideoId();
                    String title = searchResult.getSnippet().getTitle();
                    String imageURl = searchResult.getSnippet().getThumbnails().getDefault().getUrl();

                    song.id = id;
                    song.title = title;
                    song.imageUrl = imageURl;
                }
                return song;
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
