package com.votclick.andrew.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.votclick.andrew.myapplication.custom.ObservableWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    ObservableWebView myWebView;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.btnQR)
    FloatingActionButton btnQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createWebView("http://google.com/");
    }

    public void createWebView(String url) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        myWebView.setWebViewClient(new CustomWebViewClient());
        myWebView.loadUrl(url);

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        myWebView.setOnScrollChangeListener((ObservableWebView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY)
                btnQR.hide();
            else if (scrollY < oldScrollY)
                btnQR.show();

        });
    }

    // This allows for a splash screen
    // (and hide elements once the page loads)
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            super.onPageStarted(webview, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            webview.setVisibility(WebView.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            view.setVisibility(WebView.VISIBLE);

        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
