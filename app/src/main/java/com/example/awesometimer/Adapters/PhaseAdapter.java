package com.example.awesometimer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Phase;
import com.example.awesometimer.R;

import java.util.List;

public class PhaseAdapter extends RecyclerView.Adapter<PhaseAdapter.PhaseViewHolder> {

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Phase> mPhases; // Cached copy of phases

    public PhaseAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public PhaseAdapter.PhaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.phase_item_view, parent, false);
        return new PhaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhaseAdapter.PhaseViewHolder holder, int position) {
        if (mPhases != null) {
            Phase current = mPhases.get(position);
            holder.phaseItemView.setText(current.name);
        } else {
            // Covers the case of data not being ready yet.
            holder.phaseItemView.setText(R.string.no_phases_yet);
        }
    }

    public void setPhases(List<Phase> phases){
        mPhases = phases;
        notifyDataSetChanged();
    }

    public Phase getItem(int position){
        return mPhases.get(position);
    }

    @Override
    public int getItemCount() {
        if (mPhases != null)
            return mPhases.size();
        else return 0;
    }

    static class PhaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView phaseItemView;

        private PhaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            phaseItemView = itemView.findViewById(R.id.textView);
            Button editButton = itemView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onEditItemClick(getAdapterPosition(), v);
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PhaseAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onEditItemClick(int position, View v);
    }
}
