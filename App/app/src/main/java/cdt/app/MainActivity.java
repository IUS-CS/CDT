package cdt.app;

import android.content.Intent;
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

        // set this object to listen to refreshes
        refreshManager = new RefreshManager();
        refreshManager.addListener(this);


        // start the refresher
        RefreshThread refresher = new RefreshThread(3);
        refresher.start();
    }

    // refresh is called when the RefreshThread gets data
    // from the server that should be displayed on the UI
    @Override
    public void refresh(Party party) {
        this.party = party;
        Log.d("refresh", party.name);
        for(int i = 0 ; i < party.songs.length; i++)
            Log.d("refresh", party.songs[i].id);
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




