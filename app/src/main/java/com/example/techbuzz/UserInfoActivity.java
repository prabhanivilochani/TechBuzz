package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserInfoActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView usernameText, emailText;
    private ImageView userAvatar;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        toolbar = findViewById(R.id.userInfoToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Info");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(v -> finish());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        userAvatar = findViewById(R.id.userAvatar);
        MaterialButton signOutButton = findViewById(R.id.signOutButton);
        editButton = findViewById(R.id.btnEditUser);

        // Set default avatar image (no editing)
        userAvatar.setImageResource(R.drawable.ic_dev_avatar);

        signOutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(UserInfoActivity.this, SignInActivity.class));
            finish();
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserActivity.class);
            startActivity(intent);
        });

        attachUserInfoListener();  // Real-time update listener
    }

    private void attachUserInfoListener() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        DocumentReference docRef = db.collection("users").document(uid);

        // Real-time listener to update UI instantly on data changes
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(UserInfoActivity.this, "Error loading user info", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    String username = snapshot.getString("username");
                    String email = snapshot.getString("email");

                    usernameText.setText(username);
                    emailText.setText(email);

                    // Avatar is fixed, no loading from URL
                }
            }
        });
    }
}
