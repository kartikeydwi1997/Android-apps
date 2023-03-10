package edu.neu.madcourse.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final ArrayList<ItemCard> itemList;
    private ItemClickListener listener;

    //Constructor
    public MyAdapter(ArrayList<ItemCard> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemCard item = itemList.get(position);
        holder.itemIcon.setImageResource(item.getImageSource());
        holder.itemShortName.setText(item.getItemShortName());
        holder.itemUrl.setText(item.getItemUrl());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
