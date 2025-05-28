package com.example.dhanify;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewGoalsActivity extends AppCompatActivity {

    private static final String TAG = "ViewGoalsActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LinearLayout activeGoalsLayout, completedGoalsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        activeGoalsLayout = findViewById(R.id.active_goals_layout);
        completedGoalsLayout = findViewById(R.id.completed_goals_layout);

        loadGoals();
    }

    private void loadGoals() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User not authenticated.");
            return;
        }

        db.collection("users").document(user.getUid())
                .collection("goals")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    activeGoalsLayout.removeAllViews();
                    completedGoalsLayout.removeAllViews();

                    int count = 0; // Counter to check if we are getting data

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String goalId = doc.getId();
                        String goalTitle = doc.getString("title");
                        String deadline = doc.getString("deadline");
                        boolean completed = doc.getBoolean("completed");

                        if (goalTitle != null) {
                            count++;
                            Log.d(TAG, "Goal Loaded: " + goalTitle + " | Completed: " + completed);
                            addGoalToUI(goalId, goalTitle, deadline, completed);
                        }
                    }

                    if (count == 0) {
                        Log.d(TAG, "No goals found for this user.");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to load goals", e));
    }


    private void addGoalToUI(String goalId, String goalTitle, String deadline, boolean completed) {
        LinearLayout goalContainer = new LinearLayout(this);
        goalContainer.setOrientation(LinearLayout.HORIZONTAL);

        TextView goalTextView = new TextView(this);
        goalTextView.setText(goalTitle + " (Deadline: " + deadline + ")");
        goalTextView.setPadding(10, 10, 10, 10);
        goalTextView.setTextColor(getResources().getColor(android.R.color.white)); // ✅ Set text color to white

        Button completeButton = new Button(this);
        completeButton.setText(completed ? "✔ Completed" : "Mark Done");
        completeButton.setEnabled(!completed);

        completeButton.setOnClickListener(v -> markGoalCompleted(goalId));

        goalContainer.addView(goalTextView);
        goalContainer.addView(completeButton);

        if (completed) {
            completedGoalsLayout.addView(goalContainer);
        } else {
            activeGoalsLayout.addView(goalContainer);
        }
    }


    private void markGoalCompleted(String goalId) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        db.collection("users")
                .document(user.getUid())
                .collection("goals")
                .document(goalId)
                .update("completed", true)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Goal marked as completed!");
                    loadGoals();  // Reload goals to reflect changes
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to update goal", e));
    }
}
