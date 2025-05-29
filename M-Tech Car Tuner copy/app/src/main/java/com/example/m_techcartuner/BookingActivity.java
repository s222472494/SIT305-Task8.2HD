//package com.example.m_techcartuner;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.firestore.*;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class BookingActivity extends AppCompatActivity {
//
//    private EditText nameInput, contactInput, makeInput, modelInput, yearInput, dateInput, timeInput;
//    private Button buttonSubmit;
//    private FirebaseFirestore db;
//
//    private final Calendar calendar = Calendar.getInstance();
//    private final Set<String> bookedDateTimes = new HashSet<>();
//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booking);
//
//        db = FirebaseFirestore.getInstance();
//
//        nameInput = findViewById(R.id.editTextName);
//        contactInput = findViewById(R.id.editTextContact);
//        makeInput = findViewById(R.id.editTextCarMake);
//        modelInput = findViewById(R.id.editTextCarModel);
//        yearInput = findViewById(R.id.editTextCarYear);
//        dateInput = findViewById(R.id.editTextDate);
//        timeInput = findViewById(R.id.editTextTime);
//        buttonSubmit = findViewById(R.id.buttonSubmitBooking);
//
//        loadBookedDateTimes(); // Load booked slots
//
//        dateInput.setOnClickListener(v -> showDatePicker());
//        timeInput.setOnClickListener(v -> showTimePicker());
//
//        buttonSubmit.setOnClickListener(v -> submitBooking());
//    }
//
//    private void loadBookedDateTimes() {
//        db.collection("Bookings")
//                .get()
//                .addOnSuccessListener(querySnapshots -> {
//                    for (QueryDocumentSnapshot doc : querySnapshots) {
//                        String date = doc.getString("date");
//                        String time = doc.getString("time");
//                        if (date != null && time != null) {
//                            bookedDateTimes.add(date + "_" + time);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load booked slots", Toast.LENGTH_SHORT).show());
//    }
//
//    private void showDatePicker() {
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) -> {
//            calendar.set(y, m, d);
//            String selectedDate = dateFormat.format(calendar.getTime());
//            dateInput.setText(selectedDate);
//        }, year, month, day);
//
//        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()); // Block past dates
//        datePicker.show();
//    }
//
//    private void showTimePicker() {
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePicker = new TimePickerDialog(this, (view, h, m) -> {
//            calendar.set(Calendar.HOUR_OF_DAY, h);
//            calendar.set(Calendar.MINUTE, m);
//            String selectedTime = timeFormat.format(calendar.getTime());
//            timeInput.setText(selectedTime);
//        }, hour, minute, true); // true = 24-hour format
//
//        timePicker.show();
//    }
//
//    private void submitBooking() {
//        String name = nameInput.getText().toString().trim();
//        String contact = contactInput.getText().toString().trim();
//        String make = makeInput.getText().toString().trim();
//        String model = modelInput.getText().toString().trim();
//        String year = yearInput.getText().toString().trim();
//        String date = dateInput.getText().toString().trim();
//        String time = timeInput.getText().toString().trim();
//
//        if (name.isEmpty() || contact.isEmpty() || make.isEmpty() || model.isEmpty() ||
//                year.isEmpty() || date.isEmpty() || time.isEmpty()) {
//            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String slotKey = date + "_" + time;
//        if (bookedDateTimes.contains(slotKey)) {
//            Toast.makeText(this, "❌ This time slot is already booked.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        Map<String, Object> booking = new HashMap<>();
//        booking.put("name", name);
//        booking.put("contact", contact);
//        booking.put("carMake", make);
//        booking.put("carModel", model);
//        booking.put("carYear", year);
//        booking.put("date", date);
//        booking.put("time", time);
//        booking.put("timestamp", new Date());
//
//        db.collection("Bookings")
//                .add(booking)
//                .addOnSuccessListener(doc -> {
//                    Toast.makeText(this, "✅ Booking confirmed!", Toast.LENGTH_SHORT).show();
//                    finish();
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
//    }
//}

package com.example.m_techcartuner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends AppCompatActivity {

    private EditText nameInput, contactInput, makeInput, modelInput, yearInput, dateInput, timeInput;
    private Button buttonSubmit;
    private FirebaseFirestore db;

    private final Calendar calendar = Calendar.getInstance();
    private final Set<String> bookedDateTimes = new HashSet<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    // Define allowed booking times for your service (you can customize these)
    private final String[] allowedTimes = {
            "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.editTextName);
        contactInput = findViewById(R.id.editTextContact);
        makeInput = findViewById(R.id.editTextCarMake);
        modelInput = findViewById(R.id.editTextCarModel);
        yearInput = findViewById(R.id.editTextCarYear);
        dateInput = findViewById(R.id.editTextDate);
        timeInput = findViewById(R.id.editTextTime);
        buttonSubmit = findViewById(R.id.buttonSubmitBooking);

        loadBookedDateTimes(); // Load all booked slots from Firestore

        // Show DatePicker dialog when clicking dateInput
        dateInput.setOnClickListener(v -> showDatePicker());

        // Show TimePicker dialog when clicking timeInput
        timeInput.setOnClickListener(v -> showTimePicker());

        buttonSubmit.setOnClickListener(v -> submitBooking());
        setupBottomNavigation();
        setupButtons();
    }

    private void loadBookedDateTimes() {
        db.collection("Bookings")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    bookedDateTimes.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String date = doc.getString("date");
                        String time = doc.getString("time");
                        if (date != null && time != null) {
                            bookedDateTimes.add(date + "_" + time);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load booked slots", Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) -> {
            calendar.set(y, m, d);
            String selectedDate = dateFormat.format(calendar.getTime());

            if (isDateFullyBooked(selectedDate)) {
                Toast.makeText(this, "Sorry, no available time slots on this date.", Toast.LENGTH_LONG).show();
                dateInput.setText("");
                timeInput.setText("");
            } else {
                dateInput.setText(selectedDate);
                timeInput.setText(""); // Reset time input because date changed
            }
        }, year, month, day);

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()); // Block past dates
        datePicker.show();
    }

    private void showTimePicker() {
        String selectedDate = dateInput.getText().toString().trim();
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Filter allowed times to exclude booked ones for the selected date
        List<String> availableTimes = new ArrayList<>();
        for (String time : allowedTimes) {
            if (!bookedDateTimes.contains(selectedDate + "_" + time)) {
                availableTimes.add(time);
            }
        }

        if (availableTimes.isEmpty()) {
            Toast.makeText(this, "No available time slots for this date.", Toast.LENGTH_LONG).show();
            return;
        }

        // Inflate custom dialog layout
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_time_picker, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.textViewTitle);
        ListView listView = dialogView.findViewById(R.id.listViewTimes);

        // Set adapter with available times
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availableTimes);
        listView.setAdapter(adapter);

        android.app.AlertDialog dialog = builder.create();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String chosenTime = availableTimes.get(position);
            timeInput.setText(chosenTime);
            dialog.dismiss();
        });

        dialog.show();
    }

    private boolean isDateFullyBooked(String date) {
        for (String time : allowedTimes) {
            if (!bookedDateTimes.contains(date + "_" + time)) {
                return false; // Found at least one available slot
            }
        }
        return true; // All allowed times are booked
    }

    private void submitBooking() {
        String name = nameInput.getText().toString().trim();
        String contact = contactInput.getText().toString().trim();
        String make = makeInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String year = yearInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();

        if (name.isEmpty() || contact.isEmpty() || make.isEmpty() || model.isEmpty() ||
                year.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String slotKey = date + "_" + time;
        if (bookedDateTimes.contains(slotKey)) {
            Toast.makeText(this, "❌ This time slot is already booked.", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> booking = new HashMap<>();
        booking.put("name", name);
        booking.put("contact", contact);
        booking.put("carMake", make);
        booking.put("carModel", model);
        booking.put("carYear", year);
        booking.put("date", date);
        booking.put("time", time);
        booking.put("timestamp", new Date());

        db.collection("Bookings")
                .add(booking)
                .addOnSuccessListener(doc -> {
                    bookedDateTimes.add(slotKey); // Add new booking to local set
                    Toast.makeText(this, "✅ Booking confirmed!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void setupButtons() {
        // Profile button at top-right
        findViewById(R.id.buttonProfile).setOnClickListener(v -> {
            startActivity(new Intent(BookingActivity.this, ProfileActivity.class));
        });
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);

        // Set the selected item to "Home" because this is the home page
        bottomNavigationView.setSelectedItemId(R.id.nav_booking);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_booking) {
                return true;
            } else if (id == R.id.nav_tune) {
                startActivity(new Intent(BookingActivity.this, TuneCarActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(BookingActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(BookingActivity.this, TuningHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
