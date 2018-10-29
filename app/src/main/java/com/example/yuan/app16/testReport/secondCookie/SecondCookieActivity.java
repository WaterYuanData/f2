package com.example.yuan.app16.testReport.secondCookie;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.yuan.app16.MainActivity;
import com.example.yuan.app16.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SecondCookieActivity extends AppCompatActivity {

    private static final String TAG = "SecondCookieActivity";


    @ViewInject(R.id.secondWebView)
    WebView webView;
    static private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_cookie);
        //绑定组件,设置监听等
        init();

        //修改cookie
        syncCookie(MainActivity.addressUrl,"55656654654646464654646546");

        //加载网址
        webView.loadUrl(MainActivity.addressUrl);

        applicationContext = getApplicationContext();

        /**
         * @deprecated
         * The WebView now automatically syncs cookies as necessary.
         * You no longer need to create or use the CookieSyncManager.
         */
//        CookieSyncManager cookieSyncManager = CookieSyncManager.getInstance();

    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public static boolean syncCookie(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(applicationContext);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        if (newCookie==cookie) {
            Log.d(TAG, "syncCookie: 同步成功");
        }else{
            Log.d(TAG, "syncCookie: 同步失败"+newCookie);
        }
        return TextUtils.isEmpty(newCookie) ? false : true;
    }


    /**
     * 初始化组件
     */
    public void init() {
//        webView = (WebView) findViewById(R.id.secondWebView);
        ViewUtils.inject(this);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //自适应屏幕大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //网页回退
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        //打印cookie信息
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String cookie = CookieManager.getInstance().getCookie(url);
                try {
                    if (cookie != null) {
                        //解码cookie
                        String decode = URLDecoder.decode(cookie, "utf-8");
                        Log.d(TAG, "onPageStarted: 页面=" + url + " 的cookie信息=" + decode);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String cookie = CookieManager.getInstance().getCookie(url);
                try {
                    if (cookie != null) {
                        //解码cookie
                        String decode = URLDecoder.decode(cookie, "utf-8");
                        Log.d(TAG, "onPageFinished: 页面=" + url + " 的cookie信息=" + decode);
                        if (MainActivity.addressUrl.equals(url) && cookie.contains("ck_user_id")) {
//                        webView.loadUrl(MainLibraryActivity.indexUrl);

                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
