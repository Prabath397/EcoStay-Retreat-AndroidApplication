package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
// Remove these if not used:
// import androidx.viewpager2.widget.ViewPager2;
// import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    // Comment out these if not used in your layout
    // private ViewPager2 viewPager;
    // private TabLayout tabLayout;

    private TextView tvWelcome;
    private Button btnExploreRooms, btnExploreActivities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupWelcomeMessage();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        // Comment out if these IDs don't exist in your layout
        // viewPager = view.findViewById(R.id.viewPager);
        // tabLayout = view.findViewById(R.id.tabLayout);

        tvWelcome = view.findViewById(R.id.tvWelcome);
        btnExploreRooms = view.findViewById(R.id.btnExploreRooms);
        btnExploreActivities = view.findViewById(R.id.btnExploreActivities);
    }

    private void setupWelcomeMessage() {
        try {
            int userId = getActivity().getSharedPreferences("EcoStayPrefs", 0)
                    .getInt("userId", -1);

            if (userId != -1) {
                DBHelper dbHelper = new DBHelper(getContext());
                User user = dbHelper.getUser(userId);
                if (user != null && tvWelcome != null) {
                    tvWelcome.setText("Welcome back, " + user.getName() + "!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupButtons() {
        if (btnExploreRooms != null) {
            btnExploreRooms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new RoomsFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (btnExploreActivities != null) {
            btnExploreActivities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ActivitiesFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}