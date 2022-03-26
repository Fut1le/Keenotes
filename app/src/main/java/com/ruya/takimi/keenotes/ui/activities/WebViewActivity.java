package com.ruya.takimi.keenotes.ui.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ruya.takimi.keenotes.R;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // set transparent status bar
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // set view
        webView = findViewById(R.id.my_webview);

        // configure settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // set web view client
        webView.setWebViewClient(new WebViewClient());

        // set url
        webView.loadUrl("https://vl-keenotes.web.app/");
    }



    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
