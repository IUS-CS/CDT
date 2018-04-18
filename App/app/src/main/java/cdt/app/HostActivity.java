package cdt.app;

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
import android.widget.ListView;

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
                    YPlayer.loadVideo("JqowmHgxVJQ");

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
