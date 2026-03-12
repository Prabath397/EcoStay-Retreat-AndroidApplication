package com.example.ecostay;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SendNotificationFragment extends Fragment implements NotificationAdminAdapter.OnDeleteClickListener {

    private EditText etTitle, etMessage;
    private Button btnSend;
    private RecyclerView rvSentNotifications;
    private DBHelper dbHelper;
    private NotificationAdminAdapter adapter;
    private List<Notification> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);

        dbHelper = new DBHelper(getContext());
        etTitle = view.findViewById(R.id.etTitle);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        rvSentNotifications = view.findViewById(R.id.rvSentNotifications);

        setupRecyclerView();
        loadNotifications();

        btnSend.setOnClickListener(v -> sendNotification());

        return view;
    }

    private void setupRecyclerView() {
        rvSentNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadNotifications() {
        notificationList = dbHelper.getAllNotifications();
        adapter = new NotificationAdminAdapter(notificationList, getContext(), this);
        rvSentNotifications.setAdapter(adapter);
    }

    private void sendNotification() {
        String title = etTitle.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Please enter both title and message", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.sendNotificationToAllUsers(title, message);
        Toast.makeText(getContext(), "Notification sent to all users", Toast.LENGTH_LONG).show();
        etTitle.setText("");
        etMessage.setText("");
        
        // Refresh the list after sending
        loadNotifications();
    }

    @Override
    public void onDeleteClick(Notification notification) {
        if (dbHelper.deleteNotification(notification.getId())) {
            Toast.makeText(getContext(), "Notification deleted", Toast.LENGTH_SHORT).show();
            // Refresh the list to show the change
            loadNotifications();
        } else {
            Toast.makeText(getContext(), "Failed to delete notification", Toast.LENGTH_SHORT).show();
        }
    }
}