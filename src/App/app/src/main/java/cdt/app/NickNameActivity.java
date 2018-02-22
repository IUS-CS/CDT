package cdt.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class NickNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        final Button saveNickNameButton = findViewById(R.id.saveNickNameButton_id);
        saveNickNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                EditText data = (EditText)findViewById(R.id.nickName_id);
                String nickname = data.getText().toString();

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(NickNameActivity.this.openFileOutput("nickname.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(nickname);
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }//try/catch
            }//onClick
        });//saveNickNameButton

        final Button checkNickNameButton = findViewById(R.id.checkNickNameButton_id);
        checkNickNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                File path = NickNameActivity.this.getFilesDir();

                File file = new File(path, "nickname.txt");

                int length = (int) file.length();

                byte[] bytes = new byte[length];

                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }//try/catch

                String nickname = new String(bytes);
                TextView output= (TextView) findViewById(R.id.currentNickName_id);
                output.setText((CharSequence) nickname);
            }//onClick
        });//checkNickNameButton

        final Button startAppButton = findViewById(R.id.startAppButton_id);
        startAppButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                File path = NickNameActivity.this.getFilesDir();

                File file = new File(path, "nickname.txt");

                int length = (int) file.length();

                byte[] bytes = new byte[length];

                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());

                }//try/catch
                String nickname = new String(bytes);

                if (!nickname.contentEquals("")) {
                    startActivity(new Intent(NickNameActivity.this, MainActivity.class));
                }//if
                else {
                    //display error message
                }//else
            }//onClick
        });//startAppButton
    }//onCreate
}//NickNameActivity
