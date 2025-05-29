package com.example.m_techcartuner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.adapter.SuggestionsAdapter;
import com.example.m_techcartuner.api.OpenAIApi;
import com.example.m_techcartuner.api.OpenAIClient;
import com.example.m_techcartuner.model.ChatRequest;
import com.example.m_techcartuner.model.ChatResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;

public class TuneCarActivity extends AppCompatActivity {

    private EditText editMake, editModel, editYear, editEngine;
    private Spinner spinnerGoal;
    private Button buttonPickFromGarage;
    private TextView textViewAdvice;
    private RecyclerView recyclerViewSuggestions;

    private final String[] goals = {"More Horsepower", "Better Fuel Efficiency", "More Torque"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune_car);  // create this layout next

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupSpinner();
        setupRecyclerView();
        setupButtons();
        setupGetAdviceButton();
        setupBottomNavigation();
    }

    private void initViews() {
        editMake = findViewById(R.id.editMake);
        editModel = findViewById(R.id.editModel);
        editYear = findViewById(R.id.editYear);
        editEngine = findViewById(R.id.editEngine);
        spinnerGoal = findViewById(R.id.spinnerGoal);
        buttonPickFromGarage = findViewById(R.id.buttonPickFromGarage);
        textViewAdvice = findViewById(R.id.textViewAdvice);
        recyclerViewSuggestions = findViewById(R.id.recyclerViewSuggestions);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        recyclerViewSuggestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSuggestions.setVisibility(View.GONE);
    }

    private void setupButtons() {
        findViewById(R.id.buttonProfile).setOnClickListener(v -> {
            startActivity(new Intent(TuneCarActivity.this, ProfileActivity.class));
        });
    }

    private void setupGetAdviceButton() {
        buttonPickFromGarage.setOnClickListener(v -> {
            final String make = editMake.getText().toString().trim();
            final String model = editModel.getText().toString().trim();
            final String year = editYear.getText().toString().trim();
            final String engine = editEngine.getText().toString().trim();
            final String goal = spinnerGoal.getSelectedItem().toString();

            if (make.isEmpty() || model.isEmpty() || year.isEmpty() || engine.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchTuningSuggestions(year, make, model, engine, goal);
        });
    }

    private void fetchTuningSuggestions(String year, String make, String model, String engine, String goal) {
        String prompt = "Suggest 3 car tuning upgrades for a " + year + " " + make + " " + model + " with " + engine +
                " engine to achieve " + goal + ". List them as short titles.";

        textViewAdvice.setVisibility(View.VISIBLE);
        textViewAdvice.setText("Getting suggestions...");
        recyclerViewSuggestions.setVisibility(View.GONE);

        ChatRequest.Message message = new ChatRequest.Message("user", prompt);
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", java.util.Collections.singletonList(message));

        OpenAIApi api = OpenAIClient.getClient().create(OpenAIApi.class);
        api.getChatCompletion(request).enqueue(new retrofit2.Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, retrofit2.Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseText = response.body().choices.get(0).message.content;
                    List<String> suggestions = SuggestionsAdapter.parseSuggestions(responseText);

                    runOnUiThread(() -> {
                        if (suggestions.isEmpty()) {
                            textViewAdvice.setText("No suggestions found.");
                            recyclerViewSuggestions.setVisibility(View.GONE);
                        } else {
                            showSuggestions(suggestions, year, make, model, engine);
                        }
                    });
                } else {
                    runOnUiThread(() -> textViewAdvice.setText("Error fetching suggestions."));
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                runOnUiThread(() -> textViewAdvice.setText("Failed to get suggestions: " + t.getMessage()));
            }
        });
    }

    private void showSuggestions(List<String> suggestions, String year, String make, String model, String engine) {
        recyclerViewSuggestions.setVisibility(View.VISIBLE);
        textViewAdvice.setVisibility(View.GONE);

        SuggestionsAdapter adapter = new SuggestionsAdapter(suggestions, suggestion -> {
            fetchModificationDetails(suggestion, year, make, model, engine);
        });

        recyclerViewSuggestions.setAdapter(adapter);
    }

    private void fetchModificationDetails(String suggestion, String year, String make, String model, String engine) {
        textViewAdvice.setText("Getting modification details...");
        textViewAdvice.setVisibility(View.VISIBLE);
        recyclerViewSuggestions.setVisibility(View.GONE);

        String stepPrompt = "For the car tuning upgrade '" + suggestion + "', provide the following response in this format:\n\n"
                + "**Description:** [Write a short description of what the modification does and why it's beneficial.]\n"
                + "**Performance Simulation:** [Provide an estimated performance improvement.]\n"
                + "**Step-by-Step Guide:**\n"
                + "1. [Step one]\n"
                + "2. [Step two]\n"
                + "3. ... etc.";

        ChatRequest.Message detailMessage = new ChatRequest.Message("user", stepPrompt);
        ChatRequest detailRequest = new ChatRequest("gpt-3.5-turbo", java.util.Collections.singletonList(detailMessage));

        OpenAIApi api = OpenAIClient.getClient().create(OpenAIApi.class);
        api.getChatCompletion(detailRequest).enqueue(new retrofit2.Callback<ChatResponse>() {

            @Override
            public void onResponse(Call<ChatResponse> call, retrofit2.Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String fullResponse = response.body().choices.get(0).message.content;

                    runOnUiThread(() -> {
                        Intent intent = new Intent(TuneCarActivity.this, ModificationDetailActivity.class);
                        intent.putExtra("suggestion", suggestion);
                        intent.putExtra("fullDetails", fullResponse);

                        // Pass car details too
                        intent.putExtra("carYear", year);
                        intent.putExtra("carMake", make);
                        intent.putExtra("carModel", model);
                        intent.putExtra("carEngine", engine);

                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> {
                        textViewAdvice.setText("Error fetching modification details.");
                        recyclerViewSuggestions.setVisibility(View.VISIBLE);
                    });
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    textViewAdvice.setText("Failed to load modification details: " + t.getMessage());
                    recyclerViewSuggestions.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_tune);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_tune) {
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(TuneCarActivity.this, TuningHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(TuneCarActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(TuneCarActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
