package com.indrajha.sasquiz;

import android.content.Intent;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.os.AsyncTask;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    Button nextButton, prevButton, quitButton;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;
    private DataRepository repository;
    private List<Question> questionsList = new ArrayList<>();

    ///  Timer variables
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    public static long totalTimeInMillis = 0;
    public static long timeRemainingInMillis = 0;
    private boolean timerRunning = false;

    int flag, prev=0, maxQuest,stQuest;
    public static long marks=0,correct=0,wrong=0;
    public static String[] question;
    public static String[] opt;
    public static String[] answers;
    //////get/display part and paper info from WelcomeActivity
    public static String part_name;
    public static String paper_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        final TextView score = findViewById(R.id.tvScoreV);
        final TextView textView= findViewById(R.id.DispName);
        final TextView tempV = findViewById(R.id.tempView);

        Intent intent = getIntent();
        part_name=intent.getStringExtra("part");
        paper_name= intent.getStringExtra("paper");

        repository = new DataRepository(this);
        loadQuestions();
    }

    private void loadQuestions() {
        new AsyncTask<Void, Void, List<Question>>() {
            @Override
            protected List<Question> doInBackground(Void... voids) {
                return repository.getQuestions(part_name, paper_name);
            }

            @Override
            protected void onPostExecute(List<Question> questions) {
                questionsList = questions;
                if (questionsList.isEmpty()) {
                    Toast.makeText(QuestionsActivity.this, "No questions found for " + paper_name, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                initViews();
            }
        }.execute();
    }

    private void initViews() {
        final TextView score = findViewById(R.id.tvScoreV);
        final TextView textView= findViewById(R.id.DispName);
        final TextView tempV = findViewById(R.id.tempView);

        flag = 0; // Current question index within the paper
        stQuest = 1;
        maxQuest = questionsList.size();

        nextButton= findViewById(R.id.buttonNext);
        prevButton= findViewById(R.id.buttonPrev);
        quitButton= findViewById(R.id.buttonquit);

        tv= findViewById(R.id.tvque);

        radio_g= findViewById(R.id.answersgrp);
        rb1= findViewById(R.id.radioButton);
        rb2= findViewById(R.id.radioButton2);
        rb3= findViewById(R.id.radioButton3);
        rb4= findViewById(R.id.radioButton4);

        // for Total number of questions in Set Using maxQuest that is 100, value instead of questions.length (that is 400 now)
        textView.setText("Q.No. " + stQuest + " / " + maxQuest + " : " + paper_name);
        tempV.setText("prev = " + prev + " stQuest = " + stQuest + " flag = " + flag);
        
        displayQuestion();

        nextButton.setOnClickListener(v -> {
            if(radio_g.getCheckedRadioButtonId()==-1)
            {
                Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton uAns = findViewById(radio_g.getCheckedRadioButtonId());
            String ansText = uAns.getText().toString();
            Question currentQ = questionsList.get(flag);
            if(ansText.equals(currentQ.getCorrectAnswer())) {
                correct++;
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            }
            else {
                wrong++;
                Toast.makeText(getApplicationContext(), "Wrong. "+"Correct answer is : "+currentQ.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            }
            flag++;
            stQuest++;
            prev=0;
            tempV.setText("prev = " + prev + " stQuest = " + stQuest + " flag = " + flag);
            if (score != null)
                score.setText("" + correct );
            if(flag < maxQuest)
            {
                textView.setText("Q.No. " + stQuest + " / " + maxQuest + " : " + paper_name);
                displayQuestion();
                tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
            }
            else
            {
                marks=correct;
                tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
                Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(in);
            }
            radio_g.clearCheck();
        });
        prevButton.setOnClickListener(v -> {
            if(stQuest-prev>0)
            {
                if(stQuest - 1 - prev == 1) { // Changed index logic for prev
                    Toast.makeText(getApplicationContext(), "You are at the first Question", Toast.LENGTH_SHORT).show();
                    tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
                }
                else {
                    prev++;
                    textView.setText("Q.No. " + (stQuest - prev) + " / " + maxQuest + " : " + paper_name);
                    displayQuestion(flag - prev);
                    tempV.setText("prev = " + prev + " stQuest = " + stQuest + " flag = " + flag);
                }
            }
        });
        quitButton.setOnClickListener(v -> {
            Intent intent1 =new Intent(getApplicationContext(),ResultActivity.class);
            startActivity(intent1);
        });
        ///  Timer code
        timerTextView=findViewById(R.id.timer);
        startTimer();
    }

    private void displayQuestion() {
        displayQuestion(flag);
    }

    private void displayQuestion(int index) {
        Question q = questionsList.get(index);
        tv.setText(q.getText());
        rb1.setText(q.getOption1());
        rb2.setText(q.getOption2());
        rb3.setText(q.getOption3());
        rb4.setText(q.getOption4());
        radio_g.clearCheck();
    }
    private void startTimer() {
        totalTimeInMillis = 60 * 60 * 1000; // Total time in milliseconds (60 minutes)
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
                Intent intent = new Intent(QuestionsActivity.this, ResultActivity.class);
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
        if (timerRunning && timerTextView != null) {
            startTimer();
        }
    }
}