package com.indrajha.sasquiz;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeveloperActivity extends AppCompatActivity {
    Button btnRestart;

    public void openInsta (View view) {
        String url="https://instagram.com/phytophiletroll";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    public void openRed (View view) {
        String url="https://www.reddit.com/user/IndraMJha";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    public void openYt (View view) {
        String url="https://www.youtube.com/indramjha";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    public void openTg (View view) {
        String url="https://t.me/sasQuiz";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    public void openFb (View view) {
        String url="https://fb.com/IndraMJha";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    public void openBl (View view) {
        String url="https://IndraMJha.blogspot.com";
        Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        btnRestart = findViewById(R.id.button4);
        btnRestart.setOnClickListener(v -> {
            Intent in2 = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in2);
        });
    }
}
