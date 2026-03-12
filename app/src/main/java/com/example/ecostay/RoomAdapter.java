package com.example.ecostay;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;
    private DBHelper dbHelper;

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());
        holder.tvRoomType.setText(room.getType());
        holder.tvDescription.setText(room.getDescription());
        holder.tvPrice.setText(String.format("$%.2f per night", room.getPrice()));
        holder.tvCapacity.setText("Capacity: " + room.getCapacity() + " guests");
        holder.tvAmenities.setText("Amenities: " + room.getAmenities());

        loadRoomImage(holder.ivRoomImage, room.getImageUrl());

        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(room);
            }
        });
    }

    private void loadRoomImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
                    imageView.setImageURI(Uri.parse(imageUrl));
                } else {
                    int resourceId = context.getResources().getIdentifier(
                            imageUrl, "drawable", context.getPackageName());
                    if (resourceId != 0) {
                        imageView.setImageResource(resourceId);
                    } else {
                        imageView.setImageResource(R.drawable.ic_room_large);
                    }
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.ic_room_large);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_room_large);
        }
    }

    private void showBookingDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_room_booking, null);
        builder.setView(dialogView);

        TextView tvRoomName = dialogView.findViewById(R.id.tvRoomName);
        TextView tvRoomPrice = dialogView.findViewById(R.id.tvRoomPrice);
        TextView tvCheckIn = dialogView.findViewById(R.id.tvCheckIn);
        TextView tvCheckOut = dialogView.findViewById(R.id.tvCheckOut);
        Button btnSelectCheckIn = dialogView.findViewById(R.id.btnSelectCheckIn);
        Button btnSelectCheckOut = dialogView.findViewById(R.id.btnSelectCheckOut);
        Button btnConfirmBooking = dialogView.findViewById(R.id.btnConfirmBooking);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        tvRoomName.setText(room.getName());
        tvRoomPrice.setText(String.format("$%.2f per night", room.getPrice()));

        AlertDialog dialog = builder.create();

        final String[] checkInDate = {null};
        final String[] checkOutDate = {null};

        btnSelectCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Check-in Date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Date date = new Date(selection);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        checkInDate[0] = sdf.format(date);
                        tvCheckIn.setText("Check-in: " + checkInDate[0]);
                    }
                });

                datePicker.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btnSelectCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Check-out Date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Date date = new Date(selection);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        checkOutDate[0] = sdf.format(date);
                        tvCheckOut.setText("Check-out: " + checkOutDate[0]);
                    }
                });

                datePicker.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInDate[0] == null || checkOutDate[0] == null) {
                    Toast.makeText(context, "Please select both dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = context.getSharedPreferences("EcoStayPrefs", Context.MODE_PRIVATE)
                        .getInt("userId", -1);

                if (userId == -1) {
                    Toast.makeText(context, "Please login to book", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                // Calculate total price
                long diff = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date checkIn = sdf.parse(checkInDate[0]);
                    Date checkOut = sdf.parse(checkOutDate[0]);
                    diff = checkOut.getTime() - checkIn.getTime();
                    
                    if (diff < 0) {
                        Toast.makeText(context, "Check-out must be after check-in", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if (days == 0) days = 1; // Minimum 1 night stay
                    
                    double totalPrice = days * room.getPrice();

                    long result = dbHelper.bookRoom(userId, room.getId(), checkInDate[0],
                            checkOutDate[0], totalPrice);

                    if (result != -1) {
                        Toast.makeText(context, "Room booked successfully!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error processing booking", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRoomImage;
        TextView tvRoomName, tvRoomType, tvDescription, tvPrice, tvCapacity, tvAmenities;
        Button btnBookNow;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRoomImage = itemView.findViewById(R.id.ivRoomImage);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            tvAmenities = itemView.findViewById(R.id.tvAmenities);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}