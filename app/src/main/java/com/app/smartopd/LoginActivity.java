package com.app.smartopd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private AppCompatButton btnLogin;
    private TextView tvForgotPassword, tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // your XML file name

        // Initialize Views
        etEmail = findViewById(R.id.etloginEmail);
        etPassword = findViewById(R.id.etloginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        // LOGIN BUTTON CLICK
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // FORGOT PASSWORD
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this,
                    "Forgot Password Clicked",
                    Toast.LENGTH_SHORT).show();

            // startActivity(new Intent(LoginActivity.this,
            // ForgotPasswordActivity.class));
        });

        // SIGN UP CLICK
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,
                    RegistrationActivity.class));
        });
    }

    // ================= LOGIN LOGIC =================
    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
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
            etPassword.setError("Password must be 6+ characters");
            etPassword.requestFocus();
            return;
        }

        // ===== DEMO LOGIN (Replace with API/Firebase) =====
        if (email.equals("admin@gmail.com") &&
                password.equals("123456")) {

            Toast.makeText(this,
                    "Login Successful",
                    Toast.LENGTH_SHORT).show();

            // Go to Home Screen
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this,
                    "Invalid credentials",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
