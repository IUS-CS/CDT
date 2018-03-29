package cdt.app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to join a party
        final Button joinButton = findViewById(R.id.join_button_id);
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_join, null);
                final EditText partyName = (EditText) view.findViewById(R.id.id_party_name_join);

                Button continueButton = (Button) view.findViewById(R.id.id_join_dialog_continue);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (partyName.getText().toString().isEmpty() /*|| party name does not exist on server*/) {
                            Toast.makeText(MainActivity.this,
                                    "must put in party name to join",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, JoinActivity.class));
                        }
                    }
                });
            }
        });

    // button to host a party
    final Button hostButton = findViewById(R.id.host_button_id);
        hostButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, HostActivity.class));

        }
    });

    // button to settings activity
    final Button settingsButton = findViewById(R.id.settingsButton_id);
        settingsButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Code here executes on main thread after user presses button

            startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        }
    });

}
}




