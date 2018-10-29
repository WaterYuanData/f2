package com.example.yuan.app16.testReport;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.io.InputStream;

public class DirectReportActivity extends Activity {
    private static final String TAG = "DirectReportActivity";
    private WebView myWebView;
    private WebSettings settings;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_report);
        myWebView = (WebView) findViewById(R.id.webview);
        settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);//
//        myWebView.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
//        settings.setSupportZoom(true);//双击缩放
        settings.setBuiltInZoomControls(true);//拖拽缩放
        settings.setDisplayZoomControls(false);//设定缩放控件隐藏
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.setWebViewClient(new MyWebViewClient());
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {  //表示按返回键时的操作
                        myWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        String url = "http://202.99.59.96/appLogin";
        syncCookie(getApplicationContext(), url);
        myWebView.loadUrl(url);
        if (!isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }

    class MyWebViewClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: url="+url);
            CookieManager cookieManager = CookieManager.getInstance();
            String cookieStr = cookieManager.getCookie(url);
            Log.d(TAG, "cookieStr = " + cookieStr);
            super.onPageFinished(view, url);
        }
    }

    /**
     * Sync Cookie
     */
    public void syncCookie(Context context, String url) {
        try {
            Log.d("同步Cookie的url=", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("旧Cookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", "INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.d("新Cookie", newCookie);
            }
        } catch (Exception e) {
            Log.e("同步Cookie failed", e.toString());
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }


}