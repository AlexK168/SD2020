package com.example.awesometimer.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Sequence;
import com.example.awesometimer.R;

import java.util.List;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceViewHolder> {

    private static ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Sequence> mSequences;

    public SequenceAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public SequenceAdapter.SequenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.sequence_item_view, parent, false);
        return new SequenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SequenceAdapter.SequenceViewHolder holder, int position) {
        if (mSequences != null) {
            Sequence current = mSequences.get(position);
            holder.sequenceItemView.setText(current.title);
            holder.sequenceItemView.setBackgroundColor(current.color);
        } else {
            holder.sequenceItemView.setText("No Sequences yet");
        }
    }

    public void setSequences(List<Sequence> sequences){
        mSequences = sequences;
        notifyDataSetChanged();
    }

    public Sequence getItem(int pos) { return mSequences.get(pos);}

    @Override
    public int getItemCount() {
        if (mSequences != null)
            return mSequences.size();
        else return 0;
    }

    static class SequenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView sequenceItemView;

        private SequenceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            sequenceItemView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SequenceAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
