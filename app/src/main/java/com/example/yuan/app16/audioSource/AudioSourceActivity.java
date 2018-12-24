package com.example.yuan.app16.audioSource;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.yuan.app16.R;

public class AudioSourceActivity extends AppCompatActivity {

    private static final String TAG = "AudioSourceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_source);

        registerAudioSourceReceiver();

        // User user = new User("56");
        // String id = user.getId();
        // Log.d(TAG, "onCreate: user=" + user);
        // Log.d(TAG, "onCreate: id=" + id);

    }


    private void registerAudioSourceReceiver() {
        Log.d(TAG, "registerAudioSourceReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.ACTION_HEADSET_PLUG);
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mAudioSourceReceiver, filter);
    }

    private BroadcastReceiver mAudioSourceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mAudioSourceReceiver: --action=" + action);
            if (AudioManager.ACTION_HEADSET_PLUG.equals(action)) {
                int state = intent.getIntExtra("state", 0); // 0 for unplugged, 1 for plugged.
                int microphone = intent.getIntExtra("microphone", 0); // 1 if headset has a microphone, 0 otherwise
                Log.d(TAG, "mAudioSourceReceiver: --microphone=" + microphone + " --state=" + state);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAudioSourceReceiver);
    }
}
