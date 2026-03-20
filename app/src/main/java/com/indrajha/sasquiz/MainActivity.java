package com.indrajha.sasquiz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        final Button startButton = findViewById(R.id.buttonLogin);
        Button aboutButton = findViewById(R.id.buttonAbout);
        Button quitButton = findViewById(R.id.buttonQuit);

        final EditText uName = findViewById(R.id.editName);
        final EditText accountNo    = findViewById(R.id.actNo);

        startButton.setOnClickListener(v -> {
            String name = uName.getText().toString().trim();
            String aNum = accountNo.getText().toString().trim();

            if (name.isEmpty() || aNum.isEmpty()) {
                Toast.makeText(this, "Please enter both name and account number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Map user input to Firebase login.
            String email = name.toLowerCase() + "@sasquiz.com";
            
            mAuth.signInWithEmailAndPassword(email, aNum)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        intent.putExtra("myName", name);
                        intent.putExtra("aNo", aNum);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        });

        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
            startActivity(intent);
        });

        quitButton.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}