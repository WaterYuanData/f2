package com.example.yuan.app16.user;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.support.v4.os.UserManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main11Activity extends Activity {
    String TAG = "Main11Activity";
    Context applicationContext;
    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);


        applicationContext = getApplicationContext();
        UserManager mUm = (UserManager) applicationContext.getSystemService(Context.USER_SERVICE);
//        UserManager mUserManager = UserManagerCompat.getInstance(applicationContext);
//        SecurityException: You either need MANAGE_USERS or CREATE_USERS permission to: query users
//        int userCount = mUm.getUserCount();
//        Log.d(TAG, "onCreate: 用户数="+userCount);
        Log.d(TAG, "onCreate: ");

        //测语言
        String mSystemState = Locale.getDefault().toString();
        Log.d(TAG, "onCreate: mSystemState=" + mSystemState);//zh_CN

        //测任务id
        int taskId = getTaskId();
        Log.d(TAG, "onCreate: taskId=" + taskId);//395

        //测包管理器及包名等
        PackageManager mPm = applicationContext.getPackageManager();
        Intent mainIntent = new Intent();
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        String packageName = applicationContext.getPackageName();
        Log.d(TAG, "onCreate: packageName=" + packageName);//com.example.yuan.app16
        mainIntent.setPackage(packageName);
//        mainIntent.setPackage(null);
        List<ResolveInfo> infos = mPm.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info : infos) {
            Log.d(TAG, "onCreate: ResolveInfo=" + info.toString());//ResolveInfo{712d439 com.example.yuan.app16/.MainLibraryActivity m=0x108000}
        }


        //测字节,字母一个,汉字三个
        String s = "SD";
        byte[] bytes = s.getBytes();
        Log.d(TAG, "SD: " + bytes.length);//2
        s = "卡";
        bytes = s.getBytes();
        Log.d(TAG, "卡: " + bytes.length);//3


        //测Build
        boolean ATLEAST_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        Log.d(TAG, "ATLEAST_LOLLIPOP:" + ATLEAST_LOLLIPOP);//true


        String launcherAppsService = Context.LAUNCHER_APPS_SERVICE;
//        LauncherApps systemService = (LauncherApps) applicationContext.getSystemService("launcherapps");
        LauncherApps systemService = (LauncherApps) applicationContext.getSystemService(launcherAppsService);
//        systemService.startAppDetailsActivity();
//        applicationContext.startActivityAsUser();
        Intent intent;
        Bundle options;
        UserHandle user;
//        this.startActivityAsUser( intent,  options,  user);


        //测延时
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                Toast.makeText(Main11Activity.this, "3s 延时", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "run: 3s 延时");
            }
        }, 3000);

        //测域
        pm = getPackageManager();
        String packageName1 = getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName1, PackageManager.GET_ACTIVITIES);
            Log.d(TAG, "onCreate: "+ai.uid);//10085
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int currentUid = getCurrentUid();


        //获取Android正在运行的应用和它的pid
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager.getRunningAppProcesses();
        int i = 1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            Log.i("Application", (i++) + "PID: " + amProcess.pid + "(processName=" + amProcess.processName + "UID=" + amProcess.uid + ")");
//            I/Application: 1PID: 10731(processName=com.example.yuan.app16UID=10085)
        }
        getUids();



        if(false){
            //类名
            Activity ma = null;
            ma.getClass().getName();
            Fragment ff = null;
            ff.getClass().getName();
            ContentResolver cr = null;
            Context c = null;
            String name = "";
            if (cr != null) {
//                cr.acquireProvider(c, name);//
            }
        }



    }

    private int getCurrentUid() {
//        return android.os.Process.myUserHandle().getIdentifier();
        return -1;
    }




/*
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: uid = 10018
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: name = com.android.musicfx
...
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: uid = 10085
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: name = com.example.yuan.app16
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: uid = 10079
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: name = com.android.chrome
12-27 17:07:33.906 10731-10731/com.example.yuan.app16 I/test: uid = 10044
12-27 17:07:33.907 10731-10731/com.example.yuan.app16 I/test: name = com.cloudminds.music2
12-27 17:07:33.907 10731-10731/com.example.yuan.app16 I/test: uid = 10062
12-27 17:07:33.907 10731-10731/com.example.yuan.app16 I/test: name = com.android.printservice.recommendation
12-27 17:07:33.907 10731-10731/com.example.yuan.app16 I/test: uid = 10015
12-27 17:07:33.907 10731-10731/com.example.yuan.app16 I/test: name = com.android.dialer
*/
    public List getUids() {
        List<Integer> uidList = new ArrayList<Integer>();
        pm = getPackageManager();
        List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        // System.out.println(info.packageName+"访问网络");
                        int uid = info.applicationInfo.uid;
                        Log.i("test", "uid = " + uid);
                         String name = pm.getNameForUid(uid);
                         // textName.setText(name);
                         Log.i("test", "name = "+name);
                        uidList.add(uid);
                    }
                }
            }
        }

        return uidList;
    }

    public void t() {
    }
}
