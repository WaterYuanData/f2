package com.example.yuan.app16.testReport.webviewhtmlcookiedemo;

import android.os.Bundle;

import com.example.yuan.app16.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

//http://blog.csdn.net/dickyqie/article/details/54287514
public class Main18Activity extends Activity {

    private static final String TAG = "Main18Activity";
    private String urllogin = "http://202.99.59.96/appLogin";
    private String urlindex = "http://202.99.59.96/zf12365/index.html";

    private WebView webView;
    private Button btn;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private CookieManager cookieManager;
    private String CookieStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main18);
        //获取view
        initView();
        //设置监听
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                startActivity(new Intent(Main18Activity.this, Page.class));
                webView.loadUrl(urlindex);
            }
        });
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                cookieManager.setCookie(urllogin, null);
//                cookieManager.setCookie(urlindex, "");
                //setCookie为null无效不如remove
                cookieManager.removeSessionCookies(null);
                cookieManager.removeAllCookies(null);
                Log.d(TAG, "urllogin: "+cookieManager.getCookie(urllogin));
                Log.d(TAG, "urlindex: "+cookieManager.getCookie(urlindex));
            }
        });
        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(urllogin);
            }
        });
        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cookieManager.removeSessionCookies(null);
                cookieManager.removeAllCookies(null);
                //有问题?
                cookieManager.setCookie(urlindex, "; ck_user_id=13162; ck_user_name=luru; ck_user_realname=luru; ck_depart_id=15282; ck_depart_name=%E5%9B%BD%E5%AE%B6%E8%B4%A8%E6%A3%80%E6%80%BB%E5%B1%80; ck_user_sysrole=%7C%24%7C13016%7C%24%7C12567%7C%24%7C13015%7C%24%7C13063%7C%24%7C13061%7C%24%7C13062; ck_unit_type=1");
                Log.d(TAG, "*************urlindex: "+cookieManager.getCookie(urlindex));
                webView.loadUrl(urlindex);
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initView() {
        btn = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
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
//        web.setSavePassword(true);
//        web.setSaveFormData(true);

        cookieManager = CookieManager.getInstance();
        CookieStr = cookieManager.getCookie(urllogin);
        if (CookieStr == null || !CookieStr.contains("name")) {
            webView.loadUrl(urllogin);
        } else {
            webView.loadUrl(urlindex);
        }
        webView.setWebViewClient(new MyWebViewClient());//顺序???
    }

    //内部类
    class MyWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {
//            CookieManager cookieManager = CookieManager.getInstance();
            CookieStr = cookieManager.getCookie(url);
            Log.d(TAG, "onPageFinished: " + url);
            if (CookieStr != null) {
                Log.i(TAG, "cookie = " + CookieStr);
                try {
                    Log.i(TAG, "转码后 = " + URLDecoder.decode(CookieStr, "utf-8"));
                    //转码后  = JSESSIONID=4E03D9D1AC04004610B1256CA7DF9F73; ck_user_id=13162;
                    // ck_user_name=luru; ck_user_realname=luru; ck_depart_id=15282; ck_depart_name=国家质检总局;
                    // ck_user_sysrole=|$|13016|$|12567|$|13015|$|13063|$|13061|$|13062; ck_unit_type=1
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            super.onPageFinished(view, url);
        }

    }

}
