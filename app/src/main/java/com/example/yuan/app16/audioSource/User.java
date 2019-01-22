package com.example.yuan.app16.audioSource;

import android.util.Log;

public class User {
    private static final String TAG = "User";

    /*
    12-04 13:31:14.461 21923-21923/com.example.yuan.app16 D/User: User: 111111111
    User: 111111111.111111 null
    User: 222222222
    User: 33333 56
    User: 1111.3333 com.example.yuan.app16.audioSource.User@6991618
12-04 13:31:14.461 21923-21923/com.example.yuan.app16 D/AudioSourceActivity: onCreate: user=com.example.yuan.app16.audioSource.User@279e871
    onCreate: id=null
     */

    private final int id;
    private String name;

    public User(int id,String name,String book) {
        this(id,name);
        Log.d(TAG, "User: book");
    }

    public int getId() {
        return id;
    }

    User(int id, String name) {
        Log.d(TAG, "User: 222222222");
        this.id = id;
        Log.d(TAG, "User: 33333 "+getId());
        this.name = name;
    }
}
