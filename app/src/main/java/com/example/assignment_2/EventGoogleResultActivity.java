package com.example.assignment_2;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class EventGoogleResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_google_result);

        // using the ID set in previous step, get reference to the WebView
        WebView webView = findViewById(R.id.webView);

        // get country name from Intent
        String eventName = getIntent().getExtras().getString("eventName");


        // compile the Wikipedia URL, which will be used to load into WebView
                String wikiPageURL = "https://www.google.com/search?q=" + eventName;

        // set new WebView Client for the WebView
        // This gives the WebView ability to be load the URL in the current WebView
        // instead of navigating to default web browser of the device
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(wikiPageURL);
    }
}