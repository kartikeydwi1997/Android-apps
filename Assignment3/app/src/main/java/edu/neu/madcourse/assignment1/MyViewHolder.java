package edu.neu.madcourse.assignment1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemIcon;
    public TextView itemName;
    public TextView itemDesc;

    public MyViewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        itemIcon = itemView.findViewById(R.id.item_icon);
        itemName = itemView.findViewById(R.id.item_name);
        itemDesc = itemView.findViewById(R.id.item_desc);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String url=itemDesc.getText().toString();
                        listener.onItemClick(url);
                }
            }
        });

    }
}