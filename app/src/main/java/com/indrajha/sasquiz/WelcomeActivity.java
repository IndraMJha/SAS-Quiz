package com.indrajha.sasquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        final Button start1Button = findViewById(R.id.btnStart1);
        final ImageView userPic = findViewById(R.id.picUser);
        final TextView paper = findViewById(R.id.selPaper);

        Intent intent = getIntent();
        //////get/display user info
        String name = intent.getStringExtra("myName");
        String aNum = intent.getStringExtra("aNo");

        final TextView userView = findViewById(R.id.textUser);
        userView.setText(name + " / " + aNum);

        //////get/display user info
        if (name.trim().equals("")) {
            //textView.setText("Hello User");
            Toast.makeText(getApplicationContext(), "Hello " + "\uD83D\uDE42", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("indra")) | name.trim().equalsIgnoreCase(("mohan"))) {
            // textView.setText("Welcome Boss \uD83D\uDE08");
            Toast.makeText(getApplicationContext(), "Welcome Boss \uD83D\uDE08", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("neha")) | name.trim().equalsIgnoreCase("nehu")) {
            //textView.setText("Hiii Nehu \uD83D\uDE0D");
            Picasso.get().load(R.drawable.model).into(userPic);
            Toast.makeText(getApplicationContext(), "Hiii Nehu \uD83D\uDE0D", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("suman")) | name.trim().equalsIgnoreCase("suku")) {
            //textView.setText("Hii Suku \uD83D\uDE0D");
            Toast.makeText(getApplicationContext(), "Hii SuKu \uD83E\uDD70", Toast.LENGTH_SHORT).show();
        } else if (name.trim().equalsIgnoreCase(("pushpa"))) {
            //textView.setText("Hi Pushpa \uD83D\uDC90");
            Toast.makeText(getApplicationContext(), "Hi Pushpa \uD83D\uDC90", Toast.LENGTH_SHORT).show();
        } else {
            //textView.setText("Hello " + name);
            Toast.makeText(getApplicationContext(), "Hello " + name + "\uD83D\uDE42", Toast.LENGTH_SHORT).show();
        }

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        Spinner spinner3 = findViewById(R.id.spinner3);

        String[] part1List = new String[]{"Paper 1 SET1", "Paper 1 SET2", "Paper 1 SET3", "Paper 1 SET4",
                "PAPER2 SET1", "PAPER2 SET2", "PAPER2 SET3", "PAPER2 SET4",
                "PAPER2 SET5", "PAPER2 SET6", "PAPER2 SET7", "PAPER2 SET8", "PAPER2 SET9", "PAPER2 SET10",
                "Paper 2 SET 11", "Paper 2 SET 12", "Paper 2 SET 13"
        };
        String[] p1List = new String[]{"Accounts Code", "AT Act", "Audit Code", "C & AG", "CCS CCA", "CCS Conduct Rules",
                "Constitution of India", "DSE", "FR 1", "FR2", "FRBM Act", "GST", "IGAA", "NPS", "OM !", "OM 2", "RTI Act", "SH Act"
        };
        String[] part2List = new String[]{"OM IX - Set 1", "OM IX - Set 2", "OM IX - Set 3"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, part1List);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, p1List);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, part2List);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);


        final String[] selectedPart = new String[1];
        final String[] selectedPaper = new String[1];

        /////////// PART 1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                start1Button.setEnabled(position >= 0);
                selectedPart[0] = "Part 1";
                selectedPaper[0] = adapterView.getItemAtPosition(position).toString();
                paper.setText("Selected Paper: " + selectedPaper[0]);
                //create an intent and pass the selected course to the new activity
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "SELECT PAPER", Toast.LENGTH_SHORT).show();
                start1Button.setEnabled(false);
            }
        });

        /////////// PART 1 Paper wise and .CSV format
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                start1Button.setEnabled(position >= 0);
                selectedPart[0] = "Part 1 : Book wise";
                selectedPaper[0] = adapterView.getItemAtPosition(position).toString();
                paper.setText("Selected Paper: " + selectedPaper[0]);
                //create an intent and pass the selected course to the new activity
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "SELECT PAPER", Toast.LENGTH_SHORT).show();
            }
        });
        //////////// PART 2
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                start1Button.setEnabled(position >= 0);
                selectedPart[0] = "Part 2";
                selectedPaper[0] = adapterView.getItemAtPosition(position).toString();
                paper.setText("Selected Paper: " + selectedPaper[0]);
                //create an intent and pass the selected course to the new activity
                //String selectedPaper = paper.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "SELECT PAPER", Toast.LENGTH_SHORT).show();
                start1Button.setEnabled(false);
            }
        });

        start1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String selectedPaper = paper.getText().toString();
                Intent intent1;
                if (selectedPart[0].equals("Part 1 : Book wise")) {
                    intent1 = new Intent(getApplicationContext(), QuestionsCSV_ReadActivity.class);
                }
                else {
                    intent1 = new Intent(getApplicationContext(), QuestionsActivity.class);
                }
                intent1.putExtra("paper", selectedPaper[0]);
                intent1.putExtra("part", selectedPart[0]);
                startActivity(intent1);
            }
        });
    }
}
