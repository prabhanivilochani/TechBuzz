package com.example.techbuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private MaterialButton signInButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find Views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInButton = findViewById(R.id.signInButton);
        TextView signUpText = findViewById(R.id.signUpText);
        TextView forgotPassword = findViewById(R.id.forgetpw);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In...");

        // Sign In logic
        signInButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!isValidInput(email, password)) return;

            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, SportsNewsActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Sign Up navigation
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);

            startActivity(intent);
            finish(); // optional but recommended

        });

        // Forgot Password Dialog
        forgotPassword.setOnClickListener(v -> {
            ForgotPasswordDialogFragment dialog = new ForgotPasswordDialogFragment();
            dialog.show(getSupportFragmentManager(), "ForgotPasswordDialog");
        });
    }

    private boolean isValidInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return false;
        }
        if (password.length() < 8) {
            passwordInput.setError("Enter valid password");
            return false;
        }

        return true;
    }
}
