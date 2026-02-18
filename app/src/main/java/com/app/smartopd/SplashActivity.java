package com.app.smartopd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartopd.user_module.HomeUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, Registration.class);
                startActivity(intent);
                finish();

            }
        }, 3000); // 3 seconds delay
    }

//        decideNextScreen();
//    }

//    private void decideNextScreen() {
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user == null) {
//            open(Intro_Activity.class);
//            return;
//        }
//
//        FirebaseFirestore.getInstance()
//                .collection("users")
//                .document(user.getUid())
//                .get()
//                .addOnSuccessListener(snapshot -> {
//
//                    if (!snapshot.exists()) {
//                        FirebaseAuth.getInstance().signOut();
//                        open(Intro_Activity.class);
//                        return;
//                    }
//
//                    String role = snapshot.getString("role");
//
//                    if ("admin".equals(role)) {
//                        open(AdminActivity.class);
//                    }
//                    else {
//                        open(HomeActivity.class);
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    open(Intro_Activity.class);
//                });
//    }
//
//    private void open(Class<?> target) {
//        Intent intent = new Intent(this, target);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void onBackPressed() {
//    }
    }
