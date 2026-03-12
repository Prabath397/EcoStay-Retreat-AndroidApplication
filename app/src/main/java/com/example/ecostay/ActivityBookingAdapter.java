package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivityBookingAdapter extends RecyclerView.Adapter<ActivityBookingAdapter.BookingViewHolder> {
    private List<ActivityBooking> bookingList;
    private Context context;

    public ActivityBookingAdapter(List<ActivityBooking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        ActivityBooking booking = bookingList.get(position);

        holder.tvActivityName.setText(booking.getActivityName());
        holder.tvBookingDate.setText("Date: " + booking.getBookingDate());
        holder.tvParticipants.setText("Participants: " + booking.getParticipants());
        holder.tvTotalPrice.setText(String.format("Total: $%.2f", booking.getActivityPrice() * booking.getParticipants()));
        holder.tvStatus.setText(booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityName, tvBookingDate, tvParticipants, tvTotalPrice, tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}