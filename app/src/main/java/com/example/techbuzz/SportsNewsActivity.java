package com.example.techbuzz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SportsNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sports News");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        toolbar.setTitleTextColor(Color.BLACK);

        // Set left nav icon (hamburger/menu icon)
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.menu);
        if (navIcon != null) {
            navIcon.mutate().setTint(Color.BLACK);
            toolbar.setNavigationIcon(navIcon);
        }

        // Show popup menu when navigation icon clicked
        toolbar.setNavigationOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.dropdownmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_dev_info) {
                    startActivity(new Intent(this, DeveloperInfoActivity.class));
                    return true;

                } else if (id == R.id.menu_user_info) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    return true;
                } else if (id == R.id.menu_logout) {
                    FirebaseAuth.getInstance().signOut(); // Log out from Firebase

                    Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show(); // Success message

                    // Navigate to SignInActivity and clear back stack
                    Intent intent = new Intent(SportsNewsActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    return true;
                }
                return false;
            });

            popup.show();
        });
    }

    // Inflate app bar menu (right side icon)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dev_info, menu);

        // Optional: tint icon black so it’s visible on white toolbar
        MenuItem devInfo = menu.findItem(R.id.menu_dev_info);
        if (devInfo != null && devInfo.getIcon() != null) {
            devInfo.getIcon().setTint(Color.BLACK);
        }
        return true;
    }

    // Handle clicks on app bar menu icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_dev_info) {
            // Navigate to DeveloperInfoActivity
            startActivity(new Intent(this, DeveloperInfoActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
