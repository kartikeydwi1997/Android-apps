package edu.neu.madcourse.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button button,clickyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =findViewById(R.id.button);
        clickyBtn=findViewById(R.id.clicky);

    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button:
                Toast.makeText(MainActivity.this,
                        "  Kartikey Dwivedi\n dwivedi.k@northeastern.edu",Toast.LENGTH_SHORT).show();
                break;
            case R.id.clicky:
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
        }

    }
}