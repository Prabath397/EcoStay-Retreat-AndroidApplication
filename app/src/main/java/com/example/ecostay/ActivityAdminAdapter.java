package com.example.ecostay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivityAdminAdapter extends RecyclerView.Adapter<ActivityAdminAdapter.ViewHolder> {

    private List<Activity> activityList;
    private Context context;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(Activity activity);
    }

    public interface OnDeleteListener {
        void onDelete(Activity activity);
    }

    public ActivityAdminAdapter(List<Activity> activityList, Context context,
                                OnEditListener editListener, OnDeleteListener deleteListener) {
        this.activityList = activityList;
        this.context = context;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activityList.get(position);

        holder.tvName.setText(activity.getName());
        holder.tvType.setText(activity.getType());
        holder.tvPrice.setText(String.format("$%.2f/person", activity.getPrice()));
        holder.tvDuration.setText("Duration: " + activity.getDuration());
        holder.tvGuide.setText("Guide: " + activity.getGuideName());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onEdit(activity);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onDelete(activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice, tvDuration, tvGuide;
        Button btnEdit, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvGuide = itemView.findViewById(R.id.tvGuide);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}