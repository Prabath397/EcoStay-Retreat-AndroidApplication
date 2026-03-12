package com.example.ecostay;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddEditActivityDialog extends DialogFragment {

    private Context context;
    private Activity activity;
    private OnActivitySavedListener listener;
    private EditText etName, etDescription, etPrice, etCapacity, etDuration, etGuide;
    private Spinner spinnerType;
    private DBHelper dbHelper;

    public interface OnActivitySavedListener {
        void onActivitySaved();
    }

    public static AddEditActivityDialog newInstance(Activity activity, OnActivitySavedListener listener) {
        AddEditActivityDialog dialog = new AddEditActivityDialog();
        dialog.activity = activity;
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_edit_activity, null);
        builder.setView(view);

        // Initialize views
        etName = view.findViewById(R.id.etName);
        spinnerType = view.findViewById(R.id.spinnerType);
        etDescription = view.findViewById(R.id.etDescription);
        etPrice = view.findViewById(R.id.etPrice);
        etCapacity = view.findViewById(R.id.etCapacity);
        etDuration = view.findViewById(R.id.etDuration);
        etGuide = view.findViewById(R.id.etGuide);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Setup spinner with activity types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Set title and populate fields if editing
        if (activity != null) {
            builder.setTitle("Edit Activity");
            etName.setText(activity.getName());

            String[] types = context.getResources().getStringArray(R.array.activity_types);
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(activity.getType())) {
                    spinnerType.setSelection(i);
                    break;
                }
            }

            etDescription.setText(activity.getDescription());
            etPrice.setText(String.valueOf(activity.getPrice()));
            etCapacity.setText(String.valueOf(activity.getCapacity()));
            etDuration.setText(activity.getDuration());
            etGuide.setText(activity.getGuideName());
        } else {
            builder.setTitle("Add New Activity");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void saveActivity() {
        String name = etName.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String capacityStr = etCapacity.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        String guide = etGuide.getText().toString().trim();

        // Validate required fields
        if (TextUtils.isEmpty(name)) {
            etName.setError("Activity name is required");
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            etPrice.setError("Price is required");
            return;
        }

        if (TextUtils.isEmpty(capacityStr)) {
            etCapacity.setError("Capacity is required");
            return;
        }

        if (TextUtils.isEmpty(duration)) {
            etDuration.setError("Duration is required");
            return;
        }

        double price;
        int capacity;
        try {
            price = Double.parseDouble(priceStr);
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (activity == null) {
            // Add new activity
            Activity newActivity = new Activity();
            newActivity.setName(name);
            newActivity.setType(type);
            newActivity.setDescription(description);
            newActivity.setPrice(price);
            newActivity.setCapacity(capacity);
            newActivity.setDuration(duration);
            newActivity.setGuideName(guide);
            newActivity.setAvailable(true);

            if (dbHelper.addActivity(newActivity)) {
                Toast.makeText(context, "Activity added successfully", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onActivitySaved();
                dismiss();
            } else {
                Toast.makeText(context, "Failed to add activity", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing activity
            activity.setName(name);
            activity.setType(type);
            activity.setDescription(description);
            activity.setPrice(price);
            activity.setCapacity(capacity);
            activity.setDuration(duration);
            activity.setGuideName(guide);

            if (dbHelper.updateActivity(activity)) {
                Toast.makeText(context, "Activity updated successfully", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onActivitySaved();
                dismiss();
            } else {
                Toast.makeText(context, "Failed to update activity", Toast.LENGTH_SHORT).show();
            }
        }
    }
}