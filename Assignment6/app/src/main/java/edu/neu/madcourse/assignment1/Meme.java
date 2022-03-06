package edu.neu.madcourse.assignment1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.number.LocalizedNumberFormatter;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import edu.neu.madcourse.assignment1.databinding.ActivityMainBinding;

public class Meme extends AppCompatActivity {
    Spinner redditSpinner;
    EditText subReddit;
    ImageView memeImage;
    ProgressBar progressBar;
    String savedUrl = "";
    private RequestQueue queue;
    RequestQueue requestQueue;
    Uri imageUri;
    ArrayList<String> listdata = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meme);

        redditSpinner = findViewById(R.id.spinner);
        subReddit = findViewById(R.id.editTextTextPersonName);
        memeImage = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.dropdownReddit));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        redditSpinner.setAdapter(arrayAdapter);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        redditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void searchMeme(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String subreddit = subReddit.getText().toString();
        Log.d("URL Reddit", subreddit);
        String Count = redditSpinner.getSelectedItem().toString();
        Log.d("URL Count", Count);
        queue = Volley.newRequestQueue(this);
        String URL = "";
        if (subreddit.equals("") && !Count.equals("")) {
            URL = "https://meme-api.herokuapp.com/gimme/" + Count;
        } else if (Count.equals("") && !subreddit.equals("")) {
            URL = "https://meme-api.herokuapp.com/gimme/" + subreddit;
        } else {
            URL = "https://meme-api.herokuapp.com/gimme/" + subreddit + "/" + Count;
        }

        Log.d("TAG URL", URL);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        String finalURL = URL;
        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                JSONObject jObject = new JSONObject();
                try {

                    // Initial website is "https://jsonplaceholder.typicode.com/posts/1"
                    URL url = new URL(finalURL);
//                    URL url=new URL("https://jsonplaceholder.typicode.com/posts/1");
                    Log.d("TAG URL COPIED", url.toString());
                    // Get String response from the url address
                    String resp = httpResponse(url);
                    Log.d("TAG resp", resp);


                    jObject = new JSONObject(resp);


                } catch (MalformedURLException e) {
                    Log.e("TAG", "MalformedURLException");
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    Log.e("TAG", "ProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("TAG", "IOException");
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(findViewById(R.id.linearLayoutCompat), "No image found for this subreddit ",
                            Snackbar.LENGTH_SHORT)
                            .show();

                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("TAG", "JSONException");
                    e.printStackTrace();
                }


                JSONObject finalJObject = jObject;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        try {
                            JSONArray jsonArray = finalJObject.getJSONArray("memes");
                            listdata = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject eachURL = jsonArray.getJSONObject(i);
                                String mQuestion = eachURL.getString("url");
                                listdata.add(mQuestion);
                            }
                            savedUrl = listdata.get(0).toString();
                            progressBar.setVisibility(View.INVISIBLE);
                            Glide.with(Meme.this).load(listdata.get(0)).into(memeImage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public static String httpResponse(URL url) throws IOException {

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        Log.d("TAG URL2", conn.toString());
//        conn.setRequestMethod("POST");
//        conn.setDoInput(true);
//
//        conn.connect();

        // Read response.
        InputStream inputStream = conn.getInputStream();
        Log.d("TAG INPUTSTREAM", inputStream.toString());
        String resp = convertStreamToString(inputStream);

        return resp;
    }

    public static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String len;
            while ((len = bufferedReader.readLine()) != null) {
                stringBuilder.append(len);
            }
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void download(View view) {

        Uri uri = Uri.parse(savedUrl);
        ContentResolver resolver = getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName=String.valueOf(System.currentTimeMillis());
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"MY IMAGES/");
        Uri finalUri=resolver.insert(uri,contentValues);
        imageUri=finalUri;
//        return finalUri;

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);


        BitmapDrawable bitmapDrawable=(BitmapDrawable) memeImage.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        saveImageToGallery(bitmap);
    }

    private void saveImageToGallery(Bitmap bitmap) {
        OutputStream fos;
        try{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                ContentResolver resolver=getContentResolver();
                ContentValues contentValues=new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"ASSIGNMENT6");
                Uri imageUri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                fos= resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);
                Toast.makeText(this,"Image Saved",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"Image not Saved\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}