package com.PrimeClassService.prepmaster;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Reading extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private WebView webView;
    private TextToSpeech textToSpeech;
    private boolean isTtsInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        // Retrieve the file path from the intent
        String filePath = getIntent().getStringExtra("filePath");
        Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();

        webView = findViewById(R.id.webView);
        configureWebViewSettings();
        extractTextFromDocument(filePath);
        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true;
        } else {
            isTtsInitialized = false;
            // Handle initialization failure if needed
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void configureWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // Enable JavaScript
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Page finished loading, perform text extraction here
                view.loadUrl("javascript:window.android.getText(document.body.innerText);");
            }
        });
    }

    private void extractTextFromDocument(String filePath) {
        webView.addJavascriptInterface(new Object() {
            @android.webkit.JavascriptInterface
            public void getText(String text) {
                if (text != null && !text.isEmpty()) {
                    readTextOutLoud(text);
                }else{
                    readTextOutLoud("Document not found");
                }
            }
        }, "android");

        loadPdfUsingPdfJs(filePath);
    }

    private void loadPdfUsingPdfJs(String filePath) {
        webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + filePath);
    }

    private void readTextOutLoud(String text) {
        if (isTtsInitialized && textToSpeech != null && !text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}
