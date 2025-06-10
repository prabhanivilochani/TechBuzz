package com.example.techbuzz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ManageNewsActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editTextTitle, editTextDescription;
    private Button buttonSelectImage, buttonSubmit;
    private Uri selectedImageUri;
    private String category = "";
    private String newsId = null;
    private boolean isEditing = false;


    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);

        Toolbar toolbar = findViewById(R.id.toolbarManageNews);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageViewPreview);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        category = getIntent().getStringExtra("category");
        if (category == null) category = "sports";

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imageView.setImageURI(selectedImageUri);
                    }
                });

        buttonSelectImage.setOnClickListener(v -> openImagePicker());
        buttonSubmit.setOnClickListener(v -> addNewsCard());

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        if (category == null) category = "sports";

        newsId = intent.getStringExtra("newsId");
        if (newsId != null) {
            isEditing = true;
            // Prefill fields for editing
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextDescription.setText(intent.getStringExtra("description"));
            String imageUriStr = intent.getStringExtra("imageUri");
            if (imageUriStr != null) {
                selectedImageUri = Uri.parse(imageUriStr);
                imageView.setImageURI(selectedImageUri);
            }
            buttonSubmit.setText("Update News");
        }

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void addNewsCard() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (selectedImageUri == null || title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "guest";

        String docId = isEditing ? newsId : String.valueOf(System.currentTimeMillis());

        NewsItem newsItem = new NewsItem(docId, userId, selectedImageUri.toString(), title, description, category);

        FirebaseFirestore.getInstance()
                .collection("news")
                .document(docId)
                .set(newsItem)
                .addOnSuccessListener(unused -> {
                    String msg = isEditing ? "News updated successfully!" : "News added successfully!";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    if (!isEditing) {
                        imageView.setImageResource(R.drawable.placeholder);
                        editTextTitle.setText("");
                        editTextDescription.setText("");
                        selectedImageUri = null;
                    }
                    finish(); // close activity after update/add
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save news: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
