package com.app.smartopd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.app.smartopd.DoctorModule.DoctorHomeActivity;
import com.app.smartopd.user_module.HomeUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // ── UI ────────────────────────────────────────
    private EditText etEmail, etPassword;
    private AppCompatButton btnLogin;
    private TextView tvForgotPassword, tvSignUp;
    private ProgressBar progressBar;

    // ── Firebase ──────────────────────────────────
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ── Firebase Init ──
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // ── Bind Views FIRST (IMPORTANT) ──
        etEmail = findViewById(R.id.etloginEmail);
        etPassword = findViewById(R.id.etloginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        progressBar = findViewById(R.id.progressBar);

        // ── Auto Login ──
        if (mAuth.getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            checkRoleAndRedirect(mAuth.getCurrentUser().getUid());
            return;
        }

        // ── Login Button ──
        btnLogin.setOnClickListener(v -> loginUser());

        // ── Forgot Password ──
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        );

        // ── Sign Up ──
        tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrationActivity.class))
        );
    }

    // ─────────────────────────────────────────────
    // LOGIN USER
    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ── Validation ──
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // ── Show Loader ──
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // ── Firebase Login ──
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();
                    checkRoleAndRedirect(uid);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this,
                            "Login Failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    // ─────────────────────────────────────────────
    // CHECK ROLE AND REDIRECT
    private void checkRoleAndRedirect(String uid) {

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {

                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    if (!document.exists()) {
                        Toast.makeText(this,
                                "Account not found. Please register again.",
                                Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        return;
                    }

                    String role = document.getString("role");
                    String name = document.getString("name");

                    if ("doctor".equalsIgnoreCase(role)) {

                        Toast.makeText(this,
                                "Welcome Doctor 👨‍⚕️ " + name,
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, DoctorHomeActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("doctorName", name);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {

                        Toast.makeText(this,
                                "Welcome 👋 " + name,
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, HomeUser.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("patientName", name);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
