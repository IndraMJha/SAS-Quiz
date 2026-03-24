package com.indrajha.sasquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView statusText;

    private final ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Log.w(TAG, "Google sign in failed", e);
                        statusText.setText("Google Sign-In failed: " + e.getStatusCode());
                    }
                } else {
                    Log.w(TAG, "Google sign in canceled or failed. Result code: " + result.getResultCode());
                    statusText.setText("Google Sign-In canceled or failed.");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        statusText = findViewById(R.id.textStatus);

        // Configure Google Sign-In
        String webClientId = getString(R.string.default_web_client_id);
        Log.d(TAG, "Using Web Client ID: " + webClientId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton googleSignInButton = findViewById(R.id.buttonGoogleSignIn);
        Button aboutButton = findViewById(R.id.buttonAbout);
        Button quitButton = findViewById(R.id.buttonQuit);

        googleSignInButton.setOnClickListener(v -> signIn());

        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
            startActivity(intent);
        });

        quitButton.setOnClickListener(v -> finishAffinity());
    }

    private void signIn() {
        statusText.setText("Signing in...");
        // Sign out first to always show the account chooser
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        statusText.setText("Authenticating...");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.getEmail() != null) {
                            checkWhitelist(user);
                        } else {
                            statusText.setText("Sign in failed: Could not get user info.");
                        }
                    } else {
                        statusText.setText("Firebase authentication failed.");
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void checkWhitelist(FirebaseUser user) {
        statusText.setText("Checking access...");
        String email = user.getEmail();
        // Sanitize email for Firebase key: replace @ and . with _
        String sanitizedEmail = email.replace("@", "_").replace(".", "_");

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("allowed_users")
                .child(sanitizedEmail);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is whitelisted — proceed to WelcomeActivity
                    String displayName = user.getDisplayName();
                    if (displayName == null || displayName.isEmpty()) {
                        displayName = email;
                    }
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.putExtra("myName", displayName);
                    intent.putExtra("aNo", email);
                    startActivity(intent);
                    statusText.setText("Sign in with your Google account");
                } else {
                    // Not whitelisted — deny access
                    mAuth.signOut();
                    mGoogleSignInClient.signOut();
                    statusText.setText("Access denied. Your account is not authorized.");
                    Toast.makeText(MainActivity.this,
                            "Access denied. Contact admin to get your account (" + email + ") added.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                statusText.setText("Error checking access. Try again.");
                Log.w(TAG, "checkWhitelist:onCancelled", databaseError.toException());
            }
        });
    }
}