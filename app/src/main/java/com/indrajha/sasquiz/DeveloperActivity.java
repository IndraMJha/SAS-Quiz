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
    Button btnBack, btnChangelog, btnAddSuggestion;
    private DatabaseReference mDatabase;
    private DatabaseReference mSuggestionsDatabase;

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
        
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("changelogs");
        mSuggestionsDatabase = FirebaseDatabase.getInstance().getReference().child("suggestions");

        btnBack = findViewById(R.id.button4);
        btnBack.setOnClickListener(v -> {
            Intent in2 = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in2);
        });

        btnChangelog = findViewById(R.id.btnChangelog);
        btnChangelog.setOnClickListener(v -> {
            showChangelogOptions();
        });
    }

    private void showChangelogOptions() {
        String[] options = {"View Changelog", "Add Suggestion"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changelog & Suggestions");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                fetchChangelog();
            } else {
                showSuggestionDialog();
            }
        });
        builder.show();
    }

    private void showSuggestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Suggestion");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText input = new EditText(this);
        input.setHint("Type your suggestion here...");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.addView(input);

        builder.setView(layout);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String suggestion = input.getText().toString().trim();
            if (!suggestion.isEmpty()) {
                submitSuggestion(suggestion);
            } else {
                Toast.makeText(DeveloperActivity.this, "Suggestion cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void submitSuggestion(String suggestion) {
        String suggestionId = mSuggestionsDatabase.push().getKey();
        if (suggestionId != null) {
            mSuggestionsDatabase.child(suggestionId).setValue(suggestion)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeveloperActivity.this, "Suggestion submitted! Thank you.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeveloperActivity.this, "Failed to submit suggestion.", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void fetchChangelog() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuilder fullLog = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String versionKey = snapshot.getKey();
                        // Replace underscore back to dot for display (e.g., 2_0 -> 2.0)
                        String versionDisplay = versionKey != null ? versionKey.replace("_", ".") : "Unknown";
                        String log = snapshot.getValue(String.class);
                        fullLog.append("Version ").append(versionDisplay).append(":\n").append(log).append("\n\n");
                    }
                    showScrollableDialog("Changelog", fullLog.toString().trim());
                } else {
                    Toast.makeText(DeveloperActivity.this, "No changelogs found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DeveloperActivity.this, "Failed to load: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showScrollableDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
               .setMessage(message)
               .setPositiveButton("OK", null)
               .show();
    }
}