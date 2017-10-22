package com.liwei.clock.config;

import android.util.Log;

/**
 * Created by LIWEI on 2017/10/22.
 */
public class LogMy {

    public static void i(Class c, String msg) {
        Log.i("*** " + c.getSimpleName() + " ***", msg);
    }

    public static void e(Class c, String msg) {
        Log.e("*** " + c.getSimpleName() + " ***", msg);
    }
}
