package com.indrajha.sasquiz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EpubViewerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub_viewer);

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        String epubAssetFolder = getIntent().getStringExtra("EPUB_ASSET_FOLDER");
        String epubFileName = getIntent().getStringExtra("EPUB_FILE_NAME");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("viewer.html")) {
                    loadEpubIntoWebView(epubAssetFolder, epubFileName);
                }
            }
        });

        webView.loadUrl("file:///android_asset/epubjs/viewer.html");
    }

    private void loadEpubIntoWebView(String folder, String filename) {
        new Thread(() -> {
            try {
                InputStream is = getAssets().open(folder + "/" + filename);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();

                byte[] fileBytes = buffer.toByteArray();
                String base64 = Base64.encodeToString(fileBytes, Base64.NO_WRAP);
                
                new Handler(Looper.getMainLooper()).post(() -> {
                    String js = "javascript:loadEpubFromBase64('" + base64 + "');";
                    webView.evaluateJavascript(js, null);
                });
                
            } catch (IOException e) {
                Log.e("EpubViewerActivity", "Failed to load epub file", e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(EpubViewerActivity.this, "Failed to load book", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }
}
