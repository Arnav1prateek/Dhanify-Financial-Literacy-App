package com.example.dhanify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView pointsDisplay, pointsTodayDisplay, streakDisplay, coinsDisplay;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Initialize TextViews
        pointsDisplay = findViewById(R.id.points_display);
        pointsTodayDisplay = findViewById(R.id.points_today_display);
        streakDisplay = findViewById(R.id.streak_display);
        coinsDisplay = findViewById(R.id.coins_display);

        // Load User Data
        loadUserData();

        // Button Listeners
        ImageButton quizButton = findViewById(R.id.quiz_button);
        ImageButton forumButton = findViewById(R.id.forum_button);
        ImageButton simulationButton = findViewById(R.id.simulation_button);
        ImageButton goalsButton = findViewById(R.id.goals_button);
        ImageButton coursesButton = findViewById(R.id.courses_button);

        // Set click listeners
        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        // ✅ **Directly open the forum in the browser**
        forumButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dhanify.flarum.cloud/"));
            startActivity(browserIntent);
        });

        simulationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimulationActivity.class);
            startActivity(intent);
        });

        goalsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GoalsActivity.class);
            startActivity(intent);
        });

        coursesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DocumentSnapshot document = task.getResult();

                int points = document.contains("points") ? document.getLong("points").intValue() : 0;
                int streak = document.contains("streak") ? document.getLong("streak").intValue() : 0;
                int coins = document.contains("coins") ? document.getLong("coins").intValue() : 0;
                String lastQuizDate = document.contains("lastQuizDate") ? document.getString("lastQuizDate") : "";

                String todayDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                int pointsToday = document.contains("pointsToday") && lastQuizDate.equals(todayDate)
                        ? document.getLong("pointsToday").intValue()
                        : 0;

                // Update UI
                pointsDisplay.setText("Total Points: " + points + " 📊");
                streakDisplay.setText("Streak: " + streak + " days 🔥");
                coinsDisplay.setText("Coins: " + coins + " 💰");
                pointsTodayDisplay.setText("Points Earned Today: +" + pointsToday + " 💯");
            } else {
                Log.e("MainActivity", "Failed to load user data or user does not exist");
            }
        });
    }

    private void createNewUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("points", 0);
        userData.put("streak", 0);
        userData.put("lastQuizDate", "");
        userData.put("pointsToday", 0);
        userData.put("coins", 0);

        db.collection("users").document(userId).set(userData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            int quizPoints = data.getIntExtra("QUIZ_POINTS", 0);
            int coinsEarned = quizPoints;
            updatePointsAndStreak(quizPoints, coinsEarned);
        }
    }

    private void updatePointsAndStreak(final int quizPoints, final int coinsEarned) {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int totalPoints = documentSnapshot.contains("points") ? documentSnapshot.getLong("points").intValue() : 0;
                int streak = documentSnapshot.contains("streak") ? documentSnapshot.getLong("streak").intValue() : 0;
                int coins = documentSnapshot.contains("coins") ? documentSnapshot.getLong("coins").intValue() : 0;
                String lastQuizDate = documentSnapshot.getString("lastQuizDate");

                String todayDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                int pointsToday = documentSnapshot.contains("pointsToday") ? documentSnapshot.getLong("pointsToday").intValue() : 0;

                if (lastQuizDate == null || !lastQuizDate.equals(todayDate)) {
                    streak++;
                    pointsToday = quizPoints;
                }

                Map<String, Object> updates = new HashMap<>();
                updates.put("points", totalPoints + quizPoints);
                updates.put("streak", streak);
                updates.put("lastQuizDate", todayDate);
                updates.put("pointsToday", pointsToday);
                updates.put("coins", coins + coinsEarned);

                int finalStreak = streak;
                int finalPointsToday = pointsToday;
                docRef.update(updates).addOnSuccessListener(aVoid -> {
                    pointsDisplay.setText("Total Points: " + (totalPoints + quizPoints) + " 📊");
                    streakDisplay.setText("Streak: " + finalStreak + " days 🔥");
                    pointsTodayDisplay.setText("Points Earned Today: +" + finalPointsToday + " 💯");
                    coinsDisplay.setText("Coins: " + (coins + coinsEarned) + " 💰");
                });
            }
        });
    }
}
