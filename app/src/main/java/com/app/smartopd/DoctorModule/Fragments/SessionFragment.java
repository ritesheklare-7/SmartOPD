package com.app.smartopd.DoctorModule.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.app.smartopd.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SessionFragment extends Fragment {

    TextInputEditText etMaxTokens;
    RadioButton rbMorning, rbEvening;
    Button btnSaveCapacity;
    DatabaseReference doctorRef;
    String doctorId = "doctor_001";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, container, false);

        doctorRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctorId);

        etMaxTokens     = view.findViewById(R.id.etMaxTokens);
        rbMorning       = view.findViewById(R.id.rbMorning);
        rbEvening       = view.findViewById(R.id.rbEvening);
        btnSaveCapacity = view.findViewById(R.id.btnSaveCapacity);

        // ✅ FEATURE 5: Load existing saved settings from Firebase on open
        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long max = snapshot.child("maxTokens").getValue(Long.class);
                String slot = snapshot.child("timeSlot").getValue(String.class);
                if (max != null) etMaxTokens.setText(String.valueOf(max));
                if ("Morning".equals(slot)) rbMorning.setChecked(true);
                else if ("Evening".equals(slot)) rbEvening.setChecked(true);
            }
            @Override public void onCancelled(DatabaseError error) {}
        });

        // ✅ FEATURE 6: Save session capacity and time slot to Firebase
        btnSaveCapacity.setOnClickListener(v -> {
            String maxStr = etMaxTokens.getText().toString().trim();

            if (maxStr.isEmpty()) {
                etMaxTokens.setError("Please enter max tokens");
                return;
            }
            if (!rbMorning.isChecked() && !rbEvening.isChecked()) {
                Toast.makeText(getContext(), "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }

            int maxTokens = Integer.parseInt(maxStr);
            String slot   = rbMorning.isChecked() ? "Morning" : "Evening";

            // Save both values together
            Map<String, Object> updates = new HashMap<>();
            updates.put("maxTokens", maxTokens);
            updates.put("timeSlot", slot);
            updates.put("currentToken", 0);    // Reset token count for new session
            updates.put("sessionFull", false);  // Reset session full flag

            doctorRef.updateChildren(updates).addOnSuccessListener(unused -> {
                Toast.makeText(getContext(),
                        "✅ Session saved! Max: " + maxTokens + " | Slot: " + slot,
                        Toast.LENGTH_LONG).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        return view;
    }
}