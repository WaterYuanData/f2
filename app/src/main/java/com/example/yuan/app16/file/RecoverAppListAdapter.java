package com.example.yuan.app16.file;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by yuan on 17-12-14.
 */

public class RecoverAppListAdapter extends BaseAdapter {


    private static final String BACKUP_APP = "/backup/App";

    private String mRecoverPath;
    private String mBackupRootDir;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mFileNameList;
    private ArrayList<ApkInfo> mApkNameList;
    private ArrayList<Boolean> mFileCheckedList;
    private File mCurrentDir;
    private Handler mHandler;
    private static final TheComparator mAcsOrder = new TheComparator();

    public static final int MULTI_FLAG_DEFAULT = 0;

    private int mMultiFlag = MULTI_FLAG_DEFAULT;
    private ListActivity mListActivity;
    public int mMode = 0;
    public boolean mCancel = false;
    private static final String RECOVER_APP_FILE = "/backup/recoverAppData.txt";
    private String mRecoverAppDirFile;
    private Dialog mDelDialog = null;
    private Button mRestoreButton;

    // Constructor 2
    public RecoverAppListAdapter(Context context, Handler handler, String path) {
        mContext = context;
        mListActivity = (ListActivity) context;
        mInflater = LayoutInflater.from(mContext);
        mHandler = handler;
        mRecoverPath = path;
        mBackupRootDir = mRecoverPath + BACKUP_APP;
        mRecoverAppDirFile = mRecoverPath + RECOVER_APP_FILE;
    }

    public void sortImpl() {
        mCurrentDir = new File(mBackupRootDir);
        String[] mFileNameArray;
        if (mCurrentDir != null && mCurrentDir.exists()) {
            mFileNameArray = mCurrentDir.list();
            Log.d("", "FileListAdapter:mFileNameArray: " + mFileNameArray);
            mFileNameList = getSortedFileNameArray(mCurrentDir, mFileNameArray);
        } else {
            mFileNameList = new ArrayList<String>();
            mCurrentDir = null;
        }
        boolean needInitChecked = false;
        if (mFileCheckedList == null) {
            mFileCheckedList = new ArrayList<Boolean>();
            needInitChecked = true;
        }
        mFileCheckedList.clear();
        mApkNameList = new ArrayList<ApkInfo>();
        for (String p : mFileNameList) {
            ApkInfo appItem = new ApkInfo();
            try {
                PackageManager pm = mContext.getPackageManager();
                PackageInfo pakinfo = pm.getPackageArchiveInfo(mCurrentDir.getPath() + "/" + p,
                        PackageManager.GET_ACTIVITIES);
                if (pakinfo != null) {
                    ApplicationInfo appinfo = pakinfo.applicationInfo;
                    appinfo.sourceDir = mCurrentDir.getPath() + "/" + p;
                    appinfo.publicSourceDir = mCurrentDir.getPath() + "/" + p;
                    appItem.fileName = (String) pm.getApplicationLabel(appinfo);
                    appItem.appIcon = pm.getApplicationIcon(appinfo);
                    mApkNameList.add(appItem);
                } else {
                    appItem.fileName = p;
                    appItem.appIcon = null;
                    mApkNameList.add(appItem);
                }
            } catch (Exception ex) {
                Log.e("", "package:" + p + " error for " + ex);
                ex.printStackTrace();
                appItem.fileName = p;
                appItem.appIcon = null;
                mApkNameList.add(appItem);
            }
            mFileCheckedList.add(Boolean.FALSE);
        }
    }

    private ArrayList<String> getSortedFileNameArray(final File theFile, String[] filenames) {
        ArrayList<String> fileFolderArray = new ArrayList<String>();
        if (filenames != null) {
            for (String s : filenames) {
                Log.d("", "add to filelist " + s + " addable = " + (!(s.equals("path"))));
                if (s.endsWith(".apk"))
                    fileFolderArray.add(s);
            }
        }
        return fileFolderArray;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setFileCheckedList(ArrayList list){
        mFileCheckedList = list;
    }

    public ArrayList getFileNameList(){
        return mFileNameList;
    }

    public void setFileNameList(ArrayList list){
        mFileNameList = list;
    }

    public ArrayList getFileCheckedList(){
        return mFileCheckedList;
    }



    public void setRestoreButton(Button button){
        mRestoreButton = button;
    }

    public void selectAll(boolean checked) {
        for (int i = 0; i < mFileCheckedList.size(); ++i) {
            mFileCheckedList.set(i, Boolean.valueOf(checked));
        }
    }




    public int getSelectCnt() {
        int total = 0;
        for (int i = 0; i < mFileCheckedList.size(); ++i) {
            if (mFileCheckedList.get(i).booleanValue())
                total += 1;
        }
        return total;
    }



    public static class ApkInfo {
        String fileName;
        Drawable appIcon;
    }

    /**
     * This Comparator sorts strings into increasing order.
     */
    public static class TheComparator implements Comparator<String> {
        public int compare(String str1, String str2) {
            int len1 = str1.length();
            int len2 = str2.length();
            int len = len1 <= len2 ? len1 : len2;
            for (int i = 0; i < len; i++) {
                int value1 = str1.codePointAt(i);
                int value2 = str2.codePointAt(i);
                // 'A' -> 'a'
                if (value1 > 64 && value1 < 91)
                    value1 = value1 + 32;
                if (value2 > 64 && value2 < 91)
                    value2 = value2 + 32;
                if (value1 == value2)
                    continue;
                if (value1 > 256 && value2 > 256) {
                    return value1 > value2 ? -1 : 1;
                }
            }
            if (len1 == len2) {
                return 0;
            } else {
                return len1 > len2 ? -1 : 1;
            }
        }
    }
}
