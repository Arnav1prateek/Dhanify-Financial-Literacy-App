package com.example.dhanify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GoalsActivity extends AppCompatActivity {

    private static final String TAG = "GoalsActivity";
    private EditText notesInput;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        notesInput = findViewById(R.id.notes_input);
        Button saveButton = findViewById(R.id.save_button);
        Button viewGoalsButton = findViewById(R.id.view_goals_button);
        Button backButton = findViewById(R.id.back_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        saveButton.setOnClickListener(v -> {
            String goalTitle = notesInput.getText().toString().trim();
            if (!goalTitle.isEmpty()) {
                String goalId = db.collection("users").document(mAuth.getCurrentUser().getUid())
                        .collection("goals").document().getId(); // Generate unique goal ID
                String deadline = "No deadline"; // Modify as needed

                addGoal(goalId, goalTitle, deadline); // Call Firestore storage function

                // Clear input field after saving
                notesInput.setText("");
            } else {
                Toast.makeText(GoalsActivity.this, "Please enter a goal!", Toast.LENGTH_SHORT).show();
            }
        });


        viewGoalsButton.setOnClickListener(v -> startActivity(new Intent(GoalsActivity.this, ViewGoalsActivity.class)));

        backButton.setOnClickListener(v -> finish());
    }

    private void addGoal(String goalId, String goalTitle, String deadline) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User not authenticated.");
            return;
        }

        DocumentReference goalRef = db.collection("users")
                .document(user.getUid())
                .collection("goals")
                .document(goalId);

        Map<String, Object> goalData = new HashMap<>();
        goalData.put("title", goalTitle);
        goalData.put("deadline", deadline);
        goalData.put("completed", false);

        goalRef.set(goalData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Goal added successfully!"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add goal", e));
    }
}
