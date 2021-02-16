package com.example.game.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game.Models.Ship;
import com.example.game.R;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ItemViewHolder>{

    private static ShipAdapter.ClickListener clickListener;
    private final LayoutInflater mInflater;
    private ArrayList<Ship> mItems;

    public ShipAdapter(Context context) { mInflater = LayoutInflater.from(context);}

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mItems != null) {
            Ship current = mItems.get(position);
            holder.sizeText.setText(String.valueOf(current.length));
            holder.xText.setText(String.valueOf(current.x));
            holder.yText.setText(String.valueOf(current.y));
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    public void setItems(ArrayList<Ship> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public Ship getItem(int index) {
        return mItems.get(index);
    }

    public void addItem(Ship item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mItems.remove(index);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView xText;
        private final TextView yText;
        private final TextView sizeText;
        private final Button deleteButton;

        private ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            yText = itemView.findViewById(R.id.yEditText);
            xText = itemView.findViewById(R.id.xEditText);
            sizeText = itemView.findViewById(R.id.sizeEditText);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(v -> clickListener.onDeleteClicked(getAdapterPosition(), v));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClicked(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ShipAdapter.ClickListener clickListener) {
        ShipAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onDeleteClicked(int position, View v);
        void onItemClicked(int position, View v);
    }
}
