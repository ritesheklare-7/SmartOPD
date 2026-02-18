package com.app.smartopd.DoctorModule;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.app.smartopd.DoctorModule.Fragments.DashboardFragment;
import com.app.smartopd.DoctorModule.Fragments.LeaveFragment;
import com.app.smartopd.DoctorModule.Fragments.SessionFragment;
import com.app.smartopd.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DoctorHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        bottomNav = findViewById(R.id.bottomNav);

        // Load Dashboard by default
        loadFragment(new DashboardFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());
            } else if (id == R.id.nav_session) {
                loadFragment(new SessionFragment());
            } else if (id == R.id.nav_leave) {
                loadFragment(new LeaveFragment());
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}