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
    private OnAudioListener onAudioListener;

    public RecyclerViewAdapter(String[] audioFileNames, OnAudioListener onAudioListener) {
        this.audioFileNames = audioFileNames;
        this.onAudioListener = onAudioListener;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_raw, parent, false);
        return new RowHolder(itemView, onAudioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.nameTV.setText(audioFileNames[position]);
    }

    @Override
    public int getItemCount() {
        return audioFileNames.length;
    }

    public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTV;
        OnAudioListener onAudioListener;

        public RowHolder(@NonNull View itemView, OnAudioListener onAudioListener) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            this.onAudioListener = onAudioListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onAudioListener.onAudioClick(getAdapterPosition());
        }
    }

    public interface OnAudioListener{
        void onAudioClick(int position);
    }
}
