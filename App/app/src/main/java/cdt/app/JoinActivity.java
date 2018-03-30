package cdt.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class JoinActivity extends AppCompatActivity implements RefreshListener {


    // party data
    Party party;


    public JoinSongListAdapter songAdapter;

    // length of time between each party data refresh
    private static final int REFRESH_TIME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        // create a default party
        party = new Party();
        party.name = "default";
        party.songs = new Song[1];
        party.songs[0] = new Song();
        party.songs[0].upvotes = 0;
        party.songs[0].downvotes = 0;
        party.songs[0].id = "abc";
        party.songs[0].songName = "song title";




        ListView songlist = findViewById(R.id.id_song_list_join_listview);
        songAdapter = new JoinSongListAdapter();
        songlist.setAdapter(songAdapter);

        // set this object to listen to refresh events
        RefreshManager.setListener(this);

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

            songAdapter.notifyDataSetChanged();

            /*
            // display song data for testing this should be a scrollabe list in the future
            String s = "";
            s += "Party Name: " + party.name + "\n\n";
            for(int i = 0; i < party.songs.length; i++) {
                s += "Song " + i + ":\t" + party.songs[i].id + "\n";
                s+= "\t\t Upvotes: " + party.songs[i].upvotes + " \t\tDownvotes: " + party.songs[i].downvotes + "\n\n";
            }
            final TextView testText = (TextView) findViewById(R.id.party_data_text_id);
            testText.setText(s);
            */
        }
    };





    /**
     * A simple array adapter that creates a list of cheeses.
     */
    private class JoinSongListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return JoinActivity.this.party.songs.length;
        }

        @Override
        public String getItem(int position) {
            return JoinActivity.this.party.songs[position].id;
        }

        @Override
        public long getItemId(int position) {
            return JoinActivity.this.party.songs[position].id.hashCode();
        }

        // gets the number of upvotes on a song at a position
        private int getNumUpvotes(int position) {
            return JoinActivity.this.party.songs[position].upvotes;
        }

        // gets the number of downvotes on a song at a position
        private int getNumDownvotes(int position) {
            return JoinActivity.this.party.songs[position].downvotes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.song_list_item_join, container, false);
            }

            ((TextView) convertView.findViewById(R.id.id_song_title))
                    .setText(getItem(position));

/*
            ((TextView) convertView.findViewById(R.id.id_num_upvotes_downvotes))
                    .setText(getNumUpvotes(position) - getNumDownvotes(position));
                    */

            // TODO load the image

            return convertView;
        }
    }
}
