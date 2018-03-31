package cdt.app;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity implements RefreshListener {


    // party data
    Party party;


    public JoinSongListAdapter songAdapter;

    // length of time between each party data refresh
    private static final int REFRESH_TIME = 3;

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
        party.songs[0].title = "song title";
        party.songs[0].imageUrl  = "helkasdfj";




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
            return JoinActivity.this.party.songs[position].title;
        }

        @Override
        public long getItemId(int position) {
            return JoinActivity.this.party.songs[position].id.hashCode();
        }

        public String getId(int position) {
            return JoinActivity.this.party.songs[position].id;
        }

        // gets the number of upvotes on a song at a position
        protected int getNumUpvotes(int position) {
            return JoinActivity.this.party.songs[position].upvotes;
        }

        // gets the number of downvotes on a song at a position
        protected int getNumDownvotes(int position) {
            return JoinActivity.this.party.songs[position].downvotes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.song_list_item_join, container, false);
            }

            // to use the position we must use a variable declared final
            final int p = position;
            final View cView = convertView;

            // set the song title
            ((TextView) cView.findViewById(R.id.id_song_title))
                    .setText(getItem(position));


            // set the upvote button action
            ImageButton upvoteButton = (ImageButton) cView.findViewById(R.id.id_upvote_button);
            upvoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: begin load animation for the time until vote takes place
                    party.songs[p].upvotes += 1; // change this so the app feels responsive
                    //TODO: gray out button so user does not try to re-vote
                    updateNumVotes(cView, p);

                    //TODO: come up with a better solution that won't leak
                    new RequestTask() {
                        @Override
                        public void onPostExecute(Long result) {
                            int code = (int)((long)result);
                            if(code == 200) {
                                JoinSongListAdapter.this.notifyDataSetChanged();
                            } else {
                                Toast.makeText(JoinActivity.this, "unable to upvote, error code: " + code, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(ServerRequest.upvoteSong(MainActivity.account.getId(), party.name, getId(p)));
                }
            });

            // set the downvote button action
            ImageButton downvoteButton = (ImageButton) convertView.findViewById(R.id.id_downvote_button);
            downvoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: begin load animation for the time until vote takes place
                    party.songs[p].upvotes -= 1; // change this so the app feels responsive
                    //TODO: gray out button so user does not try to re-vote
                    updateNumVotes(cView, p);

                    //TODO: come up with a better solution that won't leak
                    new RequestTask() {
                        @Override
                        public void onPostExecute(Long result) {
                            int code = (int)((long)result);
                            if(code == 200) {
                                JoinSongListAdapter.this.notifyDataSetChanged();
                            } else {
                                Toast.makeText(JoinActivity.this, "unable to downvote, error code: " + code, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(ServerRequest.downvoteSong(MainActivity.account.getId(), party.name, getId(p)));

                }
            });

            // load the youtube thumbnail image
            final ImageView youtubeThumbnail = cView.findViewById(R.id.id_song_thumbnail);
            // TODO: come up with a better solution that won't leak
            new DownloadPhoto() {
                @Override
                public void onFinish(Bitmap result) {
                    youtubeThumbnail.setImageBitmap(result);
                }
            }.execute(party.songs[p].imageUrl);


            // update the number of votes
            updateNumVotes(cView, p);

            return cView;
        }

        // set the number of votes (upvotes - downvotes)
        // color TextView green for positive and red for negative
        public void updateNumVotes(View v, int position) {
            int voteValue = getNumUpvotes(position) - getNumDownvotes(position);
            TextView numUpvotesDownvotes = (TextView) v.findViewById(R.id.id_number_upvotes_and_downvotes);
            numUpvotesDownvotes.setText(Integer.toString(voteValue));
            if (voteValue < 0 ) {
                numUpvotesDownvotes.setTextColor(Color.RED);
            } else {
                numUpvotesDownvotes.setTextColor(Color.GREEN);
            }

        }
    }
}
