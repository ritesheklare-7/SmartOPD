package com.app.smartopd.user_module.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;
import com.app.smartopd.user_module.Models.UserHomeModel;
import com.app.smartopd.user_module.utils.TokenManager;

import java.util.List;

public class UserHomeAdapter
        extends RecyclerView.Adapter<UserHomeAdapter.UserViewHolder> {

    private final List<UserHomeModel> userList;

    public UserHomeAdapter(List<UserHomeModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_home, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UserViewHolder holder, int position) {

        UserHomeModel model = userList.get(position);

        holder.txtName.setText(model.getName());
        holder.txtSpeciality.setText(model.getSpeciality());

        // STATUS LOGIC
        if (model.isSessionFull()) {
            holder.txtStatus.setText("● SESSION FULL");
            holder.txtStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.btnBook.setEnabled(false);
            holder.btnBook.setText("Session Full");

        } else if (model.isAvailable()) {
            holder.txtStatus.setText("● AVAILABLE");
            holder.txtStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.btnBook.setEnabled(true);
            holder.btnBook.setText("Book Token");

        } else {
            holder.txtStatus.setText("● ON LEAVE");
            holder.txtStatus.setTextColor(Color.RED);
            holder.btnBook.setEnabled(false);
            holder.btnBook.setText("Not Available");
        }

        // BOOK TOKEN CLICK
        holder.btnBook.setOnClickListener(v -> {

            TokenManager tokenManager = TokenManager.getInstance();

            if (tokenManager.hasToken()) {
                Toast.makeText(v.getContext(),
                        "You already have an active token",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            tokenManager.bookToken(
                    model.getName(),
                    model.getSpeciality()
            );

            Toast.makeText(v.getContext(),
                    "Token booked for " + model.getName(),
                    Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtSpeciality, txtStatus;
        Button btnBook;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSpeciality = itemView.findViewById(R.id.txtSpeciality);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
