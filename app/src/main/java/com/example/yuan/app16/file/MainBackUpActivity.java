package com.example.yuan.app16.file;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.yuan.app16.R;
import com.example.yuan.app16.music.Main4Activity;

//类似备份主Activity
public class MainBackUpActivity extends TabActivity implements TabHost.OnTabChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main6);

        TabHost mTabHost = getTabHost();
        mTabHost.setOnTabChangedListener(this);

//        getActionBar().setDisplayShowHomeEnabled(false);
//        getActionBar().setTitle(R.string.main_backup_label);

        {
            Intent intent = new Intent("android.intent.action.BACKUP");
            intent.setClass(this, Main4Activity.class);
            mTabHost.addTab(mTabHost
                    .newTabSpec("Backup")
                    .setIndicator(getResources().getString(R.string.backup_label),
                            getResources().getDrawable(R.drawable.ic_tab_memo))
                    .setContent(intent));
        }
        {
            Intent intent = new Intent("android.intent.action.RECOVER");
            intent.setClass(this, RestoreActivity.class);
            mTabHost.addTab(mTabHost.newTabSpec("Recover")
                    .setIndicator(getResources().getString(R.string.recvoer_label),
                            getResources().getDrawable(R.drawable.ic_tab_appointment))
                    .setContent(intent));
        }



    }

    @Override
    public void onTabChanged(String tabId) {

    }
}
