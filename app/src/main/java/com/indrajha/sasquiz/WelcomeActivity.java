package com.indrajha.sasquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private String selectedPart = "Part 1";
    private String selectedPaper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        final Button start1Button = findViewById(R.id.btnStart1);
        final ImageView userPic = findViewById(R.id.picUser);
        final TextView paper = findViewById(R.id.selPaper);
        final Button signOutButton = findViewById(R.id.btnSignOut);

        // Configure Google Sign-In (needed for sign out)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Intent logoutIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
            });
        });

        Intent intent = getIntent();
        //////get/display user info
        String name = intent.getStringExtra("myName");
        String aNum = intent.getStringExtra("aNo");

        final TextView userView = findViewById(R.id.textUser);
        userView.setText(name + " / " + aNum);

        //////get/display user info
        if (name.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Hello " + "\uD83D\uDE42", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("indra")) | name.trim().equalsIgnoreCase(("mohan"))) {
            Toast.makeText(getApplicationContext(), "Welcome Boss \uD83D\uDE08", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("neha")) | name.trim().equalsIgnoreCase("nehu")) {
            Picasso.get().load(R.drawable.model).into(userPic);
            Toast.makeText(getApplicationContext(), "Hiii Nehu \uD83D\uDE0D", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("suman")) | name.trim().equalsIgnoreCase("suku")) {
            Toast.makeText(getApplicationContext(), "Hii SuKu \uD83E\uDD70", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("pushpa"))) {
            Toast.makeText(getApplicationContext(), "Hi Pushpa \uD83D\uDC90", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Hello " + name + "\uD83D\uDE42", Toast.LENGTH_SHORT).show();
        }

        // Spinners
        final Spinner spinner1 = findViewById(R.id.spinner1);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        final Spinner spinner3 = findViewById(R.id.spinner3);
        final Spinner spinnerLatest = findViewById(R.id.spinnerLatest);

        // Radio Buttons
        final RadioButton rbPart1 = findViewById(R.id.rbPart1);
        final RadioButton rbBookWise = findViewById(R.id.rbBookWise);
        final RadioButton rbPart2 = findViewById(R.id.rbPart2);
        final RadioButton rbLatest = findViewById(R.id.rbLatest);

        // Data arrays
        String[] part1List = new String[]{"Paper 1 SET1", "Paper 1 SET2", "Paper 1 SET3", "Paper 1 SET4",
                "PAPER2 SET1", "PAPER2 SET2", "PAPER2 SET3", "PAPER2 SET4",
                "PAPER2 SET5", "PAPER2 SET6", "PAPER2 SET7", "PAPER2 SET8", "PAPER2 SET9", "PAPER2 SET10",
                "Paper 2 SET 11", "Paper 2 SET 12", "Paper 2 SET 13"
        };
        String[] p1List = new String[]{"Accounts Code", "AT Act", "Audit Code", "C & AG", "CCS CCA", "CCS Conduct Rules",
                "Constitution of India", "DSE", "FR 1", "FR2", "FRBM Act", "GST", "IGAA", "NPS", "OM !", "OM 2", "RTI Act", "SH Act"
        };
        String[] part2List = new String[]{"OM IX - Set 1", "OM IX - Set 2", "OM IX - Set 3"};

        // Set up adapters
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, part1List);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, p1List);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, part2List);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        // Dynamically fetch Latest Questions node names from Firebase
        final ArrayList<String> latestList = new ArrayList<>();
        latestList.add("Loading...");
        final ArrayAdapter<String> adapterLatest = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, latestList);
        adapterLatest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLatest.setAdapter(adapterLatest);

        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference("questions");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latestList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    latestList.add(child.getKey());
                }
                if (latestList.isEmpty()) {
                    latestList.add("No sets available");
                }
                adapterLatest.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latestList.clear();
                latestList.add("Error loading sets");
                adapterLatest.notifyDataSetChanged();
            }
        });

        // Set initial selection from default checked radio (Part 1)
        selectedPart = "Part 1";
        selectedPaper = part1List[0];
        paper.setText("Selected Paper: " + selectedPaper);

        // Helper to enable/disable spinners based on active radio
        CompoundButton.OnCheckedChangeListener radioListener = (buttonView, isChecked) -> {
            if (!isChecked) return;

            // Uncheck the other radio buttons manually (since they're not in a RadioGroup)
            if (buttonView == rbPart1) {
                rbBookWise.setChecked(false);
                rbPart2.setChecked(false);
                rbLatest.setChecked(false);
            } else if (buttonView == rbBookWise) {
                rbPart1.setChecked(false);
                rbPart2.setChecked(false);
                rbLatest.setChecked(false);
            } else if (buttonView == rbPart2) {
                rbPart1.setChecked(false);
                rbBookWise.setChecked(false);
                rbLatest.setChecked(false);
            } else if (buttonView == rbLatest) {
                rbPart1.setChecked(false);
                rbBookWise.setChecked(false);
                rbPart2.setChecked(false);
            }

            // Enable/disable spinners
            spinner1.setEnabled(rbPart1.isChecked());
            spinner1.setAlpha(rbPart1.isChecked() ? 1.0f : 0.5f);

            spinner2.setEnabled(rbBookWise.isChecked());
            spinner2.setAlpha(rbBookWise.isChecked() ? 1.0f : 0.5f);

            spinner3.setEnabled(rbPart2.isChecked());
            spinner3.setAlpha(rbPart2.isChecked() ? 1.0f : 0.5f);

            spinnerLatest.setEnabled(rbLatest.isChecked());
            spinnerLatest.setAlpha(rbLatest.isChecked() ? 1.0f : 0.5f);

            // Update selectedPart and selectedPaper
            if (rbPart1.isChecked()) {
                selectedPart = "Part 1";
                selectedPaper = spinner1.getSelectedItem().toString();
            } else if (rbBookWise.isChecked()) {
                selectedPart = "Part 1 : Book wise";
                selectedPaper = spinner2.getSelectedItem().toString();
            } else if (rbPart2.isChecked()) {
                selectedPart = "Part 2";
                selectedPaper = spinner3.getSelectedItem().toString();
            } else if (rbLatest.isChecked()) {
                selectedPart = "Latest Questions";
                selectedPaper = spinnerLatest.getSelectedItem().toString();
            }
            paper.setText("Selected Paper: " + selectedPaper);
        };

        rbPart1.setOnCheckedChangeListener(radioListener);
        rbBookWise.setOnCheckedChangeListener(radioListener);
        rbPart2.setOnCheckedChangeListener(radioListener);
        rbLatest.setOnCheckedChangeListener(radioListener);

        // Spinner listeners – only update selectedPaper if this category is active
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (rbPart1.isChecked()) {
                    selectedPart = "Part 1";
                    selectedPaper = adapterView.getItemAtPosition(position).toString();
                    paper.setText("Selected Paper: " + selectedPaper);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (rbBookWise.isChecked()) {
                    selectedPart = "Part 1 : Book wise";
                    selectedPaper = adapterView.getItemAtPosition(position).toString();
                    paper.setText("Selected Paper: " + selectedPaper);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (rbPart2.isChecked()) {
                    selectedPart = "Part 2";
                    selectedPaper = adapterView.getItemAtPosition(position).toString();
                    paper.setText("Selected Paper: " + selectedPaper);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerLatest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (rbLatest.isChecked()) {
                    selectedPart = "Latest Questions";
                    selectedPaper = adapterView.getItemAtPosition(position).toString();
                    paper.setText("Selected Paper: " + selectedPaper);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        start1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1;
                if ((selectedPart.equals("Part 1 : Book wise") && !selectedPaper.equals("Accounts Code")) || selectedPart.equals("Latest Questions")) {
                    intent1 = new Intent(getApplicationContext(), QuestionsCSV_ReadActivity.class);
                }
                else {
                    intent1 = new Intent(getApplicationContext(), QuestionsActivity.class);
                }
                intent1.putExtra("paper", selectedPaper);
                intent1.putExtra("part", selectedPart);
                startActivity(intent1);
            }
        });
    }
}
