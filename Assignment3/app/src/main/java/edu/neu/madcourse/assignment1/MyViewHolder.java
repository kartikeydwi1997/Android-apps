package edu.neu.madcourse.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemIcon;
    public TextView itemShortName;
    public TextView itemUrl;

    public MyViewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        itemIcon = itemView.findViewById(R.id.item_icon);
        itemShortName = itemView.findViewById(R.id.item_shortName);
        itemUrl = itemView.findViewById(R.id.item_url);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String url=itemUrl.getText().toString();
                        listener.onItemClick(url);
                }
            }
        });

    }
}