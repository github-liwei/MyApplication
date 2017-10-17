package com.liwei.myapp.AppActivity;

import android.app.Application;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ${chenyn} on 16/3/22.
 *
 */
public class IMDebugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
    }
}

