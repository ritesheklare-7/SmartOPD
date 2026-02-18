package com.app.smartopd.user_module;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.app.smartopd.R;
import com.app.smartopd.user_module.Fragments.HomeFragment;
import com.app.smartopd.user_module.Fragments.ProfileFragment;
import com.app.smartopd.user_module.Fragments.TokenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeUser extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    Fragment homeFragment;
    Fragment tokenFragment;
    Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_user);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.homeRoot),   // ✅ FIXED HERE
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );

        bottomNavigation = findViewById(R.id.bottomNavigation);

        homeFragment = new HomeFragment();
        tokenFragment = new TokenFragment();
        profileFragment = new ProfileFragment();

        loadFragment(homeFragment);

        bottomNavigation.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home) {
                loadFragment(homeFragment);
                return true;
            }

            if (item.getItemId() == R.id.nav_token) {
                loadFragment(tokenFragment);
                return true;
            }

            if (item.getItemId() == R.id.nav_profile) {
                loadFragment(profileFragment);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
