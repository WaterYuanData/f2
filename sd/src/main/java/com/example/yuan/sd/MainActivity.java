package com.example.yuan.sd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
// import android.os.SystemProperties;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int WRITE_REQUEST_CODE = 1;
    private static final int REQUEST_VOLUME_PERMISSION = 2;
    private static final int REQUEST_DOCUMENT_PERMISSION = 3;
    private Context mApplicationContext;
    private StorageVolume[] mStorageVolumes;
    private Uri currentUri;
    private Button mClearButton;
    private SharedPreferences mMyPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplicationContext = getApplicationContext();

        // testInnerSDCard();
        // testSDCard();
        initUI();
        testSAF();
    }

    public void initUI() {
        mMyPreference = getSharedPreferences("MyPreference", Context.MODE_PRIVATE);

        mClearButton = (Button) findViewById(R.id.clear);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFile();
                removeURIPermission();
            }
        });
    }

    /**
     * 存储访问框架
     * https://developer.android.com/guide/topics/providers/document-provider?hl=zh-cn#overview 官网
     * https://www.jianshu.com/p/4ebc69f4e639 答疑
     * https://www.jianshu.com/p/91b2b7edcfb7 第三方应用在使用SAF时必须引导用户选择在外置SD卡的根目录进行权限授予
     * https://stackoverrun.com/cn/q/10075297 例子onActivityResult
     * https://www.jaylin.top/2018/11/26/Android-Storage-Access-Framework/ 文件授权 广播接收
     * https://stackoverflow.com/questions/46358378/how-to-use-mediarecorder-to-save-video-file-to-com-android-externalstorage-docum
     */
    public void testSAF() {
        detectURIPermission();
        // registerSDCardReceiver();
    }

    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * https://www.jaylin.top/2018/11/26/Android-Storage-Access-Framework/
     */
    private BroadcastReceiver mSDCardReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                // BroadcastReceiver has already cached the MEDIA_MOUNTED
                // notification Intent in mediaMountedIntent
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StorageVolume volume = intent.getParcelableExtra(StorageVolume.EXTRA_STORAGE_VOLUME);
                    Intent accessIntent = volume.createAccessIntent(null);//传NULL代表整个分区目录授予权限
                    startActivityForResult(accessIntent, REQUEST_VOLUME_PERMISSION);
                }
            }
        }
    };

    private void registerSDCardReceiver() {
        Log.d(TAG, "registerSDCardReceiver: ");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        this.registerReceiver(mSDCardReceiver, filter);
    }

    private void startOpenDocumentTree() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_DOCUMENT_PERMISSION);
    }

    private void detectURIPermission() {
        // 获取已授权的URI列表
        List<UriPermission> persistedUriPermissions = getContentResolver().getPersistedUriPermissions();
        for (int i = 0; i < persistedUriPermissions.size(); i++) {
            UriPermission uriPermission = persistedUriPermissions.get(i);
            Log.d(TAG, "onActivityResult: uriPermission=" + uriPermission.toString());
        }
        if (persistedUriPermissions.size() <= 0) {
            startOpenDocumentTree();
        }
    }

    private void saveURIPermission(Uri uri) {
        String uriString = uri.toString();
        Log.d(TAG, "saveURIPermission: uriString=" + uriString);
        mMyPreference.edit().putString("myUri", uriString).apply();
        int lastIndexOf = uriString.lastIndexOf("/");
        int lastIndexOf2 = uriString.lastIndexOf("%");
        String uuid = uriString.substring(lastIndexOf + 1, lastIndexOf2);
        Log.d(TAG, "saveURIPermission: uuid=" + uuid);
        mMyPreference.edit().putString("uuid", uuid).apply();
        try {
            // 为了下次不用反复请求权限的问题,保存授权在系统了，即使reboot了也依然存在，除非clear APP data.
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: 不可保存的授权 " + uri);
            e.printStackTrace();
        }
    }

    /**
     * DocumentFile可新建 文件夹 文件
     * File 不可以
     * MediaRecorder如何保存文件到DocumentFile todo
     */
    public void testFile() {
        String uuid = mMyPreference.getString("uuid", "");
        String sdPath = "/storage";
        sdPath = sdPath + File.separator + uuid;
        File file = new File(sdPath);
        boolean canWrite = file.canWrite();
        Log.d(TAG, "testFile: canWrite" + canWrite);

        sdPath += File.separator + "a.txt";
        File txtFile = new File(sdPath);
    }

    /**
     * String与Uri的相互转换
     */
    private void removeURIPermission() {
        String myUri = mMyPreference.getString("myUri", "");
        Uri uri = Uri.parse(myUri);
        if (currentUri != null && !uri.toString().equals(currentUri.toString())) {
            Log.e(TAG, "removeURIPermission: 不相等 ???");
        }
        try {
            // 取消授权
            getContentResolver().releasePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 或者
            revokeUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Log.d(TAG, "removeURIPermission: 已取消授权");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "removeURIPermission: 不能取消已取消的授权");
        }
    }

    /**
     * https://blog.csdn.net/ypz_ghost/article/details/39433361
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_DOCUMENT_PERMISSION) {
                if (data != null) {
                    currentUri = data.getData();
                    saveURIPermission(currentUri);
                    Log.d(TAG, "onActivityResult: currentUri=" + currentUri);

                    DocumentFile pickedDir = DocumentFile.fromTreeUri(this, currentUri);
                    DocumentFile newDir = pickedDir.createDirectory("MyDCIM");
                    boolean canWrite = newDir.canWrite();
                    Log.d(TAG, "onActivityResult: canWrite=" + canWrite);

                }
            }
        }
    }

    /**
     * 内置SD卡
     */
    public void testInnerSDCard() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String string = externalStorageDirectory.toString();
        Log.d(TAG, "testInnerSDCard: externalStorageDirectory=" + string);// /storage/emulated/0

    }


    /*
    01-22 18:06:40.931 13111-13111/com.example.yuan.sd D/MainActivity: testSDCard: StorageVolume size=2
    testSDCard: *****************path=/storage/emulated/0
    testSDCard: description=Internal shared storage
    testSDCard: uuid=null
    testSDCard: string=StorageVolume: Internal shared storage
    testSDCard: state=mounted
    testSDCard: emulated=true
    testSDCard: primary=true
    testSDCard: removable=false
    testSDCard: maxFileSize=0
    testSDCard: *****************path=/storage/5F79-5C43
    testSDCard: description=disk
    testSDCard: uuid=5F79-5C43
    testSDCard: string=StorageVolume: disk (5F79-5C43)
    testSDCard: state=mounted
    testSDCard: emulated=false
    testSDCard: primary=false
    testSDCard: removable=true
    testSDCard: maxFileSize=0
01-22 18:06:40.931 13111-13111/com.example.yuan.sd E/MainActivity: testSDCard:
01-22 18:06:40.932 13111-13111/com.example.yuan.sd D/MainActivity: testSDCard: mkdir=false mkdirs1=false
    testSDCard: --------------path1=/storage/5F79-5C43/DCIM
    testSDCard: canRead=false
    testSDCard: canWrite=false
    testSDCard: freeSpace=6949339136
    testSDCard: directory=true
    testSDCard: setWritable=true
    testSDCard: canWrite=false
01-22 18:06:40.933 13111-13111/com.example.yuan.sd D/MainActivity: testSDCard: --------------getPath=/storage/5F79-5C43/a.txt
    testSDCard: canWrite1=false
01-22 18:06:40.933 13111-13111/com.example.yuan.sd E/MainActivity: testSDCard:
01-22 18:06:40.934 13111-13111/com.example.yuan.sd D/MainActivity: testSDCard: /storage/emulated/0
    testSDCard: true
    testSDCard: false
01-22 18:06:40.936 13111-13111/com.example.yuan.sd D/MainActivity: testSDCard: /storage/emulated/0/DCIM
    testSDCard: true
    testSDCard: false
    * */

    /**
     * 内外置SD卡
     */
    public void testSDCard() {
        StorageManager storageManager = (StorageManager) mApplicationContext.getSystemService(Context.STORAGE_SERVICE);
        // final StorageVolume[] volumes = storageManager.getVolumeList();// 被移除或者隐藏的方法
        try {
            // 反射调用隐藏的方法,为啥CameraActivity中不反射却可以 todo
            Method getVolumeList = storageManager.getClass().getMethod("getVolumeList");
            mStorageVolumes = (StorageVolume[]) getVolumeList.invoke(storageManager);
            Log.d(TAG, "testSDCard: StorageVolume size=" + mStorageVolumes.length);
            for (int i = 0; i < mStorageVolumes.length; i++) {
                StorageVolume volume = mStorageVolumes[i];
                String state = volume.getState();
                String description = volume.getDescription(mApplicationContext);
                String uuid = volume.getUuid();
                boolean emulated = volume.isEmulated();
                boolean primary = volume.isPrimary();
                boolean removable = volume.isRemovable();
                String string = volume.toString();
                Method getPath = volume.getClass().getMethod("getPath");
                String path = (String) getPath.invoke(volume);
                Method getMaxFileSize = volume.getClass().getMethod("getMaxFileSize");
                long maxFileSize = (long) getMaxFileSize.invoke(volume);
                Log.d(TAG, "testSDCard: *****************path=" + path);
                Log.d(TAG, "testSDCard: description=" + description);
                Log.d(TAG, "testSDCard: uuid=" + uuid);
                Log.d(TAG, "testSDCard: string=" + string);
                Log.d(TAG, "testSDCard: state=" + state);
                Log.d(TAG, "testSDCard: emulated=" + emulated);
                Log.d(TAG, "testSDCard: primary=" + primary);
                Log.d(TAG, "testSDCard: removable=" + removable);
                Log.d(TAG, "testSDCard: maxFileSize=" + maxFileSize / 1024 / 1024);

                // testSDCard: canRead=true
                // testSDCard: canWrite=false
                // testSDCard: freeSpace=12137955328
                // testSDCard: directory=true
                // testSDCard: setWritable=true
                // testSDCard: canWrite=false

                if (!path.contains("emulated")) {
                    Log.e(TAG, "testSDCard: ");
                    File file = new File(path + "/DCIM");// path1=/storage/B907-2A51/DCIM
                    // /storage/B907-2A51/DCIM/Camera
                    // SDCard directory  --mkdirs=true
                    String path1 = file.getPath();
                    boolean mkdir = file.mkdir();
                    boolean mkdirs1 = file.mkdirs();
                    Log.d(TAG, "testSDCard: mkdir=" + mkdir + " mkdirs1=" + mkdirs1);// mkdir=false mkdirs1=false
                    boolean canRead = file.canRead();
                    boolean canWrite = file.canWrite();
                    long freeSpace = file.getFreeSpace();
                    Log.d(TAG, "testSDCard: --------------path1=" + path1);
                    Log.d(TAG, "testSDCard: canRead=" + canRead);
                    Log.d(TAG, "testSDCard: canWrite=" + canWrite);
                    Log.d(TAG, "testSDCard: freeSpace=" + freeSpace);
                    boolean directory = file.isDirectory();
                    boolean setWritable = file.setWritable(true);
                    canWrite = file.canWrite();
                    Log.d(TAG, "testSDCard: directory=" + directory);
                    Log.d(TAG, "testSDCard: setWritable=" + setWritable);
                    Log.d(TAG, "testSDCard: canWrite=" + canWrite);

                    File file1 = new File(path + "/a.txt");
                    // boolean newFile = file1.createNewFile();
                    boolean canWrite1 = file1.canWrite();
                    Log.d(TAG, "testSDCard: --------------getPath=" + file1.getPath());
                    // Log.d(TAG, "testSDCard: newFile=" + newFile);
                    Log.d(TAG, "testSDCard: canWrite1=" + canWrite1);

                    Log.e(TAG, "testSDCard: ");
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    boolean exists = externalStorageDirectory.exists();
                    boolean canWrite2 = externalStorageDirectory.canWrite();
                    Log.d(TAG, "testSDCard: " + externalStorageDirectory.toString());
                    Log.d(TAG, "testSDCard: " + exists);
                    Log.d(TAG, "testSDCard: " + canWrite2);
                    File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    boolean exists1 = externalStoragePublicDirectory.exists();
                    boolean canWrite3 = externalStoragePublicDirectory.canWrite();
                    Log.d(TAG, "testSDCard: " + externalStoragePublicDirectory.toString());
                    Log.d(TAG, "testSDCard: " + exists1);
                    Log.d(TAG, "testSDCard: " + canWrite3);


                    boolean isAppDebuggable = (getApplication().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
                    // boolean isApiWarningEnabled = SystemProperties.getInt("ro.art.hiddenapi.warning", 0) == 1;
                    Log.e(TAG, "onShutterButtonClick: isAppDebuggable=" + isAppDebuggable);
                    // Log.e(TAG, "onShutterButtonClick: isApiWarningEnabled=" + isApiWarningEnabled);

                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (mStorageVolumes.length > 1) {
            String uuid = mStorageVolumes[1].getUuid();
            String fileSystem = getOuterSDCardFileSystemType(uuid);
            Log.d(TAG, "testFileSystemType: fileSystem=" + fileSystem);
        }
    }

    /**
     * 外置SD卡
     * getOuterSDCardFileSystemType: line=/dev/block/vold/public:179,1 on /mnt/media_rw/F588-1226 type exfat (rw,dirsync,nosuid,nodev,noexec,noatime,uid=1023,gid=1023,fmask=0007,dmask=0007,allow_utime=0020,codepage=cp437,iocharset=utf8,namecase=0,errors=remount-ro)
     * getOuterSDCardFileSystemType: line=/mnt/media_rw/F588-1226 on /mnt/runtime/default/F588-1226 type sdcardfs (rw,nosuid,nodev,noexec,noatime,fsuid=1023,fsgid=1023,gid=1015,mask=6)
     * getOuterSDCardFileSystemType: line=/mnt/media_rw/F588-1226 on /mnt/runtime/read/F588-1226 type sdcardfs (rw,nosuid,nodev,noexec,noatime,fsuid=1023,fsgid=1023,gid=9997,mask=18)
     * getOuterSDCardFileSystemType: line=/mnt/media_rw/F588-1226 on /mnt/runtime/write/F588-1226 type sdcardfs (rw,nosuid,nodev,noexec,noatime,fsuid=1023,fsgid=1023,gid=9997,mask=18)
     * getOuterSDCardFileSystemType: line=/mnt/media_rw/F588-1226 on /storage/F588-1226 type sdcardfs (rw,nosuid,nodev,noexec,noatime,fsuid=1023,fsgid=1023,gid=1015,mask=6)
     */
    public static String getOuterSDCardFileSystemType(String sdCardUuid) {
        if (sdCardUuid != null) {
            Process mount = null;
            BufferedReader reader = null;
            try {
                mount = Runtime.getRuntime().exec("mount");
                reader = new BufferedReader(new InputStreamReader(mount.getInputStream()));
                mount.waitFor();
                Log.d(TAG, "getOuterSDCardFileSystemType: =================================================");
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(sdCardUuid)) {
                        Log.d(TAG, "getOuterSDCardFileSystemType: line=" + line);
                    }
                    String[] split = line.split("\\s+");
                    int length = split.length;
                    for (int i = 0; i < length; i++) {
                        if (split[i].endsWith(sdCardUuid) && (i + 2 < length) && !split[i + 2].contains(sdCardUuid)) {
                            return split[i + 2];
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mount != null) {
                    mount.destroy();
                }
            }
        }
        return null;
    }
}
