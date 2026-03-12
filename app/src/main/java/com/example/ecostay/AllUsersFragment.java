package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AllUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoUsers;
    private DBHelper dbHelper;
    private UserAdminAdapter adapter;
    private int currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        dbHelper = new DBHelper(getContext());

        // Get current admin user ID to prevent self-deletion
        currentUserId = getActivity().getSharedPreferences("EcoStayPrefs", 0)
                .getInt("userId", -1);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvNoUsers = view.findViewById(R.id.tvNoUsers);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadUsers();

        return view;
    }

    private void loadUsers() {
        List<User> userList = dbHelper.getAllUsers();
        if (userList.isEmpty()) {
            tvNoUsers.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoUsers.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new UserAdminAdapter(userList, getContext(), currentUserId, new UserAdminAdapter.OnUserDeleteListener() {
                @Override
                public void onUserDelete(User user) {
                    deleteUser(user);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void deleteUser(final User user) {
        // Don't allow deleting yourself (admin)
        if (user.getId() == currentUserId) {
            Toast.makeText(getContext(), "You cannot delete your own account", Toast.LENGTH_SHORT).show();
            return;
        }

        // Don't allow deleting other admins
        if (user.isAdmin()) {
            Toast.makeText(getContext(), "Cannot delete other admin users", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete user " + user.getName() + "?\n\nThis will also delete all bookings made by this user.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete user from database
                    boolean deleted = dbHelper.deleteUser(user.getId());
                    if (deleted) {
                        Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                        loadUsers(); // Refresh the list
                    } else {
                        Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsers();
    }
}