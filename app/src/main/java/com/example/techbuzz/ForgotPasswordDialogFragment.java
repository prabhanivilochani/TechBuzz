package com.example.techbuzz;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password_dialog, container, false);

        TextInputEditText emailInput = view.findViewById(R.id.emailField);
        MaterialButton sendBtn = view.findViewById(R.id.sendButton);
        Chip successChip = view.findViewById(R.id.successChip);

        sendBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                emailInput.setError("Email required");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter valid email");
                return;
            }

            sendBtn.setEnabled(false);
            sendBtn.setText("Sending...");

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            successChip.setVisibility(View.VISIBLE);

                            successChip.setText("Reset email sent to " + email);

                            new Handler().postDelayed(this::dismiss, 2000);
                        } else {
                            sendBtn.setEnabled(true);
                            sendBtn.setText("Send Link");
                            emailInput.setError("Failed to send reset email. Try again.");
                        }
                    });
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Make dialog wrap content properly
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}