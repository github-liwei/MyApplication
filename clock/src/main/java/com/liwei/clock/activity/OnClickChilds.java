package com.liwei.clock.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.liwei.clock.R;
import com.liwei.clock.config.CommonData;

/**
 * Created by LIWEI on 2017/10/21.
 */
public class OnClickChilds implements InnerItemOnclickListener {
    @Override
    public void itemClick(View v) {
        Intent intent = new Intent();
        Button bt = v.findViewById(R.id.bt_child_id);
        Log.i(CommonData.ITAG, "Button msg：" + bt.getText());
        Log.i(CommonData.ITAG, "Button Id：" + bt.getId());
    }
}
