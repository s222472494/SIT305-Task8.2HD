//package com.example.m_techcartuner;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.m_techcartuner.adapter.CarAdapter;
//import com.example.m_techcartuner.model.Car;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GarageActivity extends AppCompatActivity {
//
//    EditText editMake, editModel, editYear, editEngine;
//    Button buttonAddCar;
//    RecyclerView recyclerViewCars;
//
//    FirebaseAuth auth;
//    FirebaseFirestore db;
//    CollectionReference garageRef;
//
//    List<Car> carList = new ArrayList<>();
//    CarAdapter carAdapter;
//
//    boolean isPickMode = false;
//
//    ListenerRegistration carsListener;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_garage);
//
//        auth = FirebaseAuth.getInstance();
//        String uid = auth.getCurrentUser().getUid();
//
//        editMake = findViewById(R.id.editMake);
//        editModel = findViewById(R.id.editModel);
//        editYear = findViewById(R.id.editYear);
//        editEngine = findViewById(R.id.editEngine);
//        buttonAddCar = findViewById(R.id.btnAddCar);
//        recyclerViewCars = findViewById(R.id.recyclerViewCars);
//
//        db = FirebaseFirestore.getInstance();
//        // Firestore structure: "users" collection > userId document > "garage" subcollection
//        garageRef = db.collection("users").document(uid).collection("garage");
//
//        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
//
//        isPickMode = "pick_car".equals(getIntent().getStringExtra("mode"));
//
//        carAdapter = new CarAdapter(carList, isPickMode ? car -> {
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("make", car.getMake());
//            resultIntent.putExtra("model", car.getModel());
//            resultIntent.putExtra("year", car.getYear());
//            resultIntent.putExtra("engine", car.getEngine());
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        } : null);
//
//        recyclerViewCars.setAdapter(carAdapter);
//
//        if (isPickMode) {
//            editMake.setVisibility(EditText.GONE);
//            editModel.setVisibility(EditText.GONE);
//            editYear.setVisibility(EditText.GONE);
//            editEngine.setVisibility(EditText.GONE);
//            buttonAddCar.setVisibility(Button.GONE);
//        } else {
//            buttonAddCar.setOnClickListener(v -> addCar());
//        }
//
//        loadCars();
//    }
//
//    private void addCar() {
//        String make = editMake.getText().toString().trim();
//        String model = editModel.getText().toString().trim();
//        String year = editYear.getText().toString().trim();
//        String engine = editEngine.getText().toString().trim();
//
//        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || engine.isEmpty()) {
//            Toast.makeText(this, "Please fill all car fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Toast.makeText(this, "Adding car...", Toast.LENGTH_SHORT).show();
//
//        // Let Firestore generate an ID automatically
//        DocumentReference newCarDoc = garageRef.document();
//
//        Car newCar = new Car(newCarDoc.getId(), make, model, year, engine);
//
//        newCarDoc.set(newCar)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(this, "Car added to garage", Toast.LENGTH_SHORT).show();
//                    clearInputs();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Failed to add car: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                });
//    }
//
//    private void clearInputs() {
//        editMake.setText("");
//        editModel.setText("");
//        editYear.setText("");
//        editEngine.setText("");
//    }
//
//    private void loadCars() {
//        // Use a realtime listener so the UI updates live on any change
//        carsListener = garageRef.addSnapshotListener((value, error) -> {
//            if (error != null) {
//                Toast.makeText(GarageActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            carList.clear();
//            if (value != null) {
//                for (DocumentSnapshot doc : value.getDocuments()) {
//                    Car car = doc.toObject(Car.class);
//                    if (car != null) {
//                        carList.add(car);
//                    }
//                }
//            }
//            carAdapter.notifyDataSetChanged();
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (carsListener != null) {
//            carsListener.remove();
//        }
//    }
//}

package com.example.m_techcartuner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.adapter.CarAdapter;
import com.example.m_techcartuner.model.Car;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class GarageActivity extends AppCompatActivity {

    EditText editMake, editModel, editYear, editEngine;
    Button buttonAddCar;
    RecyclerView recyclerViewCars;

    FirebaseAuth auth;
    FirebaseFirestore db;
    CollectionReference garageRef;

    List<Car> carList = new ArrayList<>();
    CarAdapter carAdapter;

    boolean isPickMode = false;

    ListenerRegistration carsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        editMake = findViewById(R.id.editMake);
        editModel = findViewById(R.id.editModel);
        editYear = findViewById(R.id.editYear);
        editEngine = findViewById(R.id.editEngine);
        buttonAddCar = findViewById(R.id.btnAddCar);
        recyclerViewCars = findViewById(R.id.recyclerViewCars);

        db = FirebaseFirestore.getInstance();
        garageRef = db.collection("users").document(uid).collection("garage");

        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));

        isPickMode = "pick_car".equals(getIntent().getStringExtra("mode"));

        carAdapter = new CarAdapter(carList, isPickMode ? car -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("make", car.getMake());
            resultIntent.putExtra("model", car.getModel());
            resultIntent.putExtra("year", car.getYear());
            resultIntent.putExtra("engine", car.getEngine());
            setResult(RESULT_OK, resultIntent);
            finish();
        } : null);

        recyclerViewCars.setAdapter(carAdapter);

        if (isPickMode) {
            editMake.setVisibility(EditText.GONE);
            editModel.setVisibility(EditText.GONE);
            editYear.setVisibility(EditText.GONE);
            editEngine.setVisibility(EditText.GONE);
            buttonAddCar.setVisibility(Button.GONE);
        } else {
            buttonAddCar.setOnClickListener(v -> addCar());

            // Long press to delete functionality
            carAdapter.setOnCarLongClickListener(car -> {
                new android.app.AlertDialog.Builder(GarageActivity.this)
                        .setTitle("Delete Car")
                        .setMessage("Are you sure you want to delete this car?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            garageRef.document(car.getId())
                                    .delete()
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(GarageActivity.this, "Car deleted", Toast.LENGTH_SHORT).show()
                                    )
                                    .addOnFailureListener(e ->
                                            Toast.makeText(GarageActivity.this, "Failed to delete car", Toast.LENGTH_SHORT).show()
                                    );
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        loadCars();
    }

    private void addCar() {
        String make = editMake.getText().toString().trim();
        String model = editModel.getText().toString().trim();
        String year = editYear.getText().toString().trim();
        String engine = editEngine.getText().toString().trim();

        if (make.isEmpty() || model.isEmpty() || year.isEmpty() || engine.isEmpty()) {
            Toast.makeText(this, "Please fill all car fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Adding car...", Toast.LENGTH_SHORT).show();

        DocumentReference newCarDoc = garageRef.document();
        Car newCar = new Car(newCarDoc.getId(), make, model, year, engine);

        newCarDoc.set(newCar)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Car added to garage", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add car: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
    }

    private void clearInputs() {
        editMake.setText("");
        editModel.setText("");
        editYear.setText("");
        editEngine.setText("");
    }

    private void loadCars() {
        carsListener = garageRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(GarageActivity.this, "Failed to load cars.", Toast.LENGTH_SHORT).show();
                return;
            }

            carList.clear();
            if (value != null) {
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Car car = doc.toObject(Car.class);
                    if (car != null) {
                        carList.add(car);
                    }
                }
            }
            carAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (carsListener != null) {
            carsListener.remove();
        }
    }
}
