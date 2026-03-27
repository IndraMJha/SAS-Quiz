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

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
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
        String bookmarkCfi = getIntent().getStringExtra("BOOKMARK_CFI");

        webView.addJavascriptInterface(new WebAppInterface(this, epubFileName), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("viewer.html")) {
                    loadEpubIntoWebView(epubAssetFolder, epubFileName, bookmarkCfi);
                }
            }
        });

        webView.loadUrl("file:///android_asset/epubjs/viewer.html");
    }

    private void loadEpubIntoWebView(String folder, String filename, String bookmarkCfi) {
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
                    String passedCfi = (bookmarkCfi != null) ? "'" + bookmarkCfi + "'" : "null";
                    String js = "javascript:loadEpubFromBase64('" + base64 + "', " + passedCfi + ");";
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

    public class WebAppInterface {
        Context mContext;
        String mBookTitle;

        WebAppInterface(Context c, String bookTitle) {
            mContext = c;
            mBookTitle = bookTitle;
        }

        @JavascriptInterface
        public void saveBookmark(String cfi) {
            SharedPreferences prefs = mContext.getSharedPreferences("epub_bookmarks", Context.MODE_PRIVATE);
            // We append a timestamp to the title so multiple bookmarks can exist
            String uniqueKey = mBookTitle + "||" + System.currentTimeMillis();
            prefs.edit().putString(uniqueKey, cfi).apply();
            
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(mContext, "Bookmark saved!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
