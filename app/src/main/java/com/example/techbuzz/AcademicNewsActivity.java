package com.example.techbuzz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FoT News");
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_academic);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_sports) {
                startActivity(new Intent(AcademicNewsActivity.this, SportsNewsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_events) {
                startActivity(new Intent(AcademicNewsActivity.this, EventsNewsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }
}
