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
import com.app.smartopd.user_module.utils.TokenManager;

public class TokenFragment extends Fragment {

    private TextView txtTokenNumber, txtNowServing, txtEstimate, txtStatus;
    private ProgressBar progressQueue;

    private TokenManager tokenManager;

    public TokenFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);

        txtTokenNumber = view.findViewById(R.id.txtTokenNumber);
        txtNowServing  = view.findViewById(R.id.txtNowServing);
        txtEstimate    = view.findViewById(R.id.txtEstimate);
        txtStatus      = view.findViewById(R.id.txtStatus);
        progressQueue  = view.findViewById(R.id.progressQueue);

        Button btnDirections = view.findViewById(R.id.btnDirections);
        Button btnReschedule = view.findViewById(R.id.btnReschedule);
        TextView txtRefresh  = view.findViewById(R.id.txtRefresh);

        tokenManager = TokenManager.getInstance();

        btnDirections.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Opening directions...", Toast.LENGTH_SHORT).show());

        btnReschedule.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Reschedule coming soon", Toast.LENGTH_SHORT).show());

        txtRefresh.setOnClickListener(v -> {
            tokenManager.advanceQueue();
            loadToken();
        });

        loadToken();

        return view;
    }

    private void loadToken() {

        if (!tokenManager.hasToken()) {
            showNoToken();
            return;
        }

        int myToken = tokenManager.getMyToken();
        int nowServing = tokenManager.getNowServing();

        txtTokenNumber.setText("#" + myToken);
        txtNowServing.setText("Now Serving: #" + nowServing);

        int remaining = Math.max(0, myToken - nowServing);
        txtEstimate.setText("Est. " + (remaining * 5) + " mins remaining");

        progressQueue.setMax(myToken);
        progressQueue.setProgress(nowServing);

        txtStatus.setText("ONGOING");
        txtStatus.setTextColor(getResources().getColor(R.color.accent_green));
    }


    private void showNoToken() {
        txtTokenNumber.setText("--");
        txtNowServing.setText("No Active Token");
        txtEstimate.setText("Book a token to get started");
        progressQueue.setProgress(0);
        txtStatus.setText("NO TOKEN");
        txtStatus.setTextColor(getResources().getColor(R.color.gray));
    }
}
