package com.indrajha.sasquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionsCSV_ReadActivity extends AppCompatActivity {
    private TextView tv, timerTextView, textView, tempV, score;
    private Button nextButton, prevButton, quitButton;
    private RadioGroup radio_g;
    private RadioButton rb1, rb2, rb3, rb4;
    private CountDownTimer countDownTimer;
    public static long marks = 0, correct = 0, wrong = 0, totalTimeInMillis = 0, timeRemainingInMillis = 0;
    private boolean timerRunning = false;
    private int flag = 0, prev = 0, maxQuest = 0, stQuest = 0, currentIndex = 0;
    private List<String[]> questionsList = new ArrayList<>();
    public static String part_name, paper_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent intent = getIntent();
        part_name = intent.getStringExtra("part");
        paper_name = intent.getStringExtra("paper");

        // UI Initialization
        textView = findViewById(R.id.DispName);
        tempV = findViewById(R.id.tempView);
        score = findViewById(R.id.tvScoreV);
        tv = findViewById(R.id.tvque);
        timerTextView = findViewById(R.id.timer);

        nextButton = findViewById(R.id.buttonNext);
        prevButton = findViewById(R.id.buttonPrev);
        quitButton = findViewById(R.id.buttonquit);

        radio_g = findViewById(R.id.answersgrp);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);

        // Reset scores
        correct = 0;
        wrong = 0;
        marks = 0;

        if (part_name != null && (part_name.equals("Part 1 : Book wise") || part_name.equals("Latest Questions"))) {
            fetchQuestionsFromFirebase(paper_name);
        } else {
            Toast.makeText(this, "Select a valid Book-wise paper", Toast.LENGTH_SHORT).show();
        }

        prevButton.setOnClickListener(v -> goToPreviousQuestion());
        nextButton.setOnClickListener(v -> goToNextQuestion());

        quitButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(intent1);
            finish();
        });
        
        startTimer();
    }

    private void fetchQuestionsFromFirebase(String paperName) {
        // Use the paper name directly as the Firebase node name
        // Firebase path: questions/<paperName>/q1, q2, ...
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions").child(paperName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String text = snapshot.child("text").getValue(String.class);
                    String ans = snapshot.child("answer").getValue(String.class);
                    
                    List<String> opts = new ArrayList<>();
                    DataSnapshot optsSnapshot = snapshot.child("options");
                    for (DataSnapshot opt : optsSnapshot.getChildren()) {
                        opts.add(opt.getValue(String.class));
                    }
                    
                    if (text != null && opts.size() >= 4 && ans != null) {
                        String[] qData = new String[]{text, opts.get(0), opts.get(1), opts.get(2), opts.get(3), ans};
                        questionsList.add(qData);
                    }
                }
                
                if (!questionsList.isEmpty()) {
                    maxQuest = Math.min(questionsList.size(), 100);
                    currentIndex = 0;
                    stQuest = 1;
                    displayCurrentQuestion();
                } else {
                    Toast.makeText(QuestionsCSV_ReadActivity.this, "No questions found in Firebase for: " + paperName, Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QuestionsCSV_ReadActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayCurrentQuestion() {
        if (questionsList.isEmpty() || currentIndex >= questionsList.size()) return;
        
        String[] currentQuestion = questionsList.get(currentIndex);
        
        textView.setText("Q.No. " + stQuest + " / " + maxQuest + " : " + paper_name);
        if (tempV != null) tempV.setText("Question Loaded from Firebase");
        
        tv.setText(currentQuestion[0]);
        rb1.setText(currentQuestion[1]);
        rb2.setText(currentQuestion[2]);
        rb3.setText(currentQuestion[3]);
        rb4.setText(currentQuestion[4]);
        radio_g.clearCheck();
    }

    private void goToNextQuestion() {
        if (radio_g.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(radio_g.getCheckedRadioButtonId());
        String selectedOption = selectedRadioButton.getText().toString();
        String[] currentQuestion = questionsList.get(currentIndex);
        String correctAnswer = currentQuestion[5];

        if (selectedOption.equals(correctAnswer)) {
            correct++;
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            wrong++;
            Toast.makeText(getApplicationContext(), "Wrong. Correct answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        if (score != null) score.setText("" + correct);

        if (stQuest < maxQuest && currentIndex < questionsList.size() - 1) {
            currentIndex++;
            stQuest++;
            displayCurrentQuestion();
        } else {
            marks = correct;
            Intent in = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(in);
            finish();
        }
    }

    private void goToPreviousQuestion() {
        if (currentIndex > 0) {
            currentIndex--;
            stQuest--;
            displayCurrentQuestion();
        } else {
            Toast.makeText(getApplicationContext(), "You are at the first Question", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        totalTimeInMillis = 60 * 60 * 1000; // 60 minutes
        timeRemainingInMillis = totalTimeInMillis;

        countDownTimer = new CountDownTimer(timeRemainingInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemainingInMillis = millisUntilFinished;
                updateTimerText();
            }
            private void updateTimerText() {
                int minutes = (int) (timeRemainingInMillis / 1000) / 60;
                int seconds = (int) (timeRemainingInMillis / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeFormatted);
            }
            @Override
            public void onFinish() {
                marks = correct;
                Intent intent = new Intent(QuestionsCSV_ReadActivity.this, ResultActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
        timerRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerRunning && countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timerRunning && countDownTimer != null) {
            countDownTimer.start();
        }
    }
}
