package com.example.m_techcartuner;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerformanceSimulationActivity extends AppCompatActivity {

    TextView horsepowerBefore, horsepowerAfter, torqueBefore, torqueAfter, accelerationBefore, accelerationAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_simulation);

        horsepowerBefore = findViewById(R.id.hp_before);
        horsepowerAfter = findViewById(R.id.hp_after);
        torqueBefore = findViewById(R.id.torque_before);
        torqueAfter = findViewById(R.id.torque_after);
        accelerationBefore = findViewById(R.id.acceleration_before);
        accelerationAfter = findViewById(R.id.acceleration_after);

        // Get performance data from Intent (sent from AI suggestions screen)
        int hpBefore = getIntent().getIntExtra("hp_before", 200);
        int hpAfter = getIntent().getIntExtra("hp_after", 245);
        int torqueBeforeVal = getIntent().getIntExtra("torque_before", 300);
        int torqueAfterVal = getIntent().getIntExtra("torque_after", 350);
        double accBefore = getIntent().getDoubleExtra("acc_before", 7.5);
        double accAfter = getIntent().getDoubleExtra("acc_after", 6.2);

        // Set the data to TextViews
        horsepowerBefore.setText(hpBefore + " HP");
        horsepowerAfter.setText(hpAfter + " HP");
        torqueBefore.setText(torqueBeforeVal + " Nm");
        torqueAfter.setText(torqueAfterVal + " Nm");
        accelerationBefore.setText(accBefore + " s");
        accelerationAfter.setText(accAfter + " s");
    }
}
