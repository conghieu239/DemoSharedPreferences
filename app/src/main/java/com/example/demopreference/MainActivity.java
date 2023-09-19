package com.example.demopreference;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnStartStop;
    private Button btnRed;
    private Button btnGreen;
    private Button btnBlue;
    private Button btnBlack;
    private Long runningSeconds = 0L;
    private Long runningSeconds1 = 0L;

    private TextView counterTextView;
    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;

    private Runnable updateTimeThread = new Runnable() {
        public void run() {
            if (isRunning) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;
                int seconds = (int) (updatedTime / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                counterTextView.setText("" + minutes + ":" + String.format("%02d", seconds));
                handler.postDelayed(this, 0);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        runningSeconds = sharedPreferences.getLong("runningSeconds", 0) / 2;

        runningSeconds1 = runningSeconds;

        btnStartStop = findViewById(R.id.btnCount);
        counterTextView = findViewById(R.id.textView);

        int minutes = (int) (runningSeconds / 60);
        int seconds = (int) (runningSeconds % 60);
        counterTextView.setText(String.format("%01d:%02d", minutes, seconds));

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    isRunning = true;
                    startTime = SystemClock.uptimeMillis() - (runningSeconds1 * 1000);
                    handler.postDelayed(updateTimeThread, 0);
                } else {
                    isRunning = false;
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimeThread);

                    runningSeconds = (timeSwapBuff + timeInMilliseconds) / 1000;

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("runningSeconds", runningSeconds);
                    editor.apply();
                }
            }
        });

        // Các nút màu
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnBlack = findViewById(R.id.btnBlack);

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTextView.setBackgroundColor(Color.RED);
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTextView.setBackgroundColor(Color.GREEN);
            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTextView.setBackgroundColor(Color.BLUE);
            }
        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTextView.setBackgroundColor(Color.BLACK);
            }
        });
    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong("runningSeconds", runningSeconds);
//        editor.putBoolean("isRunning", isRunning);
//        editor.apply();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        isRunning = sharedPreferences.getBoolean("isRunning", false);
//
//        if (isRunning) {
//            startTime = SystemClock.uptimeMillis() - (runningSeconds * 1000);
//            handler.postDelayed(updateTimeThread, 0);
//        }
//    }
}
