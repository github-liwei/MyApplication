package com.liwei.clock.config;

import android.app.Application;
import android.util.Log;
import cn.jpush.im.android.api.JMessageClient;

/**
 * JMessage initialization
 * Created by LIWEI on 2017/10/18.
 */
public class InitJMessage extends Application {


    @Override
    public void onCreate() {
        Log.d("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
    }
}
