package com.example.disastermanagentsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.disastermanagentsystem.R;
import com.example.disastermanagentsystem.model.Alert;
import com.example.disastermanagentsystem.util.AdapterClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlertMarkerAdapter extends ListAdapter<Alert, AlertMarkerAdapter.MyViewHolder> {

    private static AdapterClickListener listener;

    public AlertMarkerAdapter(@NonNull DiffUtil.ItemCallback<Alert> diffCallback, AdapterClickListener listener) {
        super(diffCallback);
        AlertMarkerAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tag, address;
        FloatingActionButton fab;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.tv_tag);
            address = itemView.findViewById(R.id.tv_add);
            fab = itemView.findViewById(R.id.delete_marker);
        }

        void bind(Alert alert){
            tag.setText(alert.getTag());
            address.setText(alert.getAddress());

            fab.setOnClickListener(view -> {
                listener.onDelete(alert);
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_marker_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
