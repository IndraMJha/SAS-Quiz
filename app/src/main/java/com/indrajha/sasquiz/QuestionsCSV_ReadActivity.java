package com.indrajha.sasquiz;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionsCSV_ReadActivity extends AppCompatActivity {
    private TextView tv, timerTextView;
    final TextView textView = findViewById(R.id.DispName), tempV = findViewById(R.id.tempView), score = findViewById(R.id.tvScoreV);
    Button nextButton, prevButton, quitButton;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;
    private CountDownTimer countDownTimer;
    public static long marks=0,correct=0,wrong=0, totalTimeInMillis = 0, timeRemainingInMillis = 0;
    private boolean timerRunning = false;
    private int flag, prev=0, maxQuest,stQuest, currentIndex;
    private List<String[]> questionsList = new ArrayList<>();
    public static String[] question, opt, answers;
    public static String part_name, paper_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent intent = getIntent();
        part_name=intent.getStringExtra("part");
        paper_name= intent.getStringExtra("paper");

        if (part_name.equals("Part 1 : Book wise")) {
            String csvFileName = "AccountCode.csv";

            // Read data from CSV file
            try {
                questionsList = readCSVFile(csvFileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }

            assert questionsList != null;
            String[][] questionsArray = new String[questionsList.size()][];
            questionsList.toArray(questionsArray);
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

        prevButton.setOnClickListener(v -> goToPreviousQuestion());
        nextButton.setOnClickListener(v -> goToNextQuestion());

        quitButton.setOnClickListener(v -> {
            Intent intent1 =new Intent(getApplicationContext(),ResultActivity.class);
            startActivity(intent1);
        });
        timerTextView=findViewById(R.id.timer);
    }

    private List<String[]> readCSVFile(String filename) throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        CSVParser csvParser = CSVFormat.DEFAULT.parse(inputStreamReader);
        List<String[]> dataList = new ArrayList<>();

        for (CSVRecord csvRecord : csvParser) {
            String[] questionData = new String[csvRecord.size()];
            for (int i = 0; i < csvRecord.size(); i++) {
                questionData[i] = csvRecord.get(i);
            }
            dataList.add(questionData);
        }
        csvParser.close();
        return dataList;
    }

    private void displayCurrentQuestion() {
        String[] currentQuestion = questionsList.get(currentIndex);
        String questionText = currentQuestion[0];
        String[] options = new String[4];

        System.arraycopy(currentQuestion, 1, options, 0, 4);
        String correctAnswer = currentQuestion[5]; // Assuming the answer is in the 6th column (index 5)
        // for Total number of questions in Set Using maxQuest that is 100, value instead of questions.length (that is 400 now)
        textView.setText("Q.No. " + stQuest + " / " + maxQuest + " : " + paper_name);
        tempV.setText("prev = " + prev + " stQuest = " + stQuest + " flag = " + flag);
        tv.setText(questionText);
        rb1.setText(options[0]);
        rb2.setText(options[1]);
        rb3.setText(options[2]);
        rb4.setText(options[3]);
        radio_g.clearCheck();
    }

    private String formatOptions(String[] options) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            builder.append((char) ('A' + i)).append(". ").append(options[i]);
            if (i < options.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private void goToNextQuestion() {
        if (currentIndex < questionsList.size() - 1) {
            currentIndex++;
            displayCurrentQuestion();
        } else {
            // Handle end of questions
            marks=correct;
            tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
            Intent in = new Intent(getApplicationContext(),ResultActivity.class);
            startActivity(in);
        }
        RadioButton selectedRadioButton = findViewById(radio_g.getCheckedRadioButtonId());
        String selectedOption = selectedRadioButton.getText().toString();
        // Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).show();

        String[] currentQuestion = questionsList.get(currentIndex);
        String questionText = currentQuestion[0];
        String[] options = { currentQuestion[1], currentQuestion[2], currentQuestion[3], currentQuestion[4] };
        String correctAnswer = currentQuestion[5]; // Assuming the answer is in the 6th column (index 5)

        if (selectedOption.equals(correctAnswer)) {
            correct++;
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        } else {
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
        /*
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
        if(radio_g.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
            return;
        }*/
    }
    private void goToPreviousQuestion() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentQuestion();
        } else {
            // Handle first question
            Toast.makeText(getApplicationContext(), "You are at the first Question", Toast.LENGTH_SHORT).show();
            tempV.setText(String.format("prev = %d stQuest = %d flag = %d", prev, stQuest, flag));
        }
    }

    //////////////// CODES FOR TIMER /////////////// SHOULD BE KEPT LATER IN SEPARATE CLASS
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
                Intent intent = new Intent(QuestionsCSV_ReadActivity.this, ResultActivity.class);
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