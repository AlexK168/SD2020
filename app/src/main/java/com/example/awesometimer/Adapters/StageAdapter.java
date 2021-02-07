package com.example.awesometimer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Stage;
import com.example.awesometimer.R;

import java.util.List;

public class StageAdapter extends RecyclerView.Adapter<StageAdapter.StageViewHolder> {

    private static StageAdapter.ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Stage> mStages;

    public StageAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public StageAdapter.StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.stage_item_view, parent, false);
        return new StageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StageAdapter.StageViewHolder holder, int position) {
        if (mStages != null) {
            Stage current = mStages.get(position);
            holder.textView.setText(current.name);
        } else {
            holder.textView.setText(R.string.no_stages);
        }
    }

    @Override
    public int getItemCount() {
        if (mStages != null)
            return mStages.size();
        else return 0;
    }

    public Stage getItem(int position){
        return mStages.get(position);
    }

    public void setStages(List<Stage> stages){
        mStages = stages;
        notifyDataSetChanged();
    }

    static class StageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        public StageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(StageAdapter.ClickListener clickListener) {
        StageAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
