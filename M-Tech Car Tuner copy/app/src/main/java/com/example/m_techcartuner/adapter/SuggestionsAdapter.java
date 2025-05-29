package com.example.m_techcartuner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_techcartuner.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionViewHolder> {

    public interface OnSuggestionClickListener {
        void onSuggestionClick(String suggestion);
    }

    private List<String> suggestions;
    private OnSuggestionClickListener clickListener;

    public SuggestionsAdapter(List<String> suggestions, OnSuggestionClickListener clickListener) {
        this.suggestions = suggestions;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        String suggestion = suggestions.get(position);
        holder.textViewSuggestion.setText(suggestion);
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onSuggestionClick(suggestion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSuggestion;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSuggestion = itemView.findViewById(R.id.textViewSuggestion);
        }
    }

    // This method parses raw suggestion text into a list of clean suggestion strings
    public static List<String> parseSuggestions(String rawText) {
        List<String> suggestions = new ArrayList<>();

        // Split by newlines
        String[] lines = rawText.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Remove any numbering or bullet symbols
                line = line.replaceAll("^\\d+\\.\\s*", ""); // e.g., "1. Cold Air Intake" → "Cold Air Intake"
                suggestions.add(line);
            }
        }

        return suggestions;
    }
}
