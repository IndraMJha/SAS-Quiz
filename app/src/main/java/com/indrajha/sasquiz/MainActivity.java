package com.indrajha.sasquiz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = findViewById(R.id.buttonLogin);
        Button aboutButton = findViewById(R.id.buttonAbout);
        Button quitButton = findViewById(R.id.buttonQuit);

        final EditText uName = findViewById(R.id.editName);
        final EditText accountNo    = findViewById(R.id.actNo);

        startButton.setOnClickListener(v -> {
            String name = uName.getText().toString();
            String aNum = accountNo.getText().toString();

            boolean isValidUser = false;

            String[] validUsers     = {"indra","neha","pushpa","suman","sushil","friend","umesh", "mantosh", "pappuram", "sanjiv","amar", "sumit" , "dinanath","sucharita", "santosh","ramesh","rustam","navdeep","manab","bhushan","sanchaita"};
            String[] validIDs       = {"7777","8343818","8343817","8343417","8340249","1234567","8351179","8347491","8351347","8342498","8342366","8349138","8342499","8341372","8347406","8347838","8349107","8350116","8339982","8307591","8348665"};

            for (int i=0; i< validUsers.length;i++){
                if(name.trim().toLowerCase().equals(validUsers[i]) && aNum.equals(validIDs[i])){
                    isValidUser=true;
                    break;
                }
            }
            if (isValidUser){
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.putExtra("myName", name);
                intent.putExtra("aNo", aNum);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.invalid_user)+ " / " +getString(R.string.invalid_pass), Toast.LENGTH_SHORT).show();
            }
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
