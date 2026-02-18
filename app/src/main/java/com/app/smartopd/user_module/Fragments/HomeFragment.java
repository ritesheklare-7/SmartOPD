package com.app.smartopd.user_module.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;
import com.app.smartopd.user_module.Adapters.UserHomeAdapter;
import com.app.smartopd.user_module.Models.UserHomeModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerDoctors;
    private SearchView searchDoctors;
    private ChipGroup chipGroup;

    // USER MODULE adapter & model
    private UserHomeAdapter userHomeAdapter;
    private final List<UserHomeModel> fullList = new ArrayList<>();
    private final List<UserHomeModel> filteredList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Bind views
        recyclerDoctors = view.findViewById(R.id.recyclerDoctors);
        searchDoctors = view.findViewById(R.id.searchDoctors);
        chipGroup = view.findViewById(R.id.chipGroup);

        // RecyclerView setup
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load data
        loadHomeData();

        filteredList.addAll(fullList);

        userHomeAdapter = new UserHomeAdapter(filteredList);
        recyclerDoctors.setAdapter(userHomeAdapter);

        setupSearch();
        setupChipFilter();

        return view;
    }

    /**
     * Load User Home data
     * Replace later with API / Firebase
     */
    private void loadHomeData() {
        fullList.clear();

        fullList.add(new UserHomeModel(
                "Dr. Aris Thorne", "Cardiology", true, false));

        fullList.add(new UserHomeModel(
                "Dr. Sarah Jenkins", "Pediatrics", false, false));

        fullList.add(new UserHomeModel(
                "Dr. Marcus Lee", "Neurology", true, true));

        fullList.add(new UserHomeModel(
                "Dr. Elena Rodriguez", "Dermatology", true, false));
    }

    /**
     * Search filtering
     */
    private void setupSearch() {
        searchDoctors.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query, getSelectedChipText());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText, getSelectedChipText());
                return true;
            }
        });
    }

    /**
     * Chip filter
     */
    private void setupChipFilter() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) ->
                filterData(searchDoctors.getQuery().toString(), getSelectedChipText())
        );
    }

    /**
     * Core filter logic
     */
    private void filterData(String query, String category) {
        filteredList.clear();

        for (UserHomeModel item : fullList) {

            boolean matchesSearch =
                    TextUtils.isEmpty(query)
                            || item.getName().toLowerCase().contains(query.toLowerCase())
                            || item.getSpeciality().toLowerCase().contains(query.toLowerCase());

            boolean matchesCategory =
                    category.equals("All")
                            || item.getSpeciality().equalsIgnoreCase(category);

            if (matchesSearch && matchesCategory) {
                filteredList.add(item);
            }
        }

        userHomeAdapter.notifyDataSetChanged();
    }

    /**
     * Get selected chip text
     */
    private String getSelectedChipText() {
        int checkedId = chipGroup.getCheckedChipId();
        if (checkedId != View.NO_ID) {
            Chip chip = chipGroup.findViewById(checkedId);
            return chip.getText().toString();
        }
        return "All";
    }
}
