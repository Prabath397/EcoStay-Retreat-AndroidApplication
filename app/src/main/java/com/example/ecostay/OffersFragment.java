package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OffersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OfferAdapter offerAdapter;
    private List<String> allOffers;
    private DBHelper dbHelper;
    private int userId;

    // Special offers data
    private String[] ecoEvents = {
            "🌿 Earth Day Celebration - 20% off all activities",
            "🌕 Full Moon Nature Walk - This Saturday",
            "🌱 Organic Farming Workshop - Next Weekend",
            "♻️ Sustainability Summit - Free entry for guests",
            "🦅 Bird Watching Tour - Special morning session"
    };

    private String[] discountOffers = {
            "💰 Early Bird Special: 15% off morning hikes",
            "💰 Stay 3 nights, get 1 free eco-activity",
            "💰 Family Package: 25% off for groups of 4+",
            "💰 Weekday Retreat: 30% off Mon-Thu bookings",
            "💰 Refer a friend: Both get 10% off your next stay"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        dbHelper = new DBHelper(getContext());
        userId = getActivity().getSharedPreferences("EcoStayPrefs", 0).getInt("userId", -1);

        initViews(view);
        loadOffers();
        setupListeners(view);

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadOffers() {
        // Combine all offers
        allOffers = new java.util.ArrayList<>();
        for (String event : ecoEvents) {
            allOffers.add(event);
        }
        for (String discount : discountOffers) {
            allOffers.add(discount);
        }

        offerAdapter = new OfferAdapter(allOffers, getContext());
        recyclerView.setAdapter(offerAdapter);
    }

    private void setupListeners(View view) {
        Button btnNotifyMe = view.findViewById(R.id.btnNotifyMe);
        btnNotifyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    // Send notification about new offers
                    dbHelper.createNotification(userId,
                            "🎉 New Eco Events This Week",
                            "Check out our Earth Day Celebration and Nature Walk events! Visit Offers section for details.");

                    Toast.makeText(getContext(),
                            "✅ You'll be notified about new eco-events!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                            "Please login to receive notifications",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}