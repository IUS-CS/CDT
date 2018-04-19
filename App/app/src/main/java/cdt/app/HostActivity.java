package cdt.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;


import static android.content.ContentValues.TAG;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HostActivity extends JoinActivity /* implements RefreshListener */ {


    boolean youTubeInitialized = false;

    // YouTube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;

    YouTubePlayer YPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        initializeYoutubePlayer();

        ListView songlist = findViewById(R.id.id_song_list_host_listview);
        songAdapter = new JoinSongListAdapter();
        songlist.setAdapter(songAdapter);

        // set this object to listen to refresh events
        RefreshManager.setListener(this);

        // start the refresher and specify time between each refresh
        RefreshThread refresher = new RefreshThread(REFRESH_TIME);
        refresher.start();

        final Button addButton = findViewById(R.id.id_host_activity_add_song);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HostActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_add_song, null);
                final EditText songQuery = (EditText) view.findViewById(R.id.id_song_title_field);

                Button continueButton = (Button) view.findViewById(R.id.id_add_song_dialog_button);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (songQuery.getText().toString().isEmpty()) {
                            Toast.makeText(HostActivity.this,
                                    "must put in something to search",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            final String query  = songQuery.getText().toString();
                            new Thread( new Runnable() {
                                @Override
                                public void run() {
                                    // get the song from youtube from a search query
                                    final Song s = YouTubeSearch.Search(query);

                                    // add the song to the server
                                    new RequestTask() {
                                        @Override
                                        public void onPostExecute(Long result) {
                                            int code = (int)((long)result);
                                            if(code == 201) {
                                                Toast.makeText(HostActivity.this, "successfully added song: " + s.title, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(HostActivity.this, "Unable to add song, error code: " + code, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }.execute(ServerRequest.addSong(MainActivity.account.getId(), MainActivity.partyName, s.id, s.title, s.imageUrl));
                                }
                            }).start();

                            dialog.dismiss();
                        }
                    }
                });

                // show the dialog
                builder.setView(view);
                dialog = builder.create();
                dialog.show();


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user is already signed in
        // launches the signInActivity if user is not signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            startActivity(new Intent(HostActivity.this, SignInActivity.class));

            /*
            final Button searchVideoButton = findViewById(R.id.searchVideoButton_id);
            searchVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code here executes on main thread after user presses button

                }
            });

            final Button addVideoButton = findViewById(R.id.addVideoButton_id);
            addVideoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (youTubeInitialized) {
                        EditText data = (EditText) findViewById(R.id.addVideoTextBox_id);
                        String video = data.getText().toString();
                        YPlayer.loadVideo(video);
                    }

                }
            });
            */

        }
    }



    // initializes the field YPlayer with an instance of YouTubePlayer
    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    YPlayer = player;

                    //set the player style default
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    if(party.songs != null) {
                        YPlayer.loadVideo(party.songs[0].id);
                        deleteSong(party.songs[0].id);
                    }
                    player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onVideoEnded () {
                            if(party.songs.length > 0) {
                                YPlayer.loadVideo(party.songs[0].id);
                                deleteSong(party.songs[0].id);
                            }
                        }
                        @Override
                        public void onError(YouTubePlayer.ErrorReason r) {

                        }
                        @Override
                        public void onAdStarted() {

                        }
                        @Override
                        public void onLoaded(String videoId) {

                        }
                        @Override
                        public void onLoading() {

                        }
                        @Override
                        public void onVideoStarted() {

                        }
                    });
                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

}
