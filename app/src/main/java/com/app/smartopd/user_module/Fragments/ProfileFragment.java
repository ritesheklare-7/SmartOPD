package com.app.smartopd.user_module.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.smartopd.R;

public class ProfileFragment extends Fragment {

    // Profile views
    private TextView txtName, txtFullName, txtAge, txtBlood;

    // Temporary local user data (replace with Firebase later)
    private String fullName = "Johnathan Doe";
    private String age = "28";
    private String blood = "O+";

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Bind profile text views
        txtName = v.findViewById(R.id.txtName);
        txtFullName = v.findViewById(R.id.txtFullName);
        txtAge = v.findViewById(R.id.txtAge);
        txtBlood = v.findViewById(R.id.txtBlood);

        updateProfileUI();

        // Back
        v.findViewById(R.id.btnBack)
                .setOnClickListener(b -> requireActivity().onBackPressed());

        // Edit Profile
        v.findViewById(R.id.optEditProfile)
                .setOnClickListener(b -> openEditProfileDialog());

        // Token History
        v.findViewById(R.id.optTokenHistory)
                .setOnClickListener(b ->
                        Toast.makeText(getContext(),
                                "Token history will be shown here",
                                Toast.LENGTH_SHORT).show());

        // Help & Support
        v.findViewById(R.id.optSupport)
                .setOnClickListener(b -> openSupportDialog());

        // Logout
        v.findViewById(R.id.optLogout)
                .setOnClickListener(b -> showLogoutDialog());

        // Settings Icon
        ImageView btnSettings = v.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v1 -> openSettingsDialog());

        return v;
    }

    /* ------------------ PROFILE UI UPDATE ------------------ */

    private void updateProfileUI() {
        txtName.setText(fullName);
        txtFullName.setText("Full Name: " + fullName);
        txtAge.setText("Age: " + age + " Years");
        txtBlood.setText("Blood Group: " + blood);
    }

    /* ------------------ EDIT PROFILE ------------------ */

    private void openEditProfileDialog() {

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.simple_edit_profile_dialog, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtAge = dialogView.findViewById(R.id.edtAge);
        EditText edtBlood = dialogView.findViewById(R.id.edtBlood);

        edtName.setText(fullName);
        edtAge.setText(age);
        edtBlood.setText(blood);

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {

                    fullName = edtName.getText().toString().trim();
                    age = edtAge.getText().toString().trim();
                    blood = edtBlood.getText().toString().trim();

                    updateProfileUI();

                    Toast.makeText(getContext(),
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /* ------------------ LOGOUT ------------------ */

    private void showLogoutDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(getContext(),
                            "Logged out successfully",
                            Toast.LENGTH_SHORT).show();

                    // Later: clear session & navigate to login
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /* ------------------ HELP & SUPPORT ------------------ */

    private void openSupportDialog() {

        String message =
                "Name: Ritesh Eklare\n\n" +
                        "Phone: 8080402048\n\n" +
                        "Email: ritesheklare7@gmail.com";

        new AlertDialog.Builder(getContext())
                .setTitle("Help & Support")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /* ------------------ SETTINGS ------------------ */

    private void openSettingsDialog() {

        String[] options = {
                "Change Theme (Coming Soon)",
                "Notifications",
                "Privacy Policy",
                "About App"
        };

        new AlertDialog.Builder(getContext())
                .setTitle("Settings")
                .setItems(options, (dialog, which) -> {

                    switch (which) {
                        case 0:
                            Toast.makeText(getContext(),
                                    "Theme option coming soon",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            Toast.makeText(getContext(),
                                    "Notifications settings",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case 2:
                            Toast.makeText(getContext(),
                                    "Privacy policy will be shown",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case 3:
                            Toast.makeText(getContext(),
                                    "Smart OPD v1.0\nHackathon Build",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .show();
    }
}

