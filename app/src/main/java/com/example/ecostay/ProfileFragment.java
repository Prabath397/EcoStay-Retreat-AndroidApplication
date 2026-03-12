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

public class ProfileFragment extends Fragment {
    private EditText etName, etEmail, etPhone, etPreferences;
    private Button btnUpdateProfile;
    private DBHelper dbHelper;
    private int userId;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DBHelper(getContext());
        userId = getActivity().getSharedPreferences("EcoStayPrefs", 0).getInt("userId", -1);

        initViews(view);
        loadUserData();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etPreferences = view.findViewById(R.id.etPreferences);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
    }

    private void loadUserData() {
        if (userId != -1) {
            user = dbHelper.getUser(userId);
            if (user != null) {
                etName.setText(user.getName());
                etEmail.setText(user.getEmail());
                etPhone.setText(user.getPhone());
                etPreferences.setText(user.getPreferences());
            }
        }
    }

    private void setupListeners() {
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String preferences = etPreferences.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
            return;
        }

        user.setName(name);
        user.setPhone(phone);
        user.setPreferences(preferences);

        boolean updated = dbHelper.updateUser(user);
        if (updated) {
            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            
            // Fix: Notify MainActivity to refresh the navigation header name
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateNavHeader();
            }
            
        } else {
            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}