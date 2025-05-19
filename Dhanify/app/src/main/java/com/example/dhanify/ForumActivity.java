package com.example.dhanify;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        // Initialize and set the text view
        TextView forumText = findViewById(R.id.forum_text);
        forumText.setText("If you have any queries please contact us at linkerslingo@gmail.com\n\nAdditional Features COMING SOON");
    }
}
