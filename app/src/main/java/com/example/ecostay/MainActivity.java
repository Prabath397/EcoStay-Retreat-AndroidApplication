package com.example.ecostay;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvUserName, tvUserEmail;
    private DBHelper dbHelper;
    private int userId;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        userId = getSharedPreferences("EcoStayPrefs", MODE_PRIVATE).getInt("userId", -1);
        isAdmin = getSharedPreferences("EcoStayPrefs", MODE_PRIVATE).getBoolean("isAdmin", false);

        initViews();
        updateNavHeader(); // Use the new public method
        setupDrawer();

        // Load the appropriate menu based on user role
        if (isAdmin) {
            navigationView.inflateMenu(R.menu.navigation_menu_admin);
        } else {
            navigationView.inflateMenu(R.menu.navigation_menu_guest);
        }

        // Load default fragment
        if (savedInstanceState == null) {
            Fragment defaultFragment;
            int defaultItem;

            if (isAdmin) {
                defaultFragment = new AdminDashboardFragment();
                defaultItem = R.id.nav_admin_dashboard;
            } else {
                defaultFragment = new HomeFragment();
                defaultItem = R.id.nav_home;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, defaultFragment)
                    .commit();
            
            navigationView.setCheckedItem(defaultItem);
        }
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // New public method to be called from ProfileFragment
    public void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.tvUserName);
        tvUserEmail = headerView.findViewById(R.id.tvUserEmail);

        if (userId != -1) {
            User user = dbHelper.getUser(userId);
            if (user != null) {
                tvUserName.setText(user.getName());
                tvUserEmail.setText(user.getEmail());
            }
        }
    }

    private void setupDrawer() {
        ImageView ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();

        // Guest menu items
        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_rooms) {
            selectedFragment = new RoomsFragment();
        } else if (itemId == R.id.nav_activities) {
            selectedFragment = new ActivitiesFragment();
        } else if (itemId == R.id.nav_bookings) {
            selectedFragment = new BookingsFragment();
        } else if (itemId == R.id.nav_notifications) {
            selectedFragment = new NotificationsFragment();
        } else if (itemId == R.id.nav_offers) {
            selectedFragment = new OffersFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        } else if (itemId == R.id.nav_about) {
            selectedFragment = new AboutFragment();
        }
        // Admin menu items
        else if (itemId == R.id.nav_admin_dashboard) {
            selectedFragment = new AdminDashboardFragment();
        } else if (itemId == R.id.nav_manage_rooms) {
            selectedFragment = new ManageRoomsFragment();
        } else if (itemId == R.id.nav_manage_activities) {
            selectedFragment = new ManageActivitiesFragment();
        } else if (itemId == R.id.nav_all_bookings) {
            selectedFragment = new AllBookingsFragment();
        } else if (itemId == R.id.nav_send_notification) {
            selectedFragment = new SendNotificationFragment();
        } else if (itemId == R.id.nav_all_users) {
            selectedFragment = new AllUsersFragment();
        } else if (itemId == R.id.nav_logout) {
            logout();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        getSharedPreferences("EcoStayPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}