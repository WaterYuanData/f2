package com.example.yuan.app16.mediaProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.yuan.app16.R;
import com.example.yuan.app16.util.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";
    public static final Uri PHOTO_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final String[] PHOTO_QUERY_PROJECTION = {
            MediaStore.Images.Media._ID,           // 0, int
            MediaStore.Images.Media.DATE_TAKEN,    // 1, int
            MediaStore.Images.Media.DATA,          // 2, string
            MediaStore.Images.Media.DISPLAY_NAME,  // 3, string
    };
    public static final String PHOTO_QUERY_ORDER = MediaStore.Images.Media.DATE_TAKEN + " DESC, "
            + MediaStore.Images.Media._ID + " DESC";

    static final String DIRECTORY_DCIM =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    // Default camera directory in rom storage
    static final String CAMERA_DIRECTORY = DIRECTORY_DCIM + "/Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        test();
    }

    public void test() {
        Log.d(TAG, "test: MediaStore.Images.Media.EXTERNAL_CONTENT_URI=" + PHOTO_CONTENT_URI);

        // Strings[]转ArrayList
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < PHOTO_QUERY_PROJECTION.length; i++) {
            arrayList.add(PHOTO_QUERY_PROJECTION[i]);
        }
        Log.d(TAG, "test: " + arrayList.toString());// [_id, datetaken, _data, _display_name]
        // ArrayList转Strings[]
        String[] array = new String[arrayList.size()];
        arrayList.toArray(array);
        Log.d(TAG, "test: " + array.toString());// [Ljava.lang.String;@cc77e84
        for (int i = 0; i < array.length; i++) {
            Log.d(TAG, "test: array[i]=" + array[i]);// _id等
        }


        /**
         * 检查权限
         * 申请权限
         * */
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int requestCode = 0;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            return;
        }


        /**
         * Android判断是否存在外置SD卡（获取手机所有存储设备的路径）
         * https://www.jianshu.com/p/7609d5a62c45
         * */
        File externalStorageDirectory = Environment.getExternalStorageDirectory();// /storage/emulated/0
        Log.d(TAG, "test: externalStorageDirectory=" + externalStorageDirectory.toString());
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);// /storage/emulated/0/DCIM
        Log.d(TAG, "test: externalStoragePublicDirectory=" + externalStoragePublicDirectory.toString());
        StorageManager storageManager = (StorageManager) getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        for (int i = 0; i < storageVolumes.size(); i++) {
            Log.d(TAG, "test: getDescription=" + storageVolumes.get(i).getDescription(getApplicationContext()));
        }
        // 通过反射得到SD卡路径
        ArrayList<Volume> list_storagevolume = new ArrayList<Volume>();
        try {
            Method method_volumeList = StorageManager.class.getMethod("getVolumeList");
            method_volumeList.setAccessible(true);
            Object[] volumeList = (Object[]) method_volumeList.invoke(storageManager);
            if (volumeList != null) {
                Volume volume;
                for (int i = 0; i < volumeList.length; i++) {
                    try {
                        volume = new Volume();
                        volume.setPath((String) volumeList[i].getClass().getMethod("getPath").invoke(volumeList[i]));
                        volume.setRemovable((boolean) volumeList[i].getClass().getMethod("isRemovable").invoke(volumeList[i]));
                        volume.setState((String) volumeList[i].getClass().getMethod("getState").invoke(volumeList[i]));
                        list_storagevolume.add(volume);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        ArrayList<Volume> list_volume = new ArrayList<>();
        list_volume.addAll(list_storagevolume);
        for (int i = 0; i < list_volume.size(); i++) {
            Log.e(TAG, "path:" + list_volume.get(i).getPath() + "----" +
                    "removable:" + list_volume.get(i).isRemovable() + "---" +
                    "state:" + list_volume.get(i).getState());
        }


        /**
         * final class Media implements ImageColumns
         * interface ImageColumns extends MediaColumns
         * interface MediaColumns extends BaseColumns
         * */
        String selection = MediaStore.Images.Media.MIME_TYPE + "='image/jpeg' AND (" +
                MediaStore.Images.Media.DATA + " like ? OR " + MediaStore.Images.Media.DATA + " like ? )";
        String string = CAMERA_DIRECTORY + "/%";
        String[] selectionArgs = {string};// /storage/emulated/0/DCIM/Camera/%
        Uri photoUri = PHOTO_CONTENT_URI.buildUpon().appendQueryParameter("limit", "1").build();
        Cursor query = getContentResolver().query(photoUri, PHOTO_QUERY_PROJECTION, selection, selectionArgs, PHOTO_QUERY_ORDER);

        String[] selectionArgs2 = new String[list_volume.size()];
        for (int i = 0; i < list_volume.size(); i++) {
            selectionArgs2[i] = list_volume.get(i).path + "/DCIM/Camera/%";
        }
        Utils.printArray(selectionArgs2);
        Cursor cursor = getContentResolver().query(PHOTO_CONTENT_URI, PHOTO_QUERY_PROJECTION, selection, selectionArgs, PHOTO_QUERY_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                Log.d(TAG, "test: name=" + name);
            }
        }


    }

    /**
     * 存储设备信息封装类
     */
    public static class Volume {
        protected String path;
        protected boolean removable;
        protected String state;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isRemovable() {
            return removable;
        }

        public void setRemovable(boolean removable) {
            this.removable = removable;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
