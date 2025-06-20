package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SportsNewsActivity extends AppCompatActivity {

    private ArrayList<NewsItem> sportsNewsList = new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView recyclerViewSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("FoT News");

        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.dropdownmenu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_dev_info) {
                    startActivity(new Intent(this, DeveloperInfoActivity.class));
                    return true;
                } else if (id == R.id.menu_user_info) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    return true;
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                    logoutUser();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
        toolbar.inflateMenu(R.menu.dev_info);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_info) {
                startActivity(new Intent(this, DeveloperInfoActivity.class));
                return true;
            }
            return false;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_sports);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_academic) {
                startActivity(new Intent(this, AcademicNewsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_events) {
                startActivity(new Intent(this, EventsNewsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });

        recyclerViewSports = findViewById(R.id.recyclerViewSports);
        recyclerViewSports.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddNews = findViewById(R.id.fab_add_news);
        fabAddNews.setOnClickListener(v -> {
            Intent intent = new Intent(SportsNewsActivity.this, ManageNewsActivity.class);
            intent.putExtra("category", "sports");
            startActivity(intent);
        });

        loadNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNews();
    }

    private void loadNews() {
        FirebaseFirestore.getInstance().collection("news")
                .whereEqualTo("category", "sports")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    sportsNewsList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        NewsItem item = doc.toObject(NewsItem.class);
                        if (item != null) {
                            item.setId(doc.getId());
                            sportsNewsList.add(item);
                        }
                    }
                    adapter = new NewsAdapter(SportsNewsActivity.this, sportsNewsList);
                    recyclerViewSports.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void logoutUser() {
        Intent intent = new Intent(SportsNewsActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
