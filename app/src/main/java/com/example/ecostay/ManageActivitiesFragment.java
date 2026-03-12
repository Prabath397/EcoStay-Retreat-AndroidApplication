package com.example.ecostay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageActivitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActivityAdminAdapter adapter;
    private List<Activity> activityList;
    private DBHelper dbHelper;
    private static final String TAG = "ManageActivitiesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_activities, container, false);

        dbHelper = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadActivities();

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FAB clicked - opening add dialog");
                showAddEditDialog(null);
            }
        });

        return view;
    }

    private void loadActivities() {
        Log.d(TAG, "Loading activities from database");
        activityList = dbHelper.getAllActivities();
        Log.d(TAG, "Loaded " + activityList.size() + " activities");

        adapter = new ActivityAdminAdapter(activityList, getContext(),
                new ActivityAdminAdapter.OnEditListener() {
                    @Override
                    public void onEdit(Activity activity) {
                        Log.d(TAG, "Edit clicked for activity: " + activity.getName());
                        showAddEditDialog(activity);
                    }
                },
                new ActivityAdminAdapter.OnDeleteListener() {
                    @Override
                    public void onDelete(Activity activity) {
                        Log.d(TAG, "Delete clicked for activity: " + activity.getName());
                        deleteActivity(activity);
                    }
                });

        recyclerView.setAdapter(adapter);
    }

    private void showAddEditDialog(Activity activity) {
        try {
            AddEditActivityDialog dialog = AddEditActivityDialog.newInstance(activity,
                    new AddEditActivityDialog.OnActivitySavedListener() {
                        @Override
                        public void onActivitySaved() {
                            Log.d(TAG, "Dialog saved, reloading activities");
                            loadActivities();
                        }
                    });
            dialog.show(getParentFragmentManager(), "AddEditActivityDialog");
        } catch (Exception e) {
            Log.e(TAG, "Error showing dialog: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteActivity(final Activity activity) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Activity")
                .setMessage("Are you sure you want to delete " + activity.getName() + "?")
                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        boolean success = dbHelper.deleteActivity(activity.getId());
                        if (success) {
                            Log.d(TAG, "Activity deleted successfully: " + activity.getName());
                            Toast.makeText(getContext(), "Activity deleted", Toast.LENGTH_SHORT).show();
                            loadActivities();
                        } else {
                            Log.e(TAG, "Failed to delete activity: " + activity.getName());
                            Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment resumed, reloading activities");
        loadActivities();
    }
}