package com.example.techbuzz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public void setNewsList(List<NewsItem> updatedList) {
        this.newsList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);

        holder.newsTitle.setText(item.getTitle());
        holder.newsDescription.setText(item.getDescription());
        holder.newsDate.setText("Created: " + item.getCreatedDate());

        if (item.getImageBase64() != null && !item.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.decode(item.getImageBase64(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.newsImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                holder.newsImage.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.newsImage.setImageResource(R.drawable.placeholder);
        }

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManageNewsActivity.class);
            intent.putExtra("newsId", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("category", item.getCategory());
            intent.putExtra("imageBase64", item.getImageBase64());
            context.startActivity(intent);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            String documentId = item.getId();
            FirebaseFirestore.getInstance().collection("news")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        newsList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "News deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
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
        CardView newsCard;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsCard = itemView.findViewById(R.id.newsCard);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsDate = itemView.findViewById(R.id.newsDate);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
