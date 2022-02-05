package edu.neu.madcourse.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity {
    private Button buttonA,buttonB,buttonC,buttonD,buttonE,buttonF;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        buttonA =findViewById(R.id.button1);
        buttonB =findViewById(R.id.button2);
        buttonC =findViewById(R.id.button3);
        buttonD =findViewById(R.id.button4);
        buttonE =findViewById(R.id.button5);
        buttonF =findViewById(R.id.button6);
        textView=findViewById(R.id.textView);
    }


    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button1:
                textView.setText("Pressed:A");
                break;
            case R.id.button2:
                textView.setText("Pressed:B");
                break;
            case R.id.button3:
                textView.setText("Pressed:C");
                break;
            case R.id.button4:
                textView.setText("Pressed:D");
                break;
            case R.id.button5:
                textView.setText("Pressed:E");
                break;
            case R.id.button6:
                textView.setText("Pressed:F");
                break;

        }

    }
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textView.setText("Pressed:A");

        }
    }
}