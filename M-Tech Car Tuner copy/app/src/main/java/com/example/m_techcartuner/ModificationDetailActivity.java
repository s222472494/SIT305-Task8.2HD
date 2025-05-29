package com.example.m_techcartuner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModificationDetailActivity extends AppCompatActivity {

    TextView textTitle, textDescription, textPerformance, textSteps;
    Button buttonAddModification;

    FirebaseFirestore db;
    FirebaseAuth auth;

    String suggestion, description, performance, steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_detail);

        textTitle = findViewById(R.id.textTitle);
        textDescription = findViewById(R.id.textDescription);
        textPerformance = findViewById(R.id.textPerformance);
        textSteps = findViewById(R.id.textSteps);
        buttonAddModification = findViewById(R.id.buttonAddModification);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        suggestion = getIntent().getStringExtra("suggestion");
        String fullDetails = getIntent().getStringExtra("fullDetails");

        textTitle.setText(suggestion);

        // Parse the fullDetails into description, performance, and steps
        parseFullDetails(fullDetails);

        textDescription.setText(description);
        textPerformance.setText(performance);
        textSteps.setText(steps);

        buttonAddModification.setOnClickListener(v -> saveModification());
    }

    private void parseFullDetails(String fullDetails) {
        if (fullDetails == null) {
            description = "No details available.";
            performance = "";
            steps = "";
            return;
        }

        // Example expected format:
        // **Description:** some text
        // **Performance Simulation:** some text
        // **Step-by-Step Guide:**
        // 1. Step one
        // 2. Step two
        // ...

        try {
            String descMarker = "**Description:**";
            String perfMarker = "**Performance Simulation:**";
            String stepsMarker = "**Step-by-Step Guide:**";

            int descStart = fullDetails.indexOf(descMarker);
            int perfStart = fullDetails.indexOf(perfMarker);
            int stepsStart = fullDetails.indexOf(stepsMarker);

            if (descStart != -1 && perfStart != -1 && stepsStart != -1) {
                description = fullDetails.substring(descStart + descMarker.length(), perfStart).trim();
                performance = fullDetails.substring(perfStart + perfMarker.length(), stepsStart).trim();
                steps = fullDetails.substring(stepsStart + stepsMarker.length()).trim();
            } else {
                // Fallback if format is not exact
                description = "Could not parse description.";
                performance = "Could not parse performance info.";
                steps = "Could not parse steps.";
            }
        } catch (Exception e) {
            description = "Error parsing details.";
            performance = "";
            steps = "";
        }
    }

    private void saveModification() {
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (uid == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get car details from intent
        String year = getIntent().getStringExtra("carYear");
        String make = getIntent().getStringExtra("carMake");
        String model = getIntent().getStringExtra("carModel");
        String engine = getIntent().getStringExtra("carEngine");

        if (year == null || make == null || model == null || engine == null) {
            Toast.makeText(this, "Car details missing, cannot save modification.", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonAddModification.setEnabled(false);

        Map<String, Object> mod = new HashMap<>();
        mod.put("year", year);
        mod.put("make", make);
        mod.put("model", model);
        mod.put("engine", engine);
        mod.put("goal", suggestion);
        mod.put("aiAdvice", description + "\n\n" + performance + "\n\n" + steps);
        mod.put("timestamp", new Date());

        db.collection("users").document(uid).collection("tuningHistory")
                .add(mod)
                .addOnSuccessListener(docRef -> {
                    runOnUiThread(() -> {
                        new androidx.appcompat.app.AlertDialog.Builder(ModificationDetailActivity.this)
                                .setTitle("Modification Saved")
                                .setMessage("✅ Modification added to your car history!")
                                .setPositiveButton("OK", null)
                                .show();

                        buttonAddModification.setText("Modification Saved");
                        buttonAddModification.setEnabled(false);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "❌ Failed to save modification: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    buttonAddModification.setEnabled(true);
                });
    }
}

