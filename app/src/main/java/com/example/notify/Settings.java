package com.example.notify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch deleteSwitch;
    Button feedbackButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        deleteSwitch = findViewById(R.id.deleteSwitch);
        feedbackButton = findViewById(R.id.feedbackButton);
        backButton = findViewById(R.id.backButton);

        // Handle switch state change
        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                https://openclassrooms.com/fr/courses/4517166-developpez-votre-premiere-application-android/7298662-memorisez-les-preferences-du-joueur
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        //Handle feedback button sending an email
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@hotmail.com" }); // TODO mail address
                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback on the Notify! android app");
//                Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });

        //Handle back button going back to main page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}