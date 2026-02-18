package com.app.smartopd.user_module.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;

import java.util.ArrayList;
import java.util.List;

public class EmptyHistoryAdapter extends RecyclerView.Adapter<EmptyHistoryAdapter.HistoryViewHolder> {

    private final List<String> historyList;

    public EmptyHistoryAdapter() {
        historyList = new ArrayList<>();

        // Dummy data (matches UI preview)
        historyList.add("City General Hospital  •  Token #42  •  DONE");
        historyList.add("Smile Dental Clinic  •  Token #09  •  DONE");
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_token_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HistoryViewHolder holder,
            int position
    ) {
        holder.txtHistory.setText(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtHistory;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHistory = itemView.findViewById(R.id.txtHistory);
        }
    }
}
