package com.example.ecostay;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoomAdminAdapter extends RecyclerView.Adapter<RoomAdminAdapter.ViewHolder> {

    private List<Room> roomList;
    private Context context;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(Room room);
    }

    public interface OnDeleteListener {
        void onDelete(Room room);
    }

    public RoomAdminAdapter(List<Room> roomList, Context context, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.roomList = roomList;
        this.context = context;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvName.setText(room.getName());
        holder.tvType.setText(room.getType());
        holder.tvPrice.setText(String.format("$%.2f/night", room.getPrice()));
        holder.tvCapacity.setText("Capacity: " + room.getCapacity());

        // Load image from URI string
        loadRoomImage(holder.ivImage, room.getImageUrl());

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(room));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(room));
    }

    private void loadRoomImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
                    imageView.setImageURI(Uri.parse(imageUrl));
                } else {
                    int resId = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
                    if (resId != 0) {
                        imageView.setImageResource(resId);
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

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvType, tvPrice, tvCapacity;
        Button btnEdit, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}