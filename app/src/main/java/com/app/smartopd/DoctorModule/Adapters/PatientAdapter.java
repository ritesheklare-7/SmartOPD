package com.app.smartopd.DoctorModule.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<String> patientList;

    // Constructor — matches how DashboardFragment creates it:
    // PatientAdapter pa = new PatientAdapter(patientList);
    public PatientAdapter(List<String> patientList) {
        this.patientList = patientList;
    }

    // Call this to refresh list data without recreating adapter
    public void updateList(List<String> newList) {
        this.patientList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        String item = patientList.get(position);

        // Split "Token #23  —  Rahul Sharma" into two parts
        if (item.contains("—")) {
            String[] parts = item.split("—");
            holder.tvTokenBadge.setText(parts[0].trim());    // Token #23
            holder.tvPatientName.setText(parts[1].trim());   // Rahul Sharma
        } else {
            holder.tvTokenBadge.setText(item);
            holder.tvPatientName.setText("");
        }

        // Alternating row background for readability
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(0xFFF0F7FF); // light blue tint
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF); // white
        }
    }

    @Override
    public int getItemCount() {
        return patientList != null ? patientList.size() : 0;
    }

    // ── ViewHolder ────────────────────────────────────
    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView tvTokenBadge;   // shows "Token #23"
        TextView tvPatientName;  // shows "Rahul Sharma"
        TextView tvStatus;       // shows "Waiting"

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTokenBadge  = itemView.findViewById(R.id.tvTokenBadge);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvStatus      = itemView.findViewById(R.id.tvStatus);
        }
    }
}
