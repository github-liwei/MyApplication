package com.liwei.clock.interfaceclass.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.liwei.clock.R;
import com.liwei.clock.activity.ChatActivity;
import com.liwei.clock.interfaceclass.DataC;
import com.liwei.clock.interfaceclass.InnerItemOnclickListener;

/**
 * Created by LIWEI on 2017/10/21.
 */
public class OnClickChilds implements InnerItemOnclickListener {
    private static Bundle b = new Bundle();

    //为好友列表添加事件
    @Override
    public void itemClick(View v, Activity a) {
        Intent intent = new Intent();
        intent.setClass(v.getContext(), ChatActivity.class);
        Button bt = v.findViewById(R.id.bt_child_id);
        Log.i(DataC.ITAG, "Button msg：" + bt.getText());
        b.putString(DataC.USERYOU, bt.getText().toString());
        intent.putExtras(b);
        a.startActivity(intent);
    }
}
