package com.example.techbuzz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem {
    private String id;
    private String userId;
    private String imageUri;
    private String title;
    private String description;
    private String category;
    private String createdDate;

    public NewsItem() {} // Required for Firebase

    // Constructor with auto date
    public NewsItem(String id, String userId, String imageUri, String title, String description, String category) {
        this.id = id;
        this.userId = userId;
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdDate = getCurrentDate(); // ← Auto-generated date
    }

    // Optional: Constructor for manual date (for Firebase compatibility)
    public NewsItem(String id, String userId, String imageUri, String title, String description, String category, String createdDate) {
        this.id = id;
        this.userId = userId;
        this.imageUri = imageUri;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdDate = createdDate;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getImageUri() { return imageUri; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getCreatedDate() { return createdDate; }
}
