package com.app.smartopd.DoctorModule.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.app.smartopd.LoginActivity;   // ← your login screen activity
import com.app.smartopd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LeaveFragment extends Fragment {

    Switch   switchEmergency;
    Button   btnPickDate, btnSubmitLeave, btnLogout;
    TextView tvSelectedDate, tvDoctorLoggedName;
    CardView cardSelectedDate;

    // ── Firebase ──────────────────────────────────
    DatabaseReference doctorRef;
    String doctorId    = "doctor_001"; // Replace with actual logged-in doctor ID
    String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        // ── Firebase Init ─────────────────────────
        doctorRef = FirebaseDatabase.getInstance()
                .getReference("doctors").child(doctorId);

        // ── Bind Views ────────────────────────────
        switchEmergency    = view.findViewById(R.id.switchEmergency);
        btnPickDate        = view.findViewById(R.id.btnPickDate);
        btnSubmitLeave     = view.findViewById(R.id.btnSubmitLeave);
        btnLogout          = view.findViewById(R.id.btnLogout);
        tvSelectedDate     = view.findViewById(R.id.tvSelectedDate);
        tvDoctorLoggedName = view.findViewById(R.id.tvDoctorLoggedName);
        cardSelectedDate   = view.findViewById(R.id.cardSelectedDate);

        // ── Load Doctor Name for logout card ──────
        loadDoctorName();

        // ─────────────────────────────────────────
        // ✅ LOGOUT FEATURE
        // Signs out from Firebase Auth → clears session
        // Redirects to LoginActivity and clears back stack
        // so user cannot press Back to return.
        // ─────────────────────────────────────────
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes, Logout", (dialog, which) -> {
                        performLogout();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // ─────────────────────────────────────────
        // ✅ FEATURE 7: Date Picker for planned leave
        // ─────────────────────────────────────────
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(requireContext(),
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

        // ─────────────────────────────────────────
        // ✅ FEATURE 8: Emergency leave toggle
        // Instantly marks doctor unavailable + notifies all patients
        // ─────────────────────────────────────────
        switchEmergency.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("⚠️ Emergency Leave?")
                        .setMessage("This will cancel ALL today's appointments and notify every waiting patient immediately.")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            doctorRef.child("isAvailable").setValue(false);
                            doctorRef.child("emergencyLeave").setValue(true);
                            doctorRef.child("sessionFull").setValue(true);
                            notifyAllPatients("Emergency Leave",
                                    "Dr. has taken an emergency leave. Your token for today is cancelled.");
                            Toast.makeText(getContext(),
                                    "Emergency leave activated. Patients notified!",
                                    Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            switchEmergency.setChecked(false); // revert toggle if cancelled
                        })
                        .show();
            } else {
                doctorRef.child("emergencyLeave").setValue(false);
            }
        });

        // ─────────────────────────────────────────
        // ✅ FEATURE 9: Submit planned leave + notify patients
        // ─────────────────────────────────────────
        btnSubmitLeave.setOnClickListener(v -> {
            if (selectedDate.isEmpty() && !switchEmergency.isChecked()) {
                Toast.makeText(getContext(),
                        "Please select a leave date or enable emergency leave",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!selectedDate.isEmpty()) {
                doctorRef.child("plannedLeave").setValue(selectedDate)
                        .addOnSuccessListener(unused -> {
                            notifyAllPatients(
                                    "Planned Leave on " + selectedDate,
                                    "Dr. is on planned leave on " + selectedDate + ". Please reschedule."
                            );
                            Toast.makeText(getContext(),
                                    "✅ Leave submitted for " + selectedDate + ". Patients notified!",
                                    Toast.LENGTH_LONG).show();
                            // Reset state
                            selectedDate = "";
                            cardSelectedDate.setVisibility(View.GONE);
                        });
            }
        });

        return view;
    }

    // ─────────────────────────────────────────────
    /**
     * LOGOUT METHOD
     * 1. Signs out from Firebase Auth (clears session)
     * 2. Redirects to LoginActivity
     * 3. FLAG_CLEAR_TASK ensures back press doesn't return to doctor screen
     */
    private void performLogout() {
        // Step 1: Sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Step 2: Go to LoginActivity and clear entire back stack
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Step 3: Close current activity
        requireActivity().finish();

        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    // ─────────────────────────────────────────────
    /**
     * Load doctor name from Firebase to show in logout card
     */
    private void loadDoctorName() {
        doctorRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!isAdded()) return;
                String name = snapshot.getValue(String.class);
                if (name != null) {
                    tvDoctorLoggedName.setText("Dr. " + name);
                }
            }
            @Override public void onCancelled(DatabaseError error) {}
        });
    }

    // ─────────────────────────────────────────────
    /**
     * FEATURE 10: Notify all waiting patients of this doctor
     * Writes notification to each patient's notifications/ node
     * Also cancels their token by setting status = "cancelled"
     */
    private void notifyAllPatients(String title, String message) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance()
                .getReference("tokens");

        tokensRef.orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String patientId = ds.child("patientId").getValue(String.class);
                            String status    = ds.child("status").getValue(String.class);

                            if (patientId != null && "waiting".equals(status)) {

                                // Write notification for each patient
                                DatabaseReference notifRef = FirebaseDatabase.getInstance()
                                        .getReference("notifications")
                                        .child(patientId).push();

                                Map<String, Object> notif = new HashMap<>();
                                notif.put("title",     title);
                                notif.put("message",   message);
                                notif.put("timestamp", System.currentTimeMillis());
                                notif.put("read",      false);
                                notifRef.setValue(notif);

                                // Cancel their token
                                ds.getRef().child("status").setValue("cancelled");
                            }
                        }
                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });
    }
}