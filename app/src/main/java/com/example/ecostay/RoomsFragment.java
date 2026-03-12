package com.example.ecostay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DBHelper dbHelper;
    private Spinner spinnerRoomType;
    private EditText etMaxPrice;
    private Button btnFilter, btnClearFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        dbHelper = new DBHelper(getContext());
        initViews(view);
        setupSpinner();
        loadRooms();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        spinnerRoomType = view.findViewById(R.id.spinnerRoomType);
        etMaxPrice = view.findViewById(R.id.etMaxPrice);
        btnFilter = view.findViewById(R.id.btnFilter);
        btnClearFilter = view.findViewById(R.id.btnClearFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, getContext());
        recyclerView.setAdapter(roomAdapter);
    }

    private void setupSpinner() {
        String[] roomTypes = {"All Types", "Cabin", "Eco-Pod", "Treehouse"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, roomTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoomType.setAdapter(adapter);
    }

    private void loadRooms() {
        roomList.clear();
        roomList.addAll(dbHelper.getAllRooms());
        roomAdapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });

        btnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerRoomType.setSelection(0);
                etMaxPrice.setText("");
                loadRooms();
            }
        });
    }

    private void applyFilter() {
        String selectedType = spinnerRoomType.getSelectedItem().toString();
        String maxPriceStr = etMaxPrice.getText().toString();

        String type = selectedType.equals("All Types") ? null : selectedType;
        double maxPrice = 0;

        if (!maxPriceStr.isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter a valid price", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        roomList.clear();
        roomList.addAll(dbHelper.filterRooms(type, maxPrice));
        roomAdapter.notifyDataSetChanged();
    }
}