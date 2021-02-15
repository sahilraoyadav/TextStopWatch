package com.example.textstopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    private int sec = 0;
    private boolean is_running;
    private boolean was_running;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            sec = savedInstanceState.getInt("seconds");
            is_running = savedInstanceState.getBoolean("running");
            was_running = savedInstanceState.getBoolean("wasRunning");
        }
        running_Timer();
    }

    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", sec);
        savedInstanceState.putBoolean("running", is_running);
        savedInstanceState.putBoolean("wasRunning", was_running);
    }

    @Override
    protected void onPause() {
        super.onPause();
        was_running = is_running;
        is_running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (was_running) {
            is_running = true;
        }
    }

    public void onClickStart(View view) {
        is_running = true;
    }

    public void onClickStop(View view) {
        is_running = false;
    }

    public void onClickReset(View view) {
        is_running = false;
        sec = 0;
    }

    private void running_Timer() {
        final TextView t_View = (TextView) findViewById(R.id.time_view);
        final Handler handle = new Handler();
        handle.post(new Runnable() {
            @Override
            public void run() {
                int hrs = sec / 3600;
                int mins = (sec % 3600) / 60;
                int secs = sec % 60;
                String time_t = String.format(Locale.getDefault(), "    %d:%02d:%02d   ", hrs, mins, secs);
                t_View.setText(time_t);
                if (is_running) {
                    sec++;
                }
                handle.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
