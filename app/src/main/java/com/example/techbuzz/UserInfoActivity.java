package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView usernameText, emailText;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Initialize Toolbar and set it as the support action bar
        toolbar = findViewById(R.id.userInfoToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Info");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set navigation icon color to white safely
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }

        // Set toolbar title text color to white
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        // Navigation click finishes the activity
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);

        MaterialButton signOutButton = findViewById(R.id.signOutButton);
        Button editButton = findViewById(R.id.btnEditUser);

        // Fetch current user info from Firestore and display
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(uid);

            docRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String username = snapshot.getString("username");
                    String email = snapshot.getString("email");

                    usernameText.setText(username);
                    emailText.setText(email);
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to fetch user info", Toast.LENGTH_SHORT).show()
            );
        }

        // Sign out button listener
        signOutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(UserInfoActivity.this, SignInActivity.class));
            finish();
        });

        // Edit info button listener
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserActivity.class);
            startActivity(intent);
        });
    }
}
