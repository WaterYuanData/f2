package com.example.yuan.app16;

import android.app.Application;
import android.content.Context;

// import com.itheima.retrofitutils.ItheimaHttp;

/**
 * Created by yuan on 18-2-26.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        /*
         * 联网引擎 com.example.yuan.app16.RetrofitUtils.Main20Activity
         * eg:请求地址:http://www.oschina.net/action/apiv2/banner?catalog=1
         * 那么baseUrl = http://www.oschina.net RetrofitUtils内部封装了如下使用方法
         * ItheimaHttp.init(this, Urls.getBaseUrl());
         * */
        String baseUrl = "http://www.oschina.net";
        // ItheimaHttp.init(this, baseUrl);
    }

    public static Context getContext() {
        return context;
    }
}
