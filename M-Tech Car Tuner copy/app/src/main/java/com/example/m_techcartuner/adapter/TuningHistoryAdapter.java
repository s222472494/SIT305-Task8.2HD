package com.example.m_techcartuner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.R;
import com.example.m_techcartuner.model.TuningHistoryItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TuningHistoryAdapter extends RecyclerView.Adapter<TuningHistoryAdapter.ViewHolder> {

    private final List<TuningHistoryItem> items;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
    private final OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onDelete(int position, TuningHistoryItem item);
    }

    public TuningHistoryAdapter(List<TuningHistoryItem> items, OnItemDeleteListener deleteListener) {
        this.items = items;
        this.deleteListener = deleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCarDetails, textViewGoal, textViewAdvice, textViewTimestamp;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCarDetails = itemView.findViewById(R.id.textViewCarDetails);
            textViewGoal = itemView.findViewById(R.id.textViewGoal);
            textViewAdvice = itemView.findViewById(R.id.textViewAdvice);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    @NonNull
    @Override
    public TuningHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tuning_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuningHistoryAdapter.ViewHolder holder, int position) {
        TuningHistoryItem item = items.get(position);

        holder.textViewCarDetails.setText(item.getYear() + " " + item.getMake() + " " + item.getModel() + " (" + item.getEngine() + ")");
        holder.textViewGoal.setText("Goal: " + item.getGoal());
        holder.textViewAdvice.setText(item.getAiAdvice());

        if (item.getTimestamp() != null) {
            holder.textViewTimestamp.setText(dateFormat.format(item.getTimestamp()));
        } else {
            holder.textViewTimestamp.setText("");
        }

        // Set up the delete button
        holder.buttonDelete.setEnabled(true); // Ensure it's enabled by default when reused
        holder.buttonDelete.setText("Delete"); // Reset text in case it was modified previously
        holder.buttonDelete.setAlpha(1.0f); // Fully visible by default

        holder.buttonDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && deleteListener != null) {
                // Immediately give user feedback
                holder.buttonDelete.setText("Deleting...");
                holder.buttonDelete.setEnabled(false);
                holder.buttonDelete.setAlpha(0.6f); // Dim the button to show it's inactive

                deleteListener.onDelete(currentPosition, items.get(currentPosition));
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }
    }
}
