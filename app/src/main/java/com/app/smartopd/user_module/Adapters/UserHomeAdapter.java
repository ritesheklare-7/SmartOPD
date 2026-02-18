package com.app.smartopd.user_module.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartopd.R;
import com.app.smartopd.user_module.Models.UserHomeModel;

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
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_home, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UserViewHolder holder,
            int position
    ) {
        UserHomeModel model = userList.get(position);

        holder.txtName.setText(model.getName());
        holder.txtSpeciality.setText(model.getSpeciality());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtSpeciality;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSpeciality = itemView.findViewById(R.id.txtSpeciality);
        }
    }
}
