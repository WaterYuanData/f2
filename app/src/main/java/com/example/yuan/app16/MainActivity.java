package com.example.yuan.app16;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.media.RingtoneManager;
import android.media.Ringtone;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;

import com.example.yuan.app16.Theme.ThemeActivity;
import com.example.yuan.app16.example1.MyActivity;
import com.example.yuan.app16.example2.Main2Activity;
import com.example.yuan.app16.gestureDetector.MyGesture;
import com.example.yuan.app16.leakMemory.LeaKMemoryActivity;
import com.example.yuan.app16.listView.ListOnLongClickActivity;
import com.example.yuan.app16.mediaProvider.MediaActivity;
import com.example.yuan.app16.qr.QrActivity;
import com.example.yuan.app16.myView.SpiralActivity;
import com.example.yuan.app16.testDialog.InputDialog;
import com.example.yuan.app16.testIncludeModule.IncludeModuleActivity;
import com.example.yuan.app16.touchEvent.Main3Activity;
import com.example.yuan.app16.file.Main5Activity;
import com.example.yuan.app16.file.MainBackUpActivity;
import com.example.yuan.app16.locationBaseService.Main16Activity;
import com.example.yuan.app16.menuAndContent.Main17Activity;
import com.example.yuan.app16.music.Main4Activity;
import com.example.yuan.app16.RecyclerViewTest.Main7Activity;
import com.example.yuan.app16.RecyclerViewTest.MyOkhttp;
import com.example.yuan.app16.progressDialog.Main12Activity;
import com.example.yuan.app16.requestPermission.Main14Activity;
import com.example.yuan.app16.downloadContinue.old_copy.Main15Activity;
import com.example.yuan.app16.testReport.Main8Activity;
import com.example.yuan.app16.testReport.Main9Activity;
import com.example.yuan.app16.user.Main11Activity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/*
http://www.cnblogs.com/liangstudyhome/p/3798638.html
 */

public class MainActivity extends Activity {

    String TAG = "MainLibraryActivity";
    public static String addressUrl="http://202.99.59.96/appLogin";
//    public static String addressUrl="http://www.12306.cn";
    public static String indexUrl="http://202.99.59.96/zf12365/index.html";

    public void ringChargingSound() {
//        final boolean enabled = DevicePolicyConst.isSettingsSeparate() ?
//                Settings.Global.getIntForUser(mContext.getContentResolver(),
//                        Settings.Global.CHARGING_SOUNDS_ENABLED, 1, getCurrentUserId()) != 0 :
//                Settings.Global.getInt(mContext.getContentResolver(),
//                        Settings.Global.CHARGING_SOUNDS_ENABLED, 1) != 0;
//        final String soundPath = Settings.Global.getString(mContext.getContentResolver(),
//                Settings.Global.WIRELESS_CHARGING_STARTED_SOUND);
        final boolean enabled = true;
        final String soundPath = "/system/media/audio/ui/WirelessChargingStarted.ogg";
        if (enabled && soundPath != null) {
            final Uri soundUri = Uri.parse("file://" + soundPath);
            if (soundUri != null) {
                Log.d(TAG, "===电uri" + soundUri);

                Ringtone sfx = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
                if (sfx != null) {
                    sfx.setStreamType(AudioManager.STREAM_SYSTEM);
                    sfx.play();
                    Log.d(TAG, "playSounds: ");
                } else {
                    Log.d(TAG, "playSounds: failed to load ringtone from uri: " + soundUri);
                }

                AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                float volumnRatio = volumnCurrent / audioMaxVolumn;
//                        IRingtonePlayer ringtonePlayer = am.getRingtonePlayer();
//                        ringtonePlayer.play(,soundUri,,volumnRatio,false);
                final MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(volumnRatio,volumnRatio);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                        }
                    }
                });
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), soundUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Log.d(TAG, "ringChargingSound: 电???");
                } catch (IOException e) {
                    Log.d(TAG, "===");
                    e.printStackTrace();
                } finally {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                }
            }
        }
    }

    @OnClick(R.id.bt18)
    public void getVersionCode(View view){
        Toast.makeText(this,getVersion(),Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt19)
    public void listViewTest(View view){
        startActivity(new Intent(this, ListOnLongClickActivity.class));
    }

    @OnClick(R.id.btn_spiral)
    public void testSpiral(View view){
        startActivity(new Intent(this, SpiralActivity.class));
    }

    @OnClick(R.id.btn_media)
    public void testMedia(View view){
        startActivity(new Intent(this, MediaActivity.class));
    }

    @OnClick(R.id.btn_leak)
    public void testLeak(View view){
        startActivity(new Intent(this, LeaKMemoryActivity.class));
    }

    @OnClick(R.id.bt20)
    public void MyGesture(View view){
        startActivity(new Intent(this, MyGesture.class));
    }

    @OnClick(R.id.bt21)
    public void testTheme(View view){
        startActivity(new Intent(this, ThemeActivity.class));
    }

    @OnClick(R.id.bt_qr)
    public void testQr(View view){
        startActivity(new Intent(this, QrActivity.class));
    }

    public void testModule(View view){
        startActivity(new Intent(this, IncludeModuleActivity.class));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void testDialog(View view){
        // AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext(), AlertDialog.THEME_HOLO_LIGHT);
        // dialogBuilder.setTitle("title");
        // dialogBuilder.setNegativeButton(R.string.cancel, null);
        // dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        //     @Override
        //     public void onClick(DialogInterface dialog, int which) {
        //         Log.d(TAG, "onClick: ");
        //     }
        // });
        // AlertDialog alertDialog = dialogBuilder.create();
        // alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        //     @Override
        //     public void onDismiss(DialogInterface dialog) {
        //         Log.d(TAG, "onDismiss: ");
        //     }
        // });
        // alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        // // DialogViewManager.fullScreenImmersive(alertDialog.getWindow().getDecorView());
        // alertDialog.show();

        startActivity(new Intent(this, InputDialog.class));
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "版本号:" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化
        ViewUtils.inject(this);
        final Context applicationContext = getApplicationContext();
//        ringChargingSound();
        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, MyActivity.class);
                startActivity(inte);
            }
        });

        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, Main2Activity.class);
                startActivity(inte);
            }
        });

        Button bt3 = (Button) findViewById(R.id.bt3);
        bt3.setText("滑动事件");
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, Main3Activity.class);
                startActivity(inte);
            }
        });

        Button bt4 = (Button) findViewById(R.id.bt4);
        bt4.setText("music");
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, Main4Activity.class);
                startActivity(inte);
            }
        });

        Button bt5 = (Button) findViewById(R.id.bt5);
        bt5.setText("file");
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, Main5Activity.class);
                startActivity(inte);
            }
        });

        Button bt6 = (Button) findViewById(R.id.bt6);
        bt6.setText("备份还原");
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(applicationContext, MainBackUpActivity.class);
                startActivity(inte);
            }
        });


        {
            Button bt = (Button) findViewById(R.id.bt7);
            bt.setText("测试recycleView");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main7Activity.class);
                    startActivity(inte);
                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt8);
            bt.setText("测试okhttp获取json");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            String result = MyOkhttp.get("http://gank.io/api/data/福利/10/1");
                            Log.d(TAG, "onClick: result=" + result);
                        }
                    }.start();
                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt9);
            bt.setText("测试webView");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main8Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt91);
            bt.setText("报表登录");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main9Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt11);
            bt.setText("测");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main11Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt12);
            bt.setText("12 测进度对话框");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main12Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt14);
            bt.setText("14 测申请权限");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main14Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt15);
            bt.setText("15 断点续传");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main15Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt16);
            bt.setText("位置服务");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main16Activity.class);
                    startActivity(inte);

                }
            });
        }
        {
            Button bt = (Button) findViewById(R.id.bt17);
            bt.setText("侧滑按钮");
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(applicationContext, Main17Activity.class);
                    startActivity(inte);

                }
            });
        }




    }

}
