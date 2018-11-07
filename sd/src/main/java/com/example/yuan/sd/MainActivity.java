package com.example.yuan.sd;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context mApplicationContext;
    private StorageVolume[] mStorageVolumes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApplicationContext = getApplicationContext();

        testInnerSDCard();
        testSDCard();
    }

    /**
     * 内置SD卡
     */
    public void testInnerSDCard() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String string = externalStorageDirectory.toString();
        Log.d(TAG, "testInnerSDCard: externalStorageDirectory=" + string);// /storage/emulated/0

    }

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
                Log.d(TAG, "testSDCard: *****************path=" + path);
                Log.d(TAG, "testSDCard: description=" + description);
                Log.d(TAG, "testSDCard: uuid=" + uuid);
                Log.d(TAG, "testSDCard: state=" + state);
                Log.d(TAG, "testSDCard: emulated=" + emulated);
                Log.d(TAG, "testSDCard: primary=" + primary);
                Log.d(TAG, "testSDCard: removable=" + removable);
                Log.d(TAG, "testSDCard: string=" + string);
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
