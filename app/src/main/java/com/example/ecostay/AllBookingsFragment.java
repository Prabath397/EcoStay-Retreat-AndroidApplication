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

public class AllBookingsFragment extends Fragment {

    private RecyclerView recyclerViewRooms, recyclerViewActivities;
    private TextView tvNoRooms, tvNoActivities;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_bookings, container, false);

        dbHelper = new DBHelper(getContext());

        recyclerViewRooms = view.findViewById(R.id.recyclerViewRooms);
        recyclerViewActivities = view.findViewById(R.id.recyclerViewActivities);
        tvNoRooms = view.findViewById(R.id.tvNoRooms);
        tvNoActivities = view.findViewById(R.id.tvNoActivities);

        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBookings();

        return view;
    }

    private void loadBookings() {
        // Room bookings
        List<RoomBooking> roomBookings = dbHelper.getAllRoomBookings();
        if (roomBookings.isEmpty()) {
            tvNoRooms.setVisibility(View.VISIBLE);
            recyclerViewRooms.setVisibility(View.GONE);
        } else {
            tvNoRooms.setVisibility(View.GONE);
            recyclerViewRooms.setVisibility(View.VISIBLE);
            recyclerViewRooms.setAdapter(new RoomBookingAdminAdapter(roomBookings, getContext()));
        }

        // Activity bookings
        List<ActivityBooking> activityBookings = dbHelper.getAllActivityBookings();
        if (activityBookings.isEmpty()) {
            tvNoActivities.setVisibility(View.VISIBLE);
            recyclerViewActivities.setVisibility(View.GONE);
        } else {
            tvNoActivities.setVisibility(View.GONE);
            recyclerViewActivities.setVisibility(View.VISIBLE);
            recyclerViewActivities.setAdapter(new ActivityBookingAdminAdapter(activityBookings, getContext()));
        }
    }
}