package com.example.dhanify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private int score = 0;

    private String[][] questionsAndAnswers = {
            {"Which of the following is a key financial asset according to Rich Dad Poor Dad?", "Stocks and Bonds", "Savings Account", "House", "Retirement Plan"},
            {"What is the primary purpose of a budget?", "To plan and manage finances", "To increase expenses", "To reduce savings", "To avoid investments"},
            {"Which of these is a liability?", "Credit Card Debt", "Rental Property", "Savings Account", "Stock Portfolio"},
            {"What does ROI stand for in financial terms?", "Return on Investment", "Rate of Income", "Revenue Over Investment", "Risk of Investment"},
            {"What is the best way to grow wealth?", "Investing", "Spending", "Saving", "Borrowing"}
    };

    private TextView questionText;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize UI elements
        questionText = findViewById(R.id.question_text);
        option1Button = findViewById(R.id.option1_button);
        option2Button = findViewById(R.id.option2_button);
        option3Button = findViewById(R.id.option3_button);
        option4Button = findViewById(R.id.option4_button);
        backButton = findViewById(R.id.back_button);

        // Display the first question
        updateQuestion();

        // Set click listeners for the answer buttons
        option1Button.setOnClickListener(v -> checkAnswer(option1Button.getText().toString()));
        option2Button.setOnClickListener(v -> checkAnswer(option2Button.getText().toString()));
        option3Button.setOnClickListener(v -> checkAnswer(option3Button.getText().toString()));
        option4Button.setOnClickListener(v -> checkAnswer(option4Button.getText().toString()));

        backButton.setOnClickListener(v -> finish());
    }

    private void updateQuestion() {
        if (currentQuestionIndex < questionsAndAnswers.length) {
            // Load the next question and options
            String[] currentQuestion = questionsAndAnswers[currentQuestionIndex];
            questionText.setText(currentQuestion[0]);
            option1Button.setText(currentQuestion[1]);
            option2Button.setText(currentQuestion[2]);
            option3Button.setText(currentQuestion[3]);
            option4Button.setText(currentQuestion[4]);
        } else {
            // Save score as coins in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("DhanifyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("coins", prefs.getInt("coins", 0) + score); // Add to existing coins
            editor.apply();

            // Display score to the user
            questionText.setText("Congratulations! You earned " + score + " coins!");

            // Hide all option buttons after quiz ends
            option1Button.setVisibility(View.GONE);
            option2Button.setVisibility(View.GONE);
            option3Button.setVisibility(View.GONE);
            option4Button.setVisibility(View.GONE);

            // Inspirational message
            Toast.makeText(this, "Coins added! Check the Simulation Page.", Toast.LENGTH_LONG).show();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        String correctAnswer = questionsAndAnswers[currentQuestionIndex][1];
        if (selectedAnswer.equals(correctAnswer)) {
            score += 10;
            Toast.makeText(this, "Correct Answer! +10 coins", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
        currentQuestionIndex++;
        updateQuestion();
    }
}
