package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class AdminDashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        Button btnManageRooms = view.findViewById(R.id.btnManageRooms);
        Button btnManageActivities = view.findViewById(R.id.btnManageActivities);
        Button btnAllBookings = view.findViewById(R.id.btnAllBookings);
        Button btnSendNotification = view.findViewById(R.id.btnSendNotification);

        btnManageRooms.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ManageRoomsFragment())
                        .addToBackStack(null)
                        .commit());

        btnManageActivities.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ManageActivitiesFragment())
                        .addToBackStack(null)
                        .commit());

        btnAllBookings.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AllBookingsFragment())
                        .addToBackStack(null)
                        .commit());

        btnSendNotification.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SendNotificationFragment())
                        .addToBackStack(null)
                        .commit());

        return view;
    }
}