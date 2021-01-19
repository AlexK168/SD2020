package com.example.awesometimer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesometimer.Models.Item;
import com.example.awesometimer.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static ItemAdapter.ClickListener clickListener;
    private final LayoutInflater mInflater;
    private List<Item> mItems;

    public ItemAdapter(Context context) { mInflater = LayoutInflater.from(context);}

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        if (mItems != null) {
            Item current = mItems.get(position);
            holder.phaseItemView.setText(current.phase);
            holder.timeItemView.setText(String.valueOf(current.duration));

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

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView phaseItemView;
        private final TextView timeItemView;
        private  Button editButton;

        private ItemViewHolder(View itemView) {
            super(itemView);
            phaseItemView = itemView.findViewById(R.id.textView);
            timeItemView = itemView.findViewById(R.id.timeTextView);
            editButton = itemView.findViewById(R.id.button);
            editButton.setOnClickListener(v -> clickListener.onItemEditClick(getAdapterPosition(), v));
        }
    }


    public void setOnItemClickListener(ItemAdapter.ClickListener clickListener) {
        ItemAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemEditClick(int position, View v);
    }
}
