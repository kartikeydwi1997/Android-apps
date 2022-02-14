package edu.neu.madcourse.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkCollector extends AppCompatActivity implements Dialog.DialogListener {
    private ArrayList<ItemCard> itemList = new ArrayList<>();;
    private static final String FILE_NAME="A01.txt";
    private RecyclerView recyclerView;
    private MyAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FloatingActionButton floatingActionButton;
    private TextView textView1;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    String regex = "((http|https)://)(www.)?"
            + "[a-zA-Z0-9@:%._\\+~#?&//=]"
            + "{2,256}\\.[a-z]"
            + "{2,6}\\b([-a-zA-Z0-9@:%"
            + "._\\+~#?&//=]*)";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);
        FileInputStream fis=null;
        try {
            fis=openFileInput(FILE_NAME);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            String line = br.readLine();

            while (line != null) {
                String[] lineSplit = line.split("---");
                itemList.add(0, new ItemCard(R.drawable.logo, lineSplit[0], lineSplit[1]));
                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        textView1=findViewById(R.id.noItemText);
        init(savedInstanceState);
        if(itemList.size()!=0) {
            textView1.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0);
            textView1.setText("");
        }
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

                FileInputStream fis=null;
                List<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();

                int count=0;
                try {
                    fis=openFileInput(FILE_NAME);
                    InputStreamReader isr=new InputStreamReader(fis);
                    BufferedReader br=new BufferedReader(isr);
                    String line = br.readLine();

                    while (line != null) {
                        ArrayList<String> list1 = new ArrayList<String>();
                        String[] lineSplit = line.split("---");
                        if(count!=(itemList.size()-(position+1))) {
                            list1.add(lineSplit[0]);
                            list1.add(lineSplit[1]);
                            listOfLists.add(list1);
                        }
                        count+=1;

                        line = br.readLine();

                    }

                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //List is deleted
              File file= new File(getFilesDir(),FILE_NAME);
              if(file.exists()){
                  deleteFile(FILE_NAME);
              }

              //Form new list in localstorage

                listOfLists.forEach((list)  ->
                {
                    FileOutputStream fos=null;//close this fileoutput
                    String store = list.get(0).toString() + "---" + list.get(1).toString();
                    try {
                        fos = openFileOutput(FILE_NAME, MODE_APPEND);
                        fos.write((store + "\n").getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                itemList.remove(position);
                if (itemList.size()==0){
                    textView1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_search_24, 0, 0);
                    textView1.setText("No item found");
                }
                rviewAdapter.notifyItemRemoved(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        for (int i = 0; i < size; i++) {
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getImageSource());
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getItemShortName());
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getItemUrl());
        }
        super.onSaveInstanceState(outState);

    }
    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {

            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    ItemCard itemCard = new ItemCard(imgId, itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }else{
           textView1.setText("No item found");
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
                Pattern p = Pattern.compile(regex);

                //https://www.geeksforgeeks.org/check-if-an-url-is-valid-or-not-using-regular-expression/
                if (url == null) {
                    Snackbar.make(findViewById(R.id.myLayout), "Invalid Url ",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                Matcher m = p.matcher(url);
                if(!m.matches()){
                    Snackbar.make(findViewById(R.id.myLayout), "Invalid Url ",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                LinkCollector.this.startActivity(browserIntent);
            }
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
        textView1.setText("");
        Pattern p = Pattern.compile(regex);

        //https://www.geeksforgeeks.org/check-if-an-url-is-valid-or-not-using-regular-expression/
        if (url.toString() == null) {
            Snackbar.make(findViewById(R.id.myLayout), "Invalid Url",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        Matcher m = p.matcher(url.toString() );
        if(!m.matches()){
            Snackbar.make(findViewById(R.id.myLayout), "Invalid Url",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        textView1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        itemList.add(0, new ItemCard(R.drawable.logo, linkName, url));
        FileOutputStream fos=null;//close this fileoutput
        String store = linkName + "---" + url;

        try {
            fos=openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write((store + "\n").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Snackbar.make(findViewById(R.id.myLayout), "Item Added Successfully!",
                Snackbar.LENGTH_SHORT)
                .show();
        rviewAdapter.notifyItemInserted(0);
    }
}