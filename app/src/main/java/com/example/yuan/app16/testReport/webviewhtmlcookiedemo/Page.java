package com.example.yuan.app16.testReport.webviewhtmlcookiedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.yuan.app16.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Page extends Activity {


    private Button btn;
    public String urlSign = "http://202.99.59.96:9000/zf12365/index.html";
    private WebView webView;
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main18);
        initView();

        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();
                cookieManager.removeAllCookie();

            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        // TODO Auto-generated method stub
        btn = (Button) findViewById(R.id.button1);
        btn.setText("清除缓存");
        webView = (WebView) findViewById(R.id.activity_webview);
        webView.requestFocus();
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        WebSettings web = webView.getSettings();
        web.setJavaScriptEnabled(true);
        web.setBuiltInZoomControls(true);
        web.setSupportZoom(true);
        web.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web.setUseWideViewPort(true);
        web.setLoadWithOverviewMode(true);
        web.setSavePassword(true);
        web.setSaveFormData(true);
        webView.loadUrl(urlSign);
        webView.setWebViewClient(new MyWebViewClient());
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     */
    public void syncCookie(String url, String cookie) {
        CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);
    }

}

class MyWebViewClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return true;
    }

    public void onPageFinished(WebView view, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(url);
        if (CookieStr != null) {
            Log.i("cookie", CookieStr);
            try {
                Log.i("转码后", URLDecoder.decode(CookieStr, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        super.onPageFinished(view, url);
    }

}




