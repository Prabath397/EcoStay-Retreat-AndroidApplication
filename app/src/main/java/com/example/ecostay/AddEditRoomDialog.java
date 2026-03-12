package com.example.ecostay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddEditRoomDialog extends DialogFragment {

    private Context context;
    private Room room;
    private OnRoomSavedListener listener;
    private EditText etName, etDescription, etPrice, etCapacity, etAmenities;
    private Spinner spinnerType;
    private ImageView ivRoomImagePreview;
    private Button btnSelectImage;
    private DBHelper dbHelper;
    private Uri selectedImageUri;

    public interface OnRoomSavedListener {
        void onRoomSaved();
    }

    public AddEditRoomDialog() {
    }

    public static AddEditRoomDialog newInstance(Room room, OnRoomSavedListener listener) {
        AddEditRoomDialog dialog = new AddEditRoomDialog();
        dialog.room = room;
        dialog.listener = listener;
        return dialog;
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            context.getContentResolver().takePersistableUriPermission(selectedImageUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ivRoomImagePreview.setImageURI(selectedImageUri);
                    }
                }
            });

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
        View view = inflater.inflate(R.layout.dialog_add_edit_room, null);
        builder.setView(view);

        etName = view.findViewById(R.id.etName);
        spinnerType = view.findViewById(R.id.spinnerType);
        etDescription = view.findViewById(R.id.etDescription);
        etPrice = view.findViewById(R.id.etPrice);
        etCapacity = view.findViewById(R.id.etCapacity);
        etAmenities = view.findViewById(R.id.etAmenities);
        ivRoomImagePreview = view.findViewById(R.id.ivRoomImagePreview);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.room_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        if (room != null) {
            builder.setTitle("Edit Room");
            etName.setText(room.getName());
            
            String[] types = context.getResources().getStringArray(R.array.room_types);
            for (int i = 0; i < types.length; i++) {
                if (types[i].equalsIgnoreCase(room.getType())) {
                    spinnerType.setSelection(i);
                    break;
                }
            }
            
            etDescription.setText(room.getDescription());
            etPrice.setText(String.valueOf(room.getPrice()));
            etCapacity.setText(String.valueOf(room.getCapacity()));
            etAmenities.setText(room.getAmenities());
            loadPreviewImage(room.getImageUrl());
        } else {
            builder.setTitle("Add New Room");
        }

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveRoom());
        btnCancel.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    private void loadPreviewImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            ivRoomImagePreview.setImageResource(R.drawable.ic_room_large);
            return;
        }
        
        try {
            if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
                ivRoomImagePreview.setImageURI(Uri.parse(imageUrl));
            } else {
                int resId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
                if (resId != 0) {
                    ivRoomImagePreview.setImageResource(resId);
                } else {
                    ivRoomImagePreview.setImageResource(R.drawable.ic_room_large);
                }
            }
        } catch (Exception e) {
            ivRoomImagePreview.setImageResource(R.drawable.ic_room_large);
        }
    }

    private void saveRoom() {
        String name = etName.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String capacityStr = etCapacity.getText().toString().trim();
        String amenities = etAmenities.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(capacityStr)) {
            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // New validation: Ensure an image is selected for new rooms
        if (room == null && selectedImageUri == null) {
            Toast.makeText(context, "Please select an image for the new room", Toast.LENGTH_SHORT).show();
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

        // Removed the "cabin" fallback. If no new image is selected during edit, keep the old one.
        String finalImageUrl = (selectedImageUri != null) ? selectedImageUri.toString() : (room != null ? room.getImageUrl() : "");

        if (room == null) {
            Room newRoom = new Room(0, name, type, description, price, true, capacity, finalImageUrl, amenities);
            if (dbHelper.addRoom(newRoom)) {
                if (listener != null) listener.onRoomSaved();
                dismiss();
            } else {
                Toast.makeText(context, "Error adding room", Toast.LENGTH_SHORT).show();
            }
        } else {
            room.setName(name);
            room.setType(type);
            room.setDescription(description);
            room.setPrice(price);
            room.setCapacity(capacity);
            room.setAmenities(amenities);
            room.setImageUrl(finalImageUrl);
            if (dbHelper.updateRoom(room)) {
                if (listener != null) listener.onRoomSaved();
                dismiss();
            } else {
                Toast.makeText(context, "Error updating room", Toast.LENGTH_SHORT).show();
            }
        }
    }
}