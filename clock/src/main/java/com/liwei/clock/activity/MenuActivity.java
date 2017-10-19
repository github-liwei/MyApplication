package com.liwei.clock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.config.CommonData;
import com.liwei.clock.config.MyExpandableListViewAdapter;

public class MenuActivity extends AppCompatActivity {

    /*可拓展列表视图*/
    private ExpandableListView expandableListView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        //关闭监听事件
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    void initView() {
        setContentView(R.layout.activity_menu);
        expandableListView = (ExpandableListView) findViewById(R.id.elv);
        searchView = (SearchView) findViewById(R.id.search_user);

        //监听事件开启
        JMessageClient.registerEventReceiver(this);
    }

    void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                Log.i(CommonData.ITAG, "收到好友邀请");
                break;
            case invite_accepted://对方接收了你的好友邀请
                Log.i(CommonData.ITAG, "对方接收了你的好友邀请");
                break;
            case invite_declined://对方拒绝了你的好友邀请
                Log.i(CommonData.ITAG, "对方拒绝了你的好友邀请");
                break;
            case contact_deleted://对方将你从好友中删除
                Log.i(CommonData.ITAG, "对方将你从好友中删除");
                break;
            default:
                break;
        }
    }

    void initData() {
        /* 1.1 创建一个adapter实例*/
        MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(this);
        /* 1. 设置适配器*/
        expandableListView.setAdapter(adapter);


        //添加好友
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String name = s.trim();
                Log.i(CommonData.ITAG, "查询的字段为" + name);
                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "字段为空", Toast.LENGTH_LONG).show();
                } else {
                    //TODO 发送好友请求，请求说明暂时为 你好帅
                    ContactManager.sendInvitationRequest(name, null, "你好帅", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (0 == i) {
                                Toast.makeText(getApplicationContext(), "好友添加发送成功", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e(CommonData.ETAG, "gotResult:  添加失败可能无此人\n" + s);
                                Toast.makeText(getApplicationContext(), "无此人", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}
