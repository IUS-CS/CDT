package cdt.app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {

    public static GoogleSignInAccount account;

    // the party name is used by both the join and host activities
    public static String partyName;

    AlertDialog dialog;

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
                            new RequestTask() {
                                @Override
                                public void onPostExecute(Long result) {
                                    int code = (int)((long)result);
                                    if(code == 200) {
                                        MainActivity.partyName = partyName.getText().toString();
                                        dialog.dismiss();
                                        startActivity(new Intent(MainActivity.this, JoinActivity.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, "error code: " + code + " that party does not exist, make sure you spelled it correctly", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.execute(ServerRequest.checkPartyExists(MainActivity.account.getId(), partyName.getText().toString()));
                        }
                    }
                });

                // show the dialog
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });

        // button to host a party
        final Button hostButton = findViewById(R.id.host_button_id);
        hostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_host, null);
                final EditText partyName = (EditText) view.findViewById(R.id.id_song_title_field);

                Button continueButton = (Button) view.findViewById(R.id.id_host_dialog_continue);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (partyName.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    "must put in party name to host",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            new RequestTask() {
                                @Override
                                public void onPostExecute(Long result) {
                                    int code = (int)((long)result);
                                    if(code == 201) {
                                        MainActivity.partyName = partyName.getText().toString();
                                        dialog.dismiss();
                                        startActivity(new Intent(MainActivity.this, HostActivity.class));
                                    } else {
                                        //Toast.makeText(MainActivity.this, "error code: " + code + " cohosting the party not supported", Toast.LENGTH_SHORT).show();
                                        //TODO do a check if the user is the party owner and act accordingly
                                        MainActivity.partyName = partyName.getText().toString();
                                        dialog.dismiss();
                                        startActivity(new Intent(MainActivity.this, HostActivity.class));
                                    }
                                }
                            }.execute(ServerRequest.createParty(MainActivity.account.getId(), partyName.getText().toString()));
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
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }
    }
}
