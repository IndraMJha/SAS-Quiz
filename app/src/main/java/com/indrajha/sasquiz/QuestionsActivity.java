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

import java.util.Locale;

public class QuestionsActivity extends AppCompatActivity {
    TextView tv;
    Button nextButton, prevButton, quitButton;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;

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

        if (part_name.equals("Part 1")) {
            question=Part1QuestionList.questions;
            opt=Part1OptionsList.opt;
            answers =Part1AnswersList.answers;
            switch (paper_name) {
                case ("Paper 1 SET1"):
                    flag = 0;
                    break;
                case ("Paper 1 SET2"):
                    flag = 100;
                    break;
                case ("Paper 1 SET3"):
                    flag = 200;
                    break;
                case ("Paper 1 SET4"):
                    flag = 300;
                    break;
                case ("PAPER2 SET1"):
                    flag = 400;
                    break;
                case ("PAPER2 SET2"):
                    flag = 500;
                    break;
                case ("PAPER2 SET3"):
                    flag = 600;
                    break;
                case ("PAPER2 SET4"):
                    flag = 700;
                    break;
                case ("PAPER2 SET5"):
                    flag = 800;
                    break;
                case ("PAPER2 SET6"):
                    flag = 900;
                    break;
                case ("PAPER2 SET7"):
                    flag = 1000;
                    break;
                case ("PAPER2 SET8"):
                    flag = 1100;
                    break;
                case ("PAPER2 SET9"):
                    flag = 1200;
                    break;
                case ("PAPER2 SET10"):
                    flag = 1300;
                    break;
                case ("Paper 2 SET 11"):
                    flag = 1400;
                    break;
                case ("Paper 2 SET 12"):
                    flag = 1500;
                    break;
                case ("Paper 2 SET 13"):
                    flag = 1600;
                    break;
            }
        } else {
            //Part if 2
            question=Part2QuestionList.questions;
            opt=Part2OptionsList.opt;
            answers =Part2AnswersList.answers;
            switch (paper_name) {
                //"OM IX - Set 1", "OM IX - Set 2", "OM IX - Set 3"}
                case ("OM IX - Set 1"):
                    flag = 0;
                    break;
                case ("OM IX - Set 2"):
                    flag = 100;
                    break;
                case ("OM IX - Set 3"):
                    flag = 200;
                    break;
            }
        }

        stQuest=1;
        maxQuest = Math.min(question.length - flag, 100);

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
        tv.setText(question[flag]);
        rb1.setText(opt[flag*4]);
        rb2.setText(opt[flag*4 +1]);
        rb3.setText(opt[flag*4 +2]);
        rb4.setText(opt[flag*4 +3]);

        nextButton.setOnClickListener(v -> {
            if(radio_g.getCheckedRadioButtonId()==-1)
            {
                Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton uAns = findViewById(radio_g.getCheckedRadioButtonId());
            String ansText = uAns.getText().toString();
           // Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();
            if(ansText.equals(answers[flag])) {
                correct++;
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            }
            else {
                wrong++;
                Toast.makeText(getApplicationContext(), "Wrong. "+"Correct answer is : "+answers[flag], Toast.LENGTH_SHORT).show();
            }
            flag++;
            stQuest++;
            prev=0;
            tempV.setText("prev = " + prev + " stQuest = " + stQuest + " flag = " + flag);
            if (score != null)
                score.setText("" + correct );
            // previously if(flag<questions.length)
            if(stQuest<maxQuest)
            {
                textView.setText("Q.No. " + stQuest + " / " + maxQuest + " : " + paper_name);
               /// textView.setText("Question No. " + (flag+1)+" / "+questions.length);
                tv.setText(question[flag]);
                rb1.setText(opt[flag*4]);
                rb2.setText(opt[flag*4 +1]);
                rb3.setText(opt[flag*4 +2]);
                rb4.setText(opt[flag*4 +3]);
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
                if(stQuest - 1 - prev == 0) {
                    Toast.makeText(getApplicationContext(), "You are at the first Question", Toast.LENGTH_SHORT).show();
                    tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
                }
                else {
                    prev++;
                    textView.setText("Q.No. " + (stQuest - prev) + " / " + maxQuest + " : " + paper_name);
                    /// textView.setText("Question No. " + (flag+1)+" / "+questions.length);
                    tv.setText(question[flag - prev]);
                    rb1.setText(opt[(flag - prev) * 4]);
                    rb2.setText(opt[(flag - prev) * 4 + 1]);
                    rb3.setText(opt[(flag - prev) * 4 + 2]);
                    rb4.setText(opt[(flag - prev) * 4 + 3]);
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
                // Timer finished, handle accordingly
                // For example, navigate to the ResultActivity with the total time
                Intent intent = new Intent(QuestionsActivity.this, ResultActivity.class);
                //intent.putExtra("totalTimeTaken", (totalTimeInMillis-timeRemainingInMillis)/1000);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
        timerRunning = true;
    }
    /// Override the `onPause` and `onResume` methods to handle pausing and resuming the timer when the app goes into the background and comes back to the foreground:
    @Override
    protected void onPause() {
        super.onPause();
        if (timerRunning) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (timerRunning) {
            countDownTimer.start();
        }
        startTimer();
    }
}