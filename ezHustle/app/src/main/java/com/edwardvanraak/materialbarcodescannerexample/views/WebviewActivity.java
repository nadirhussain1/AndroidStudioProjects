package com.edwardvanraak.materialbarcodescannerexample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.edwardvanraak.materialbarcodescannerexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nadirhussain on 03/04/2017.
 */

public class WebviewActivity extends AppCompatActivity {
    public static final String KEY_WEBVIEW_URL = "KEY_WEBVIEW_URL";
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            String url = getIntent().getStringExtra(KEY_WEBVIEW_URL);
            loadUrlContentIntoWebView(url);
        }
    }

    private void loadUrlContentIntoWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.loadUrl(url);
    }
}
