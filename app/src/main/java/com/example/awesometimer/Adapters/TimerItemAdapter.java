package com.example.awesometimer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.R;

import java.util.List;

public class TimerItemAdapter extends RecyclerView.Adapter<TimerItemAdapter.ItemViewHolder> {

    private final LayoutInflater mInflater;
    private List<Item> mItems;

    public TimerItemAdapter(Context context) { mInflater = LayoutInflater.from(context);}

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.timer_item, parent, false);
        return new TimerItemAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mItems != null) {
            Item current = mItems.get(position);
            holder.phaseItemView.setText(current.phase);
            String result = current.duration + " s";
            holder.timeItemView.setText(result);
        } else {
            holder.phaseItemView.setText(R.string.no_phases_yet);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    public Item getItem(int position){
        return mItems.get(position);
    }

    public void setItems(List<Item> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return mItems;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView phaseItemView;
        private final TextView timeItemView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            phaseItemView = itemView.findViewById(R.id.textView);
            timeItemView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
