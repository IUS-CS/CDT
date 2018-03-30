package cdt.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    static final int RC_SIGN_IN = 1;
    public static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.id_signout).setOnClickListener(this);
        findViewById(R.id.id_continue).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.id_signout:
                signOut();
                break;
            case R.id.id_continue:
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGN_IN", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    public void updateUI(GoogleSignInAccount acct) {
        if (acct == null) {

            ConstraintLayout signIn = (ConstraintLayout) findViewById(R.id.sign_in_layout);
            signIn.setVisibility(View.VISIBLE);

            ConstraintLayout signedIn = (ConstraintLayout) findViewById(R.id.signed_in_layout);
            signedIn.setVisibility(View.GONE);

        } else {

            // begin downloading user photo
            if (acct.getPhotoUrl() != null) {
                new DownloadPhoto() {
                   @Override
                   public void onPostExecute(Long result) {
                       // where bm is the inherited bitmap image downloaded
                       setUserPhoto(bm);
                   }
                }.execute(acct.getPhotoUrl().toString());
            } else {
                // set the image to a stock image if no account photo exists
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.headshotdefault);
                setUserPhoto(bm);
            }

            TextView firstName = (TextView) findViewById(R.id.id_firstname);
            firstName.setText(acct.getGivenName());

            TextView lastName = (TextView) findViewById(R.id.id_lastname);
            lastName.setText(acct.getFamilyName());

            TextView email = (TextView) findViewById(R.id.id_email);
            email.setText(acct.getEmail());

            ConstraintLayout signIn = (ConstraintLayout) findViewById(R.id.sign_in_layout);
            signIn.setVisibility(View.GONE);

            ConstraintLayout signedIn = (ConstraintLayout) findViewById(R.id.signed_in_layout);
            signedIn.setVisibility(View.VISIBLE);
        }
    }

    // sets the google photo of the user
    // TODO check if null bm
    public void setUserPhoto(Bitmap bm) {
        ImageView usrImg = (ImageView) findViewById(R.id.id_usr_img);
        usrImg.setImageBitmap(bm);
    }


}
