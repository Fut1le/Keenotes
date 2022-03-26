package com.ruya.takimi.keenotes.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ruya.takimi.keenotes.R;

import static java.lang.Thread.sleep;

public class WebViewSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_webview_splash);

        // start the thread and hold the screen for 3 sec
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    // new intent
                    Intent intent = new Intent(WebViewSplashActivity.this, MiniGameActivity.class);
                    startActivity(intent);

                    // kill the current activity
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        thread.start();
    }
}
