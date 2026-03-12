package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView tvNoNotifications;
    private DBHelper dbHelper;
    private int userId;
    private NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        dbHelper = new DBHelper(getContext());
        userId = getActivity().getSharedPreferences("EcoStayPrefs", 0).getInt("userId", -1);

        initViews(view);
        loadNotifications();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvNoNotifications = view.findViewById(R.id.tvNoNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadNotifications() {
        if (userId != -1) {
            List<Notification> notifications = dbHelper.getUserNotifications(userId);
            if (notifications.isEmpty()) {
                tvNoNotifications.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvNoNotifications.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                notificationAdapter = new NotificationAdapter(notifications, getContext());
                recyclerView.setAdapter(notificationAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications(); // Refresh when returning to fragment
    }
}