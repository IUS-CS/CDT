package cdt.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NickNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);

        final Button mainButton = findViewById(R.id.mainButton_id);
        mainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                startActivity(new Intent(NickNameActivity.this, MainActivity.class));



            }
        });

        final Button startAppButton = findViewById(R.id.startAppButton_id);
        startAppButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button



                //startActivity(new Intent(NickNameActivity.this, MainActivity.class));



            }
        });
    }


}
