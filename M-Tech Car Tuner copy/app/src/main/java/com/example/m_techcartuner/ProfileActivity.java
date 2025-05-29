package com.example.m_techcartuner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewEmail;
    Button buttonLogout;
    Button buttonGarage;  // New button to open garage

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewEmail = findViewById(R.id.textViewEmail);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonGarage = findViewById(R.id.buttonGarage); // Make sure you add this button to your XML layout

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            textViewEmail.setText(user.getEmail());
        } else {
            textViewEmail.setText("No user logged in");
        }

        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Open GarageActivity when button is clicked
        buttonGarage.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, GarageActivity.class));
        });
        setupBottomNavigation();
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
                startActivity(new Intent(ProfileActivity.this, TuneCarActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(ProfileActivity.this, TuningHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(ProfileActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
