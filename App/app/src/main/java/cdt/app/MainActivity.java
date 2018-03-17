package cdt.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements RefreshListener {

    Party party;
    public static RefreshManager refreshManager;
    // length of time between each party data refresh
    private static final int REFRESHTIME = 1;

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

        // set this object to listen to refresh events
        refreshManager = new RefreshManager();
        refreshManager.addListener(this);


        // start the refresher and specify time between each refresh
        RefreshThread refresher = new RefreshThread(REFRESHTIME);
        refresher.start();
    }


    // onRefreshEvent is called when the RefreshThread gets data
    // from the server that should be displayed on the UI
    @Override
    public void onRefreshEvent(Party party) {
        // set the party with new data
        this.party = party;
        // let the refresh handler update the list of songs on the UI thread because
        // UI operations cannot be done on the worker thread this event is called from
        Message message = refreshHandler.obtainMessage();
        message.sendToTarget();
    }

    // create a handler object for handling refresh on UI thread
    Handler refreshHandler = new Handler(Looper.getMainLooper()) {
        @Override
        // this method is ran on the UI thread when a refresh event happens.
        // TODO: Have this method update the list of songs
        public void handleMessage(Message message) {

            // display song data for testing this should be a scrollabe list in the future
            String s = "";
            s += "Party Name: " + party.name + "\n\n";
            for(int i = 0; i < party.songs.length; i++) {
                s += "Song " + i + ":\t" + party.songs[i].id + "\n";
                s+= "\t\t Upvotes: " + party.songs[i].upvotes + " \t\tDownvotes: " + party.songs[i].downvotes + "\n\n";
            }
            final TextView testText = (TextView) findViewById(R.id.id_test_party_data);
            testText.setText(s);
        }
    };


}




