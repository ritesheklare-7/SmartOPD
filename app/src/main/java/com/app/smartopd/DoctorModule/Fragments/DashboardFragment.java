package com.app.smartopd.DoctorModule.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartopd.DoctorModule.Adapters.PatientAdapter;
import com.app.smartopd.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    Switch switchAvailability;
    TextView tvStatusBadge, tvIssuedCount, tvWaitingCount, tvProgressPercent, tvDoctorName;
    ProgressBar progressTokens;
    Button btnMarkFull;
    RecyclerView rvPatients;

    // Firebase
    DatabaseReference doctorRef;
    String doctorId = "doctor_001"; // Replace with actual logged-in doctor ID

    // Patient list for RecyclerView
    List<String> patientList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize Firebase
        doctorRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctorId);

        // Bind Views
        switchAvailability = view.findViewById(R.id.switchAvailability);
        tvStatusBadge      = view.findViewById(R.id.tvStatusBadge);
        tvIssuedCount      = view.findViewById(R.id.tvIssuedCount);
        tvWaitingCount     = view.findViewById(R.id.tvWaitingCount);
        tvProgressPercent  = view.findViewById(R.id.tvProgressPercent);
        tvDoctorName       = view.findViewById(R.id.tvDoctorName);
        progressTokens     = view.findViewById(R.id.progressTokens);
        btnMarkFull        = view.findViewById(R.id.btnMarkFull);
        rvPatients         = view.findViewById(R.id.rvPatients);

        // Setup RecyclerView
        rvPatients.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ FEATURE 1: Real-time availability toggle synced with Firebase
        doctorRef.child("isAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean isAvailable = snapshot.getValue(Boolean.class);
                if (isAvailable != null) {
                    switchAvailability.setChecked(isAvailable);
                    updateStatusBadge(isAvailable);
                }
            }
            @Override public void onCancelled(DatabaseError error) {}
        });

        switchAvailability.setOnCheckedChangeListener((btn, isChecked) -> {
            // Write availability to Firebase instantly
            doctorRef.child("isAvailable").setValue(isChecked);
            updateStatusBadge(isChecked);
            String msg = isChecked ? "You are now Available ✅" : "You are now marked On Leave 🔴";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // ✅ FEATURE 2: Live token count from Firebase
        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long issued  = snapshot.child("currentToken").getValue(Long.class);
                Long maxTok  = snapshot.child("maxTokens").getValue(Long.class);
                String name  = snapshot.child("name").getValue(String.class);

                if (issued == null) issued = 0L;
                if (maxTok == null) maxTok = 40L;
                if (name != null) tvDoctorName.setText("Dr. " + name);

                int issuedInt = issued.intValue();
                int maxInt    = maxTok.intValue();
                int waiting   = issuedInt; // simplification; refine if needed

                tvIssuedCount.setText(String.valueOf(issuedInt));
                tvWaitingCount.setText(String.valueOf(waiting));
                tvProgressPercent.setText(issuedInt + "/" + maxInt);
                progressTokens.setMax(maxInt);
                progressTokens.setProgress(issuedInt);
            }
            @Override public void onCancelled(DatabaseError error) {}
        });

        // ✅ FEATURE 3: Live waiting patient list
        FirebaseDatabase.getInstance().getReference("tokens")
                .orderByChild("doctorId").equalTo(doctorId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        patientList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String status = ds.child("status").getValue(String.class);
                            if ("waiting".equals(status)) {
                                String pName  = ds.child("patientName").getValue(String.class);
                                String tokNum = ds.child("tokenNumber").getValue(String.class);
                                patientList.add("Token #" + tokNum + "  —  " + pName);
                            }
                        }
                        // Update RecyclerView adapter
                        PatientAdapter pa = new PatientAdapter(patientList);
                        rvPatients.setAdapter(pa);
                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });

        // ✅ FEATURE 4: Mark Session Full — stops new token generation
        btnMarkFull.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Mark Session Full?")
                    .setMessage("No new tokens will be issued. This cannot be undone for today.")
                    .setPositiveButton("Yes, Mark Full", (dialog, which) -> {
                        doctorRef.child("sessionFull").setValue(true);
                        btnMarkFull.setEnabled(false);
                        btnMarkFull.setText("Session is Full 🔴");
                        Toast.makeText(getContext(), "Session marked full!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    // Helper to update status badge color and text
    private void updateStatusBadge(boolean isAvailable) {
        if (isAvailable) {
            tvStatusBadge.setText("● AVAILABLE");
            tvStatusBadge.setTextColor(getResources().getColor(R.color.accent_green));
        } else {
            tvStatusBadge.setText("● ON LEAVE");
            tvStatusBadge.setTextColor(getResources().getColor(R.color.red));
        }
    }
}