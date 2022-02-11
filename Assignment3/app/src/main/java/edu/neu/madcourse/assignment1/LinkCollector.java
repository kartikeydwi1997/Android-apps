package edu.neu.madcourse.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class LinkCollector extends AppCompatActivity implements Dialog.DialogListener {
    private ArrayList<ItemCard> itemList = new ArrayList<>();;

    private RecyclerView recyclerView;
    private MyAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FloatingActionButton floatingActionButton;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);
        init(savedInstanceState);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Snackbar.make(findViewById(R.id.myLayout), "Item Deleted Successfully!",
                        Snackbar.LENGTH_SHORT)
                        .show();
                int position = viewHolder.getLayoutPosition();
                itemList.remove(position);
                rviewAdapter.notifyItemRemoved(position);

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // This is only a possible way to do, please find your own way to generate the key
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getImageSource());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getItemName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getItemDesc());
            // put isChecked information into instance
            //outState.putBoolean(KEY_OF_INSTANCE + i + "3", itemList.get(i).getStatus());
        }
        super.onSaveInstanceState(outState);

    }
    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
//                    boolean isChecked = savedInstanceState.getBoolean(KEY_OF_INSTANCE + i + "3");

                    // We need to make sure names such as "XXX(checked)" will not duplicate
                    // Use a tricky way to solve this problem, not the best though
//                    if (isChecked) {
//                        itemName = itemName.substring(0, itemName.lastIndexOf("("));
//                    }
                    ItemCard itemCard = new ItemCard(imgId, itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }



    }

    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        rviewAdapter = new MyAdapter(itemList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                LinkCollector.this.startActivity(browserIntent);
            }

//
        };
        rviewAdapter.setOnItemClickListener(itemClickListener);

        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }

    public void openDialog(){
    Dialog dialog=new Dialog();
    dialog.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    public void applyTexts(String linkName, String url) {
        itemList.add(0, new ItemCard(R.drawable.empty, linkName, url));
        Snackbar.make(findViewById(R.id.myLayout), "Item Added Successfully!",
                Snackbar.LENGTH_SHORT)
                .show();
        rviewAdapter.notifyItemInserted(0);
    }
}