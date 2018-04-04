package cdt.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.content.SharedPreferences;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import static android.content.ContentValues.TAG;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HostActivity extends YouTubeBaseActivity /* implements RefreshListener */ {


    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    YouTubePlayer myYouTubePlayer;
    boolean youTubeInitialized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        mYouTubePlayerView = findViewById(R.id.youTubePlayer);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user is already signed in
        // launches the signInActivity if user is not signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            startActivity(new Intent(HostActivity.this, SignInActivity.class));

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
                        myYouTubePlayer.loadVideo(video);
                    }

                }
            });

        }
    }

}
