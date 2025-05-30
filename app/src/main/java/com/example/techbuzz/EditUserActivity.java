package com.example.techbuzz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditUserActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        nameEditText = findViewById(R.id.editUsername);
        emailEditText = findViewById(R.id.editEmail);
        saveButton = findViewById(R.id.btnSaveChanges);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserData();

        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                nameEditText.setText(documentSnapshot.getString("username"));
                emailEditText.setText(documentSnapshot.getString("email"));
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
        );
    }

    private void saveUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(uid);

        String updatedName = nameEditText.getText().toString().trim();
        String updatedEmail = emailEditText.getText().toString().trim();

        userRef.update("username", updatedName, "email", updatedEmail)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // return to previous screen
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                );
    }
}
