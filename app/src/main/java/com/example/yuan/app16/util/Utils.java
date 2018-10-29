package com.example.yuan.app16.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.yuan.app16.util.runtimepermissions.PermissionsManager;
import com.example.yuan.app16.util.runtimepermissions.PermissionsResultAction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuan on 18-3-5.
 */

public class Utils {

    /**
     * 打印数组
     */
    public static void printArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            Log.d("打印", "print: array[" + i + "]=" + array[i]);
        }
    }

    public static void printList(List list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d("打印", "print: list.get(" + i + ")=" + list.get(i));
        }
    }


    /**
     * 查询并请求某单个权限
     */
    public static void queryAndRequestPermission(Activity context, String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, 1);
        }
    }


    /**
     * 请求清单文件中所有的权限
     * 适配android6.0以上权限                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         =
     */
    public static void requestPermissions(final Activity context) {
        /**
         * 请求所有必要的权限
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(context, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Toast.makeText(context, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(context, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 首先默认个文件保存路径
     */
    private static final String SAVE_PIC_PATH =
            Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ?
                    Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";// 保存到SD卡


    /**
     * 获取要下载的文件的长度long型
     */
    public static long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
//            response.toString();
            String disposition = response.header("Content-disposition");
            Log.d("URL预处理", "getContentLength: disposition=" + disposition);
            response.body().close();
            return contentLength;
        }
        return 0;
    }

    /**
     * 是否包含中文
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 判断是否输入的数字
     */
    public static boolean isInputNum(String str) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 格式化时间
     */
    public static String formatMD(long timelnMillis) {

        //如何得到当前时间
//        long elapsedRealtime = SystemClock.elapsedRealtime();//从开机到现在经过的时间毫秒数,包括睡眠时间
//        long currentTimeMillis = System.currentTimeMillis();//当前时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timelnMillis);
        Date date = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("M月d日");
        String newTypeDate = f.format(date);
        return newTypeDate;
    }

    /**
     * 格式化时间
     */
    public static String formatTimeYMDHS(long time) {
        // 建一个格式
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制 12小时hh
        // 把时间变为日期
        // 格式化
        return format1.format(new Date(time));
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前是否使用的是 WIFI网络
     */
    public static boolean isWifiActive(Context icontext) {
        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI")
                            && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context ctx) {
        return ctx.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context ctx) {
        return ctx.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取手机密度
     */
    public static float getDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取手机dpi
     */
    public static int getDenisityDpi(Context ctx) {
        return ctx.getResources().getDisplayMetrics().densityDpi;
    }

}
