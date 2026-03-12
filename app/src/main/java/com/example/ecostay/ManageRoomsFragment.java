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

public class ManageRoomsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoomAdminAdapter adapter;
    private List<Room> roomList;
    private DBHelper dbHelper;
    private static final String TAG = "ManageRoomsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_rooms, container, false);

        dbHelper = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRooms();

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FAB clicked");
                showAddEditDialog(null);
            }
        });

        return view;
    }

    private void loadRooms() {
        roomList = dbHelper.getAllRooms();
        adapter = new RoomAdminAdapter(roomList, getContext(),
                new RoomAdminAdapter.OnEditListener() {
                    @Override
                    public void onEdit(Room room) {
                        showAddEditDialog(room);
                    }
                },
                new RoomAdminAdapter.OnDeleteListener() {
                    @Override
                    public void onDelete(Room room) {
                        deleteRoom(room);
                    }
                });

        recyclerView.setAdapter(adapter);
    }

    private void showAddEditDialog(Room room) {
        try {
            AddEditRoomDialog dialog = AddEditRoomDialog.newInstance(room, new AddEditRoomDialog.OnRoomSavedListener() {
                @Override
                public void onRoomSaved() {
                    loadRooms();
                }
            });
            dialog.show(getParentFragmentManager(), "AddEditRoomDialog");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRoom(final Room room) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Room")
                .setMessage("Are you sure you want to delete " + room.getName() + "?")
                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        if (dbHelper.deleteRoom(room.getId())) {
                            Toast.makeText(getContext(), "Room deleted", Toast.LENGTH_SHORT).show();
                            loadRooms();
                        } else {
                            Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}