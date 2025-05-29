package com.example.m_techcartuner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.adapter.TuningHistoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class TuningHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textViewEmpty;
    TuningHistoryAdapter adapter;
    List<com.example.m_techcartuner.model.TuningHistoryItem> tuningHistoryList;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuning_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tuningHistoryList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new TuningHistoryAdapter(tuningHistoryList, (position, item) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();

            db.collection("users")
                    .document(uid)
                    .collection("tuningHistory")
                    .document(item.getDocumentId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        runOnUiThread(() -> {
                            adapter.removeItem(position);
                        });
                    })
                    .addOnFailureListener(e -> {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    });
        });


        recyclerView.setAdapter(adapter);

        loadTuningHistory();
        setupButtons();
        setupBottomNavigation();
    }

    private void loadTuningHistory() {
        progressBar.setVisibility(View.VISIBLE);
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("tuningHistory")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        tuningHistoryList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            com.example.m_techcartuner.model.TuningHistoryItem item = doc.toObject(com.example.m_techcartuner.model.TuningHistoryItem.class);
                            item.setDocumentId(doc.getId());  // 💡 Save Firestore document ID
                            tuningHistoryList.add(item);
                        }
                        adapter.notifyDataSetChanged();

                        textViewEmpty.setVisibility(tuningHistoryList.isEmpty() ? View.VISIBLE : View.GONE);
                    } else {
                        Log.e("Firestore", "Error loading history", task.getException());
                        textViewEmpty.setText("Failed to load history.");
                        textViewEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setupButtons() {
        // Profile button at top-right
        findViewById(R.id.buttonProfile).setOnClickListener(v -> {
            startActivity(new Intent(TuningHistoryActivity.this, ProfileActivity.class));
        });
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);

        // Set the selected item to "Home" because this is the home page
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_tune) {
                startActivity(new Intent(TuningHistoryActivity.this, TuneCarActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(TuningHistoryActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(TuningHistoryActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
