package com.app.smartopd;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartopd.LoginActivity;
import com.app.smartopd.R;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword;
    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;

    private String userRole = "Patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        TextView btnPatient = findViewById(R.id.btnPatient);
        TextView btnDoctor = findViewById(R.id.btnDoctor);

        // Toggle Role
        btnPatient.setOnClickListener(v -> {
            userRole = "Patient";
            btnPatient.setBackgroundResource(R.drawable.bg_toggle_selected);
            btnDoctor.setBackground(null);
        });

        btnDoctor.setOnClickListener(v -> {
            userRole = "Doctor";
            btnDoctor.setBackgroundResource(R.drawable.bg_toggle_selected);
            btnPatient.setBackground(null);
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> registerUser());

        findViewById(R.id.tvLogin).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) ||
                password.length() < 8) {

            Toast.makeText(this,
                    "Fill all fields correctly",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String uid = mAuth.getCurrentUser().getUid();

                    HashMap<String,Object> user = new HashMap<>();
                    user.put("uid", uid);
                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("role", userRole);

//                    db.collection("users")
//                            .document(uid)
//                            .set(user);
//
//                    Toast.makeText(this,
//                            "Registration Successful",
//                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }
}
