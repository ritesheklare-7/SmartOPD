package com.app.smartopd.user_module.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;
import com.app.smartopd.user_module.Adapters.EmptyHistoryAdapter;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView btnBack = view.findViewById(R.id.btnBack);
        ImageView btnSettings = view.findViewById(R.id.btnSettings);
        RecyclerView recyclerHistory = view.findViewById(R.id.recyclerHistory);

        recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Dummy adapter placeholder (connect Firebase/API later)
        recyclerHistory.setAdapter(new EmptyHistoryAdapter());

        btnBack.setOnClickListener(v ->
                requireActivity().onBackPressed()
        );

        btnSettings.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Settings clicked", Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
