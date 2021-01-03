package com.example.awesometimer.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.R;

import java.util.List;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceViewHolder> {

    private final LayoutInflater mInflater;
    private List<Sequence> mSequences; // Cached copy of sequences

    public SequenceAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public SequenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.sequence_item_view, parent, false);
        return new SequenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SequenceViewHolder holder, int position) {
        if (mSequences != null) {
            Sequence current = mSequences.get(position);
            holder.sequenceItemView.setText(current.title);
            // holder.sequenceItemView.setBackgroundColor(Color.parseColor(current.color));
        } else {
            // Covers the case of data not being ready yet.
            holder.sequenceItemView.setText("No Sequences yet");
        }
    }

    public void setSequences(List<Sequence> sequences){
        mSequences = sequences;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mSequences has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mSequences != null)
            return mSequences.size();
        else return 0;
    }

    class SequenceViewHolder extends RecyclerView.ViewHolder {
        private final TextView sequenceItemView;

        private SequenceViewHolder(View itemView) {
            super(itemView);
            sequenceItemView = itemView.findViewById(R.id.textView);
        }
    }
}
