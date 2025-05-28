package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SportsNewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("FoT News");

        // Set hamburger icon
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
                    // TODO: Add your logout logic here
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;

            });

            popupMenu.show();
        });

        // Inflate right-side icon
        toolbar.inflateMenu(R.menu.dev_info);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_info) {
                startActivity(new Intent(this, DeveloperInfoActivity.class));
                return true;
            }
            return false;
        });

        // Bottom Navigation setup
        bottomNavigationView = findViewById(R.id.bottom_navigation);
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
    }

    // Sample logout function
    private void logoutUser() {
        // Clear session/sharedPreferences here if needed
        Intent intent = new Intent(SportsNewsActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Exit the app
    }
}
