package cdt.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                final TextView buttonMessage = (TextView) findViewById(R.id.buttonMessage);
                buttonMessage.setText("This button executed code!");
            }
        });

        final Button settingsButton = findViewById(R.id.settingsButton_id);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                   startActivity(new Intent(MainActivity.this, SettingsActivity.class));

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
            Toast toast = Toast.makeText(this, "could not parse example json file", 3);
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
            Toast toast = Toast.makeText(this, "could not read file", 3);
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