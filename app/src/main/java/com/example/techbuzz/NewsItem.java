package com.example.techbuzz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem {
    private String id;
    private String userId;
    private String imageUri;
    private String imageBase64;
    private String title;
    private String description;
    private String category;
    private String createdDate;

    public NewsItem() {}

    public NewsItem(String id, String userId, String imageUri, String imageBase64,
                    String title, String description, String category, String createdDate) {
        this.id = id;
        this.userId = userId;
        this.imageUri = imageUri;
        this.imageBase64 = imageBase64;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdDate = (createdDate != null && !createdDate.isEmpty()) ? createdDate : getCurrentDate();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getImageUri() { return imageUri; }
    public String getImageBase64() { return imageBase64; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getCreatedDate() { return createdDate; }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setCreatedDate(String createdDate) {
        this.createdDate = (createdDate != null && !createdDate.isEmpty()) ? createdDate : getCurrentDate();
    }
}
