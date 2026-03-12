package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoomBookingAdapter extends RecyclerView.Adapter<RoomBookingAdapter.BookingViewHolder> {
    private List<RoomBooking> bookingList;
    private Context context;

    public RoomBookingAdapter(List<RoomBooking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        RoomBooking booking = bookingList.get(position);

        holder.tvRoomName.setText(booking.getRoomName());
        holder.tvCheckIn.setText("Check-in: " + booking.getCheckIn());
        holder.tvCheckOut.setText("Check-out: " + booking.getCheckOut());
        holder.tvTotalPrice.setText(String.format("Total: $%.2f", booking.getTotalPrice()));
        holder.tvStatus.setText(booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvCheckIn, tvCheckOut, tvTotalPrice, tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvCheckIn = itemView.findViewById(R.id.tvCheckIn);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}