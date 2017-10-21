package com.liwei.clock.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import com.liwei.clock.activity.LoginActivity;

/**
 * Created by LIWEI on 2017/10/20.
 */
public class UserrtAssert {
    public static boolean userInfo(Activity context) {
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo == null) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
