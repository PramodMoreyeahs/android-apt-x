package com.apt_x.app.views.activity.signup;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //your handling...
        return super.shouldOverrideUrlLoading(view, url);
    }
}
