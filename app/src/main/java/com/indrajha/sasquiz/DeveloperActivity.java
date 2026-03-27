package com.indrajha.sasquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeveloperActivity extends AppCompatActivity {
    Button btnBack;

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
        btnBack = findViewById(R.id.button4);
        btnBack.setOnClickListener(v -> {
            Intent in2 = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in2);
        });
    }

}