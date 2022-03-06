package edu.neu.madcourse.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button button,clickyBtn,linkCollector,location,memeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =findViewById(R.id.button);
        clickyBtn=findViewById(R.id.clicky);
        linkCollector=findViewById(R.id.linkCollector);
        memeBtn=findViewById(R.id.meme);
    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button:
                Intent intentAboutMe=new Intent(MainActivity.this,AboutMe.class);
                startActivity(intentAboutMe);
                break;
            case R.id.clicky:
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.linkCollector:
                Intent intentLink=new Intent(MainActivity.this,LinkCollector.class);
                startActivity(intentLink);
                break;

            case R.id.mylocation:
                Intent intentLocation=new Intent(MainActivity.this,Location.class);
                startActivity(intentLocation);
                break;
            case R.id.meme:
                Intent intentMeme=new Intent(MainActivity.this,Meme.class);
                startActivity(intentMeme);
                break;
        }


    }
}