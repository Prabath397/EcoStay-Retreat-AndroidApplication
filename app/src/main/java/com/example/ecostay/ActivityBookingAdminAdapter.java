package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivityBookingAdminAdapter extends RecyclerView.Adapter<ActivityBookingAdminAdapter.ViewHolder> {

    private List<ActivityBooking> bookingList;
    private Context context;
    private DBHelper dbHelper;

    public ActivityBookingAdminAdapter(List<ActivityBooking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityBooking booking = bookingList.get(position);

        // Get user name from user ID
        User user = dbHelper.getUser(booking.getUserId());
        String userName = (user != null) ? user.getName() : "Unknown User";

        holder.tvType.setText("🎯 Activity: " + booking.getActivityName());
        holder.tvUser.setText("👤 Guest: " + userName);  // Now shows name instead of ID
        holder.tvDetails.setText(String.format("📅 Date: %s | 👥 %d participants",
                booking.getBookingDate(), booking.getParticipants()));
        holder.tvTotal.setText(String.format("💰 Total: $%.2f",
                booking.getActivityPrice() * booking.getParticipants()));
        holder.tvStatus.setText("✅ " + booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvUser, tvDetails, tvTotal, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}