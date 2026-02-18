package com.app.smartopd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword;
    TextView btnPatient, btnDoctor, tvLogin;
    
    FirebaseAuth      mAuth;
    FirebaseFirestore db;

    // ── Role Selection ────────────────────────────
    // Tracks which role user selected: "patient" or "doctor"
    String selectedRole = "patient"; // default is patient

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        // ── Bind Views ────────────────────────────
        etName     = findViewById(R.id.etName);
        etEmail    = findViewById(R.id.etEmail);
        etPhone    = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnPatient = findViewById(R.id.btnPatient);
        btnDoctor  = findViewById(R.id.btnDoctor);
        tvLogin    = findViewById(R.id.tvLogin);

        // ── Default: Patient selected ─────────────
        setPatientSelected();

        // ─────────────────────────────────────────
        // ROLE TOGGLE: Patient button tap
        // ─────────────────────────────────────────
        btnPatient.setOnClickListener(v -> {
            selectedRole = "patient";
            setPatientSelected();
        });

        // ─────────────────────────────────────────
        // ROLE TOGGLE: Doctor button tap
        // ─────────────────────────────────────────
        btnDoctor.setOnClickListener(v -> {
            selectedRole = "doctor";
            setDoctorSelected();
        });

        // ─────────────────────────────────────────
        // REGISTER BUTTON
        // ─────────────────────────────────────────
        findViewById(R.id.btnRegister).setOnClickListener(v -> registerUser());

        // ─────────────────────────────────────────
        // GO TO LOGIN
        // ─────────────────────────────────────────
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    // ─────────────────────────────────────────────
    /**
     * REGISTER USER
     * Step 1 — Validate inputs
     * Step 2 — Create Firebase Auth account
     * Step 3 — Save user data + role to Firestore
     * Step 4 — Redirect to LoginActivity
     */
    private void registerUser() {
        String name     = etName.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ── Validation ────────────────────────────
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // ── Step 2: Create Firebase Auth account ──
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String uid = mAuth.getCurrentUser().getUid();

                    // ── Step 3: Save to Firestore ─
                    // role field = "patient" or "doctor"
                    // LoginActivity reads this to decide which screen to open
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("uid",       uid);
                    userMap.put("name",      name);
                    userMap.put("email",     email);
                    userMap.put("phone",     phone);
                    userMap.put("role",      selectedRole);  // ← KEY FIELD
                    userMap.put("createdAt", System.currentTimeMillis());

                    db.collection("users")
                            .document(uid)
                            .set(userMap)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this,
                                        "Registration Successful ✅\nPlease login as " + selectedRole,
                                        Toast.LENGTH_LONG).show();

                                // ── Step 4: Go to Login ──
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this,
                                            "Firestore Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Auth Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    // ─────────────────────────────────────────────
    // UI HELPERS: Toggle button highlight
    // ─────────────────────────────────────────────

    private void setPatientSelected() {
        // Patient button — highlighted
        btnPatient.setBackgroundResource(R.drawable.bg_toggle_selected);
        btnPatient.setTextColor(getColor(R.color.primary_blue));

        // Doctor button — not selected
        btnDoctor.setBackgroundResource(android.R.color.transparent);
        btnDoctor.setTextColor(getColor(R.color.text_gray));
    }

    private void setDoctorSelected() {
        // Doctor button — highlighted
        btnDoctor.setBackgroundResource(R.drawable.bg_toggle_selected);
        btnDoctor.setTextColor(getColor(R.color.primary_blue));

        // Patient button — not selected
        btnPatient.setBackgroundResource(android.R.color.transparent);
        btnPatient.setTextColor(getColor(R.color.text_gray));
    }
}