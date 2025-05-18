package com.example.dhanify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CoursesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Back Button
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    // Course 1 Link
    public void openCourse1(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=g_bqQxkJWlo"));
        startActivity(intent);
    }

    // Course 2 Link
    public void openCourse2(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=_chiIIxM9Ys"));
        startActivity(intent);
    }

    // Course 3 Link
    public void openCourse3(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=PHe0bXAIuk0"));
        startActivity(intent);
    }

    // Course 4 Link
    public void openCourse4(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=8_tnf1oI1Zk"));
        startActivity(intent);
    }
}
