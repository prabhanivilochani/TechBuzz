package com.example.techbuzz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsItem> newsList;
    private String currentUserId;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "guest";
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, descriptionView,dateView;


        ImageView buttonEdit, buttonDelete;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.newsImage);
            titleView = itemView.findViewById(R.id.newsTitle);
            descriptionView = itemView.findViewById(R.id.newsDescription);
            dateView = itemView.findViewById(R.id.newsDate);

            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

        }
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
        holder.titleView.setText(item.getTitle());
        holder.descriptionView.setText(item.getDescription());

        Glide.with(context)
                .load(item.getImageUri())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        // Show Edit/Delete buttons only if current user owns the news
        boolean isOwner = currentUserId.equals(item.getUserId());
        holder.buttonEdit.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        holder.buttonDelete.setVisibility(isOwner ? View.VISIBLE : View.GONE);

        holder.buttonEdit.setOnClickListener(v -> {
            // Open ManageNewsActivity to edit the news
            Intent intent = new Intent(context, ManageNewsActivity.class);
            intent.putExtra("category", item.getCategory());
            intent.putExtra("newsId", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("imageUri", item.getImageUri());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            // Delete the news item from Firestore
            FirebaseFirestore.getInstance().collection("news")
                    .document(item.getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "News deleted successfully", Toast.LENGTH_SHORT).show();
                        newsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, newsList.size());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to delete news: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
