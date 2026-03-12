package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activityList;
    private Context context;
    private DBHelper dbHelper;

    public ActivityAdapter(List<Activity> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);

        holder.tvActivityName.setText(activity.getName());
        holder.tvActivityType.setText(activity.getType());
        holder.tvDescription.setText(activity.getDescription());
        holder.tvPrice.setText(String.format("$%.2f per person", activity.getPrice()));
        holder.tvDuration.setText("Duration: " + activity.getDuration());
        holder.tvGuideName.setText("Guide: " + activity.getGuideName());
        holder.tvCapacity.setText("Max: " + activity.getCapacity() + " people");

        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(activity);
            }
        });
    }

    private void showBookingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_activity_booking, null);
        builder.setView(dialogView);

        TextView tvActivityName = dialogView.findViewById(R.id.tvActivityName);
        TextView tvActivityPrice = dialogView.findViewById(R.id.tvActivityPrice);
        TextView tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        TextView tvAvailableSpots = dialogView.findViewById(R.id.tvAvailableSpots);
        EditText etParticipants = dialogView.findViewById(R.id.etParticipants);
        Button btnSelectDate = dialogView.findViewById(R.id.btnSelectDate);
        Button btnConfirmBooking = dialogView.findViewById(R.id.btnConfirmBooking);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        tvActivityName.setText(activity.getName());
        tvActivityPrice.setText(String.format("$%.2f per person", activity.getPrice()));

        AlertDialog dialog = builder.create();

        final String[] selectedDate = {null};
        final int[] availableSpots = {activity.getCapacity()};

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Activity Date")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Date date = new Date(selection);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate[0] = sdf.format(date);
                        tvSelectedDate.setText("Selected Date: " + selectedDate[0]);

                        checkAvailability(activity.getId(), selectedDate[0], tvAvailableSpots, availableSpots);
                    }
                });

                datePicker.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate[0] == null) {
                    Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                String participantsStr = etParticipants.getText().toString().trim();
                if (participantsStr.isEmpty()) {
                    Toast.makeText(context, "Please enter number of participants", Toast.LENGTH_SHORT).show();
                    return;
                }

                int participants = Integer.parseInt(participantsStr);
                if (participants <= 0) {
                    Toast.makeText(context, "Participants must be at least 1", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (participants > availableSpots[0]) {
                    Toast.makeText(context, "Not enough spots available. Available: " + availableSpots[0], Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = context.getSharedPreferences("EcoStayPrefs", Context.MODE_PRIVATE)
                        .getInt("userId", -1);

                if (userId == -1) {
                    Toast.makeText(context, "Please login to book", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                long result = dbHelper.bookActivity(userId, activity.getId(), selectedDate[0], participants);

                if (result != -1) {
                    Toast.makeText(context, "Activity booked successfully!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
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

    private void checkAvailability(int activityId, String date, TextView tvAvailableSpots, int[] availableSpotsArray) {
        int bookedParticipantsCount = 0;
        List<ActivityBooking> allBookings = dbHelper.getAllConfirmedActivityBookings();
        for (ActivityBooking b : allBookings) {
            if (b.getActivityId() == activityId && b.getBookingDate().equals(date)) {
                bookedParticipantsCount += b.getParticipants();
            }
        }
        
        Activity activity = dbHelper.getActivity(activityId);

        if (activity != null) {
            int available = activity.getCapacity() - bookedParticipantsCount;
            availableSpotsArray[0] = available;
            if (available > 0) {
                tvAvailableSpots.setText(String.format("Available spots: %d", available));
                tvAvailableSpots.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvAvailableSpots.setText("Fully booked on this date");
                tvAvailableSpots.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityName, tvActivityType, tvDescription, tvPrice, tvDuration, tvGuideName, tvCapacity;
        Button btnBookNow;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvActivityType = itemView.findViewById(R.id.tvActivityType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvGuideName = itemView.findViewById(R.id.tvGuideName);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}