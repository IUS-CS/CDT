package cdt.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class JoinActivity extends AppCompatActivity implements RefreshListener {

    Party party;
    // length of time between each party data refresh
    private static final int REFRESH_TIME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // set this object to listen to refresh events
        RefreshManager.setListener(this);

        // TODO: party name should get used somewhere as parameter
        // start the refresher and specify time between each refresh
        RefreshThread refresher = new RefreshThread(REFRESH_TIME);
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
            final TextView testText = (TextView) findViewById(R.id.party_data_text_id);
            testText.setText(s);
        }
    };
}
