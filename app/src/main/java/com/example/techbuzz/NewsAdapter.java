package com.example.techbuzz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Activity activity;
    private List<NewsItem> newsList;

    public NewsAdapter(Activity activity, List<NewsItem> newsList) {
        this.activity = activity;
        this.newsList = newsList;
    }

    public void setNewsList(List<NewsItem> updatedList) {
        this.newsList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);

        holder.newsTitle.setText(newsItem.getTitle());
        holder.newsDescription.setText(newsItem.getDescription());
        holder.newsDate.setText("Created: " + newsItem.getCreatedDate());

        if (newsItem.getImageBase64() != null && !newsItem.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.decode(newsItem.getImageBase64(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.newsImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                holder.newsImage.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.newsImage.setImageResource(R.drawable.placeholder);
        }

        holder.buttonEdit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && pos < newsList.size()) {
                NewsItem itemToEdit = newsList.get(pos);
                if (itemToEdit.getId() == null || itemToEdit.getId().isEmpty()) {
                    Toast.makeText(activity, "Invalid news ID for edit", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(activity, ManageNewsActivity.class);
                intent.putExtra("newsId", itemToEdit.getId());

                activity.startActivity(intent);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION || pos >= newsList.size()) {
                Toast.makeText(activity, "Delete failed: Invalid position", Toast.LENGTH_SHORT).show();
                return;
            }

            NewsItem itemToDelete = newsList.get(pos);
            if (itemToDelete.getId() == null || itemToDelete.getId().isEmpty()) {
                Toast.makeText(activity, "Delete failed: Missing document ID", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore.getInstance()
                    .collection("news")
                    .document(itemToDelete.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        newsList.remove(pos);
                        notifyItemRemoved(pos);
                        Toast.makeText(activity, "News deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("NewsAdapter", "Delete failed", e);
                        Toast.makeText(activity, "Delete error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle, newsDescription, newsDate;
        ImageButton buttonEdit, buttonDelete;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsDate = itemView.findViewById(R.id.newsDate);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
