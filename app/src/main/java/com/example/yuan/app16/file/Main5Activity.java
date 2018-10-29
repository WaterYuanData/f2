package com.example.yuan.app16.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yuan.app16.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//在app的build中
//compile files('/home/yuan/Documents/AndroidStudioProjects/App16/libs/android-vcard.jar')
import a_vcard.android.syncml.pim.vcard.VCardComposer;


//联系人权限不仅在manifest中添加,还要在手机设置中打开
public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);






















        String sd = "/adcard/asd/contact.vcf";
        File f = new File(sd);
        if (f.exists()) {
            Toast.makeText(this, "存在", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "不存在", Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream fos = new FileOutputStream(sd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        VCardComposer vcc = new VCardComposer();

        ContentResolver mContentResolver = this.getContentResolver();

        String[] projection = new String[]{"_id"};//
        Uri contentUri = ContactsContract.RawContacts.CONTENT_URI;
        Log.d("联系人", "onCreate: contentUri=" + contentUri);//content://com.android.contacts/raw_contacts
        String selection = "(deleted=0) and (account_type='com.android.localphone')";
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor mCursor = mContentResolver.query(contentUri, projection, selection, selectionArgs, sortOrder);

        //

    }
}
