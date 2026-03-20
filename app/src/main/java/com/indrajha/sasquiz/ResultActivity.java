package com.indrajha.sasquiz;

import static com.indrajha.sasquiz.QuestionsActivity.timeRemainingInMillis;
import static com.indrajha.sasquiz.QuestionsActivity.totalTimeInMillis;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5;
    Button btnRestart, buttonWel;
    //public static String time_taken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv1 = findViewById(R.id.tvres);
        tv2 = findViewById(R.id.tvres2);
        tv3 = findViewById(R.id.tvres3);
        tv4 = findViewById(R.id.result);
        tv5 = findViewById(R.id.timer);

        btnRestart = findViewById(R.id.btnRestart);
        buttonWel = findViewById(R.id.buttonWelcome);

        StringBuffer sb1 = new StringBuffer();
        sb1.append("Correct answers: ").append(QuestionsActivity.correct).append("\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Wrong Answers: ").append(QuestionsActivity.wrong).append("\n");
        StringBuffer sb3 = new StringBuffer();
        sb3.append("Final Score: ").append(QuestionsActivity.correct - (QuestionsActivity.wrong / 4)).append("\n");

        StringBuilder sb4 = new StringBuilder();
        sb4.append("Result of ").append(QuestionsActivity.paper_name);

        StringBuilder sb5 = new StringBuilder();
        //Intent intent = getIntent();
        //time_taken= String.valueOf((totalTimeInMillis-timeRemainingInMillis)/60000);
        int minutes = (int) (totalTimeInMillis - timeRemainingInMillis / 1000) / 60;
        int seconds = (int) (totalTimeInMillis - timeRemainingInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        //timerTextView.setText(timeFormatted);
        sb5.append("Total time taken : ").append(timeFormatted).append(" minutes and seconds");

        tv1.setText(sb1);
        tv2.setText(sb2);
        tv3.setText(sb3);
        tv4.setText(sb4);
        tv5.setText(sb5);

        QuestionsActivity.correct=0;
        QuestionsActivity.wrong=0;

        Toast.makeText(getApplicationContext(), "Thanks for using this app", Toast.LENGTH_SHORT).show();

        btnRestart.setOnClickListener(v -> {
            finish();
            //Intent in = new Intent(getApplicationContext(),MainActivity.class);
            Intent in = new Intent(getApplicationContext(),QuestionsActivity.class);
            startActivity(in);
        });
        buttonWel.setOnClickListener(v -> finishAffinity());
    }
}
