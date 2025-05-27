package com.example.techbuzz;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password_dialog, container, false);

        TextInputEditText emailInput = view.findViewById(R.id.emailField);
        MaterialButton sendBtn = view.findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (!email.isEmpty()) {
                // Perform your forgot password logic here
                // For example, Firebase password reset:
                // FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                //     .addOnCompleteListener(task -> {
                //         if (task.isSuccessful()) {
                //             showSuccessMessage();
                //             dismiss();
                //         } else {
                //             showErrorMessage();
                //         }
                //     });

                // For now, just show success message
                showSuccessMessage();
                dismiss();
            } else {
                emailInput.setError("Email required");
            }
        });

        return view;
    }

    private void showSuccessMessage() {
        // Option 1: Using Snackbar (recommended)
        if (getActivity() != null) {
            View rootView = getActivity().findViewById(android.R.id.content);
            Snackbar.make(rootView, "Password reset email sent successfully!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.success_green))
                    .setTextColor(Color.WHITE)
                    .show();
        }

        // Option 2: Using Toast (simpler)
        // Toast.makeText(getContext(), "Password reset email sent successfully!", Toast.LENGTH_LONG).show();
    }

    private void showErrorMessage() {
        if (getActivity() != null) {
            View rootView = getActivity().findViewById(android.R.id.content);
            Snackbar.make(rootView, "Failed to send reset email. Please try again.", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.error_red))
                    .setTextColor(Color.WHITE)
                    .show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}