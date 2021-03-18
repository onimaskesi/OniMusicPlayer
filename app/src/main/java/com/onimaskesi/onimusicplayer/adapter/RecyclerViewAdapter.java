package com.onimaskesi.onimusicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onimaskesi.onimusicplayer.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private String[] audioFileNames;

    public RecyclerViewAdapter(String[] audioFileNames) {
        this.audioFileNames = audioFileNames;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_raw, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.nameTV.setText(audioFileNames[position]);
    }

    @Override
    public int getItemCount() {
        return audioFileNames.length;
    }

    public class RowHolder extends RecyclerView.ViewHolder {

        TextView nameTV;

        public RowHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
        }
    }
}
