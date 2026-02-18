package com.app.smartopd.DoctorModule.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartopd.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LeaveFragment extends Fragment {

    Switch switchEmergency;
    Button btnPickDate, btnSubmitLeave;
    TextView tvSelectedDate;
    CardView cardSelectedDate;
    DatabaseReference doctorRef;
    String doctorId   = "doctor_001";
    String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        doctorRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctorId);

        switchEmergency = view.findViewById(R.id.switchEmergency);
        btnPickDate     = view.findViewById(R.id.btnPickDate);
        btnSubmitLeave  = view.findViewById(R.id.btnSubmitLeave);
        tvSelectedDate  = view.findViewById(R.id.tvSelectedDate);
        cardSelectedDate = view.findViewById(R.id.cardSelectedDate);

        // ✅ FEATURE 7: Date Picker for planned leave
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(getContext(),
                    (dpView, year, month, day) -> {
                        selectedDate = day + "/" + (month + 1) + "/" + year;
                        tvSelectedDate.setText(selectedDate);
                        cardSelectedDate.setVisibility(View.VISIBLE);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            // Prevent selecting past dates
            dp.getDatePicker().setMinDate(System.currentTimeMillis());
            dp.show();
        });

        // ✅ FEATURE 8: Emergency leave — instant Firebase update + notification trigger
        switchEmergency.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                new AlertDialog.Builder(getContext())
                        .setTitle("⚠️ Emergency Leave?")
                        .setMessage("This will cancel ALL today's appointments and notify every waiting patient immediately.")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            // Mark doctor unavailable immediately
                            doctorRef.child("isAvailable").setValue(false);
                            doctorRef.child("emergencyLeave").setValue(true);
                            doctorRef.child("sessionFull").setValue(true);
                            // Notify all patients for today
                            notifyAllPatients("Emergency Leave",
                                    "Dr. has taken an emergency leave. Your token for today is cancelled.");
                            Toast.makeText(getContext(),
                                    "Emergency leave activated. Patients notified!", Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            switchEmergency.setChecked(false); // revert toggle
                        })
                        .show();
            } else {
                doctorRef.child("emergencyLeave").setValue(false);
            }
        });

        // ✅ FEATURE 9: Submit planned leave & notify patients
        btnSubmitLeave.setOnClickListener(v -> {
            if (selectedDate.isEmpty() && !switchEmergency.isChecked()) {
                Toast.makeText(getContext(),
                        "Please select a leave date or enable emergency leave", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!selectedDate.isEmpty()) {
                doctorRef.child("plannedLeave").setValue(selectedDate)
                        .addOnSuccessListener(unused -> {
                            notifyAllPatients("Planned Leave on " + selectedDate,
                                    "Dr. is on planned leave on " + selectedDate + ". Please reschedule.");
                            Toast.makeText(getContext(),
                                    "✅ Leave submitted for " + selectedDate + ". Patients notified!",
                                    Toast.LENGTH_LONG).show();
                            selectedDate = "";
                            cardSelectedDate.setVisibility(View.GONE);
                        });
            }
        });

        return view;
    }

    // ✅ FEATURE 10: Notify all patients booked with this doctor
    private void notifyAllPatients(String title, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference("tokens");
        tokensRef.orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String patientId = ds.child("patientId").getValue(String.class);
                            String status    = ds.child("status").getValue(String.class);
                            if (patientId != null && "waiting".equals(status)) {
                                // Write notification to Firebase for each patient
                                DatabaseReference notifRef = FirebaseDatabase.getInstance()
                                        .getReference("notifications").child(patientId).push();
                                Map<String, Object> notif = new HashMap<>();
                                notif.put("title", title);
                                notif.put("message", message);
                                notif.put("timestamp", System.currentTimeMillis());
                                notif.put("read", false);
                                notifRef.setValue(notif);

                                // Also cancel their token
                                ds.getRef().child("status").setValue("cancelled");
                            }
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });
    }
}