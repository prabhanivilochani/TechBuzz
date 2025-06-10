package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AcademicNewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_news);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("FoT News");

        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.dropdownmenu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_dev_info) {
                    startActivity(new Intent(this, DeveloperInfoActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_user_info) {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_logout) {
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
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

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_academic);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sports) {
                startActivity(new Intent(this, SportsNewsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_events) {
                startActivity(new Intent(this, EventsNewsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
