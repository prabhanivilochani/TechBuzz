package com.example.techbuzz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ManageNewsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ManageNewsActivity";

    private ImageView newsImageView;
    private EditText titleEditText, descriptionEditText;
    private Button uploadButton;

    private Uri selectedImageUri;
    private String encodedImage = null;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isEditMode = false;
    private String editNewsId;
    private String category = "general";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);

        newsImageView = findViewById(R.id.newsImageView);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        uploadButton = findViewById(R.id.uploadButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("category") != null ? intent.getStringExtra("category") : "general";

            if (intent.hasExtra("newsId")) {
                isEditMode = true;
                editNewsId = intent.getStringExtra("newsId");
                loadNewsDetails(editNewsId); // Safe loading from Firestore
            }
        }

        newsImageView.setOnClickListener(v -> openImageChooser());

        uploadButton.setOnClickListener(v -> {
            if (isEditMode) {
                updateNews();
            } else {
                addNews();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select News Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            newsImageView.setImageURI(selectedImageUri);
            encodeImageToBase64();
        }
    }

    private void encodeImageToBase64() {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Error encoding image", e);
            Toast.makeText(this, "Failed to encode image", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNews() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || encodedImage == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        String newsId = db.collection("news").document().getId();

        Map<String, Object> newsData = new HashMap<>();
        newsData.put("userId", userId);
        newsData.put("title", title);
        newsData.put("description", description);
        newsData.put("imageBase64", encodedImage);
        newsData.put("category", category);
        newsData.put("timestamp", System.currentTimeMillis());

        db.collection("news").document(newsId).set(newsData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "News uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Upload failed", e);
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadNewsDetails(String newsId) {
        db.collection("news").document(newsId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String desc = documentSnapshot.getString("description");
                        String image = documentSnapshot.getString("imageBase64");

                        titleEditText.setText(title);
                        descriptionEditText.setText(desc);

                        if (image != null && !image.isEmpty()) {
                            byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            newsImageView.setImageBitmap(bitmap);
                            encodedImage = image;
                        }

                        uploadButton.setText("Update News");
                    } else {
                        Toast.makeText(this, "News not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load news: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void updateNews() {
        String title = titleEditText.getText().toString().trim();
        String desc = descriptionEditText.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editNewsId == null || editNewsId.isEmpty()) {
            Toast.makeText(this, "Error: missing news ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", desc);
        if (encodedImage != null) {
            data.put("imageBase64", encodedImage);
        }

        db.collection("news").document(editNewsId).update(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "News updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Update failed", e);
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                });
    }
}
