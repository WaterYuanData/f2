package com.example.yuan.app16.testReport;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.yuan.app16.R;
import com.example.yuan.app16.RecyclerViewTest.MyOkhttp;
import com.example.yuan.app16.testReport.secondCookie.SecondCookieActivity;
import com.example.yuan.app16.testReport.webviewhtmlcookiedemo.Main18Activity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static java.net.Proxy.Type.HTTP;

public class Main8Activity extends AppCompatActivity {

    String TAG = "Main8Activity";


    /**
     * Sync Cookie
     */
    public void syncCookie(Context context, String url) {
        try {
            Log.d("syncCookie.url", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("oldCookie", oldCookie);
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
                Log.d("newCookie", newCookie);
            }
        } catch (Exception e) {
            Log.e("syncCookie failed", e.toString());
        }
    }

    /**另一种方法同步Cookie*/
    @OnClick(R.id.otherCookie)
    public void otherCookie(View view){
        startActivity(new Intent(this, SecondCookieActivity.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        ViewUtils.inject(this);
        final String utl = "";
        final TextView textView = (TextView) findViewById(R.id.textUrl);
        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);//双击缩放
        webView.getSettings().setBuiltInZoomControls(true);//拖拽缩放
        webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        webView.getSettings().setDefaultFontSize(12);
//        webView.getSettings().setUserAgentString(getApplication().getUserAgent());
        // 设置可以访问文件
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
        {
            Button bt = (Button) findViewById(R.id.login);
            bt.setText("百度");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String baidu = "http://baidu.com";
                    textView.setText(baidu);
                    webView.loadUrl(baidu);
                }
            });
        }
        Button login2 = ((Button) findViewById(R.id.login2));
        login2.setText("测cookie保存密码");
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Main18Activity.class));
            }
        });

        Button viewById = ((Button) findViewById(R.id.login3));
        viewById.setText("直报系统");
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DirectReportActivity.class));
            }
        });


    }

    class MyWebViewClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: ");
            CookieManager cookieManager = CookieManager.getInstance();
            String cookieStr = cookieManager.getCookie(url);
            Log.d(TAG, "cookieStr = " + cookieStr);
            super.onPageFinished(view, url);
        }
    }

//    public void ppost() {
//        String uriAPI = "http://xx.xxxx.xx:17777/Nafio/Emulator/test/tempPostWml.jsp";
//            /*建立HTTP Post连线*/
//        HttpPost httpRequest =new HttpPost(uriAPI);
//        //Post运作传送变数必须用NameValuePair[]阵列储存
//        //传参数 服务端获取的方法为request.getParameter("name")
//        List <NameValuePair> params=new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("imei","imei"));
//        params.add(new BasicNameValuePair("wml","我的测试"));
//        try{
//            //发出HTTP request
//            httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));//注意这里要写成utf-8,与jsp对应
//            //取得HTTP response
//            HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
//            //若状态码为200 ok
//            if(httpResponse.getStatusLine().getStatusCode()==200){
//                //取出回应字串
//                String strResult=EntityUtils.toString(httpResponse.getEntity());
//            }else{
//                Log.e("n", "b");
//            }
//        }catch(ClientProtocolException e){
//
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
