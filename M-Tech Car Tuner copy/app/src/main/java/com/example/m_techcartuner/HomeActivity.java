package com.example.m_techcartuner;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // create this layout next

        // Firebase Authentication check
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupButtons();
        setupBottomNavigation();
    }

    private void setupButtons() {
        // Profile button at top-right
        findViewById(R.id.buttonProfile).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        // Add button to navigate to TuneCarActivity
        findViewById(R.id.buttonTuneCar).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TuneCarActivity.class));
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);

        // Set the selected item to "Home" because this is the home page
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_tune) {
                startActivity(new Intent(HomeActivity.this, TuneCarActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(HomeActivity.this, TuningHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(HomeActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
