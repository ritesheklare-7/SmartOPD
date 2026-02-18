package com.app.smartopd.user_module.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.smartopd.R;

public class TokenFragment extends Fragment {

    private TextView txtTokenNumber, txtNowServing, txtEstimate;
    private ProgressBar progressQueue;

    public TokenFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);

        txtTokenNumber = view.findViewById(R.id.txtTokenNumber);
        txtNowServing = view.findViewById(R.id.txtNowServing);
        txtEstimate = view.findViewById(R.id.txtEstimate);
        progressQueue = view.findViewById(R.id.progressQueue);

        Button btnDirections = view.findViewById(R.id.btnDirections);
        Button btnReschedule = view.findViewById(R.id.btnReschedule);
        TextView txtRefresh = view.findViewById(R.id.txtRefresh);

        btnDirections.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Opening directions...", Toast.LENGTH_SHORT).show());

        btnReschedule.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Reschedule clicked", Toast.LENGTH_SHORT).show());

        txtRefresh.setOnClickListener(v -> refreshToken());

        return view;
    }

    /**
     * Dummy refresh logic
     * Replace with API / Firebase later
     */
    private void refreshToken() {
        txtTokenNumber.setText("#11");
        txtNowServing.setText("Now Serving: #09");
        txtEstimate.setText("Est. 10 mins remaining");
        progressQueue.setProgress(70);

        Toast.makeText(requireContext(),
                "Token status updated", Toast.LENGTH_SHORT).show();
    }
}
