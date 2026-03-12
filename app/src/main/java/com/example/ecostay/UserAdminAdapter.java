package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;
    private int currentUserId;
    private OnUserDeleteListener deleteListener;

    public interface OnUserDeleteListener {
        void onUserDelete(User user);
    }

    public UserAdminAdapter(List<User> userList, Context context, int currentUserId, OnUserDeleteListener deleteListener) {
        this.userList = userList;
        this.context = context;
        this.currentUserId = currentUserId;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvName.setText("Name: " + user.getName());
        holder.tvEmail.setText("Email: " + user.getEmail());
        holder.tvPhone.setText("Phone: " + user.getPhone());
        holder.tvPreferences.setText("Preferences: " + user.getPreferences());

        // Show admin badge if user is admin
        if (user.isAdmin()) {
            holder.tvAdminBadge.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.GONE); // Hide delete button for admins
        } else {
            holder.tvAdminBadge.setVisibility(View.GONE);

            // Hide delete button for current user (can't delete yourself)
            if (user.getId() == currentUserId) {
                holder.btnDelete.setVisibility(View.GONE);
            } else {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(v -> deleteListener.onUserDelete(user));
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvPreferences, tvAdminBadge;
        Button btnDelete;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPreferences = itemView.findViewById(R.id.tvPreferences);
            tvAdminBadge = itemView.findViewById(R.id.tvAdminBadge);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}