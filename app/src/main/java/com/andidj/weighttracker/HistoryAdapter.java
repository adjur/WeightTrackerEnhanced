package com.andidj.weighttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    public interface OnDeleteClick {
        void onDelete(WeightEntry entry);
    }

    public interface OnRowClick {
        void onRowClick(WeightEntry entry);
    }

    private List<WeightEntry> data;
    private final OnDeleteClick onDeleteClick;
    private final OnRowClick onRowClick;

    public HistoryAdapter(List<WeightEntry> data, OnDeleteClick onDeleteClick, OnRowClick onRowClick) {
        this.data = data;
        this.onDeleteClick = onDeleteClick;
        this.onRowClick = onRowClick;
    }

    public void setData(List<WeightEntry> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        WeightEntry entry = data.get(position);

        holder.tvWeight.setText(String.valueOf(entry.weight));
        holder.tvDate.setText(entry.date);

        holder.btnDelete.setOnClickListener(v -> onDeleteClick.onDelete(entry));
        holder.itemView.setOnClickListener(v -> onRowClick.onRowClick(entry));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvWeight, tvDate;
        Button btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvWeight = itemView.findViewById(R.id.tvRowWeight);
            tvDate = itemView.findViewById(R.id.tvRowDate);
            btnDelete = itemView.findViewById(R.id.btnRowDelete);
        }
    }
}