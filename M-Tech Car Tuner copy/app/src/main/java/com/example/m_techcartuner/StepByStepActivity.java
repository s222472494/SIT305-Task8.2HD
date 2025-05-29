package com.example.m_techcartuner;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StepByStepActivity extends AppCompatActivity {

    TextView textViewStepDetails, textViewSuggestionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step);

        textViewSuggestionTitle = findViewById(R.id.textViewSuggestionTitle);
        textViewStepDetails = findViewById(R.id.textViewStepDetails);

        // Get data from intent
        String suggestion = getIntent().getStringExtra("suggestion");
        String steps = getIntent().getStringExtra("steps");

        if (suggestion != null) {
            textViewSuggestionTitle.setText(suggestion);
        } else {
            textViewSuggestionTitle.setText("No suggestion title received.");
        }

        if (steps != null) {
            textViewStepDetails.setText(steps);
        } else {
            textViewStepDetails.setText("No step-by-step instructions received.");
        }
    }
}
