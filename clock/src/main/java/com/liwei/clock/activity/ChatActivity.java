package com.liwei.clock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import com.google.gson.jpush.Gson;
import com.liwei.clock.R;
import com.liwei.clock.config.ChatListViewAdapter;
import com.liwei.clock.config.LogMy;
import com.liwei.clock.interfaceclass.DataC;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public final static String CHATKEY = "ChatData";
    public static String userYou = "";

    private ListView listView;

    private Button btSendMsg;
    private EditText etMsg;
    private ChatListViewAdapter adapter;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (adapter != null) {

                //(String) intent.getExtras().get(CHATKEY),Message.class;
                //LogMy.e(this.getClass(), " 我自己创建的message " + message);
                //adapter.addMessage(message);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(this.getClass().getSimpleName(), "onCreate: 正在创建");
        setContentView(R.layout.activity_chat);

        //配置广播接收
        IntentFilter filter = new IntentFilter(MainActivity.action);
        registerReceiver(broadcastReceiver, filter);

        initView();
        initData();
    }

    void initView() {
        btSendMsg = (Button) findViewById(R.id.bt_send);
        etMsg = (EditText) findViewById(R.id.et_meg);
        listView = (ListView) findViewById(R.id.lv_chat_message);
    }

    void initData() {
        //得到userYou信息
        Bundle bundle = getIntent().getExtras();
        userYou = bundle.getString(DataC.USERYOU);

        btSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _msg = etMsg.getText().toString();
                if (_msg != null) {
                    final Message message = JMessageClient.createSingleTextMessage(userYou, null, _msg);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(this.getClass().getSimpleName(), "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                                adapter.addMessage(message);
                                etMsg.setText("");
                            } else {
                                Log.i(this.getClass().getSimpleName(), "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    MessageSendingOptions options = new MessageSendingOptions();
                    options.setRetainOffline(true);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
                    options.setShowNotification(true);//是否让对方展示sdk默认的通知栏通知
                    options.setCustomNotificationEnabled(false);//是否需要自定义对方收到这条消息时sdk默认展示的通知栏中的文字
                    //发送消息
                    JMessageClient.sendMessage(message, options);
                } else {
                    Toast.makeText(getApplicationContext(), "未输入内容", Toast.LENGTH_LONG).show();
                }
            }
        });
        LogMy.e(this.getClass(), "onCreate : userYou = " + userYou);
        getMessage(userYou, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //moveTaskToBack(true);
            Intent i = new Intent();
            i.setClass(getApplication(), MainActivity.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param s1 user的userName
     * @param s2 group的id
     */
    void getMessage(String s1, String s2) {
        // TODO 获取组消息
        // String getConversationId = mEt_group_id.getText().toString();
        String getConversation = s1;
        Conversation conversation;
        List<Message> _messages;
        if (!TextUtils.isEmpty(getConversation) && TextUtils.isEmpty(s2)) {
            conversation = JMessageClient.getSingleConversation(getConversation);
        } else if (TextUtils.isEmpty(getConversation) && !TextUtils.isEmpty(s2)) {
            long groupId = Long.parseLong(s2);
            conversation = JMessageClient.getGroupConversation(groupId);
        } else {
            Toast.makeText(getApplicationContext(), "输入userName或groupID", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder str = new StringBuilder();
        str.append(" ");
        //输出接收的消息
        if (conversation != null) {
            _messages = conversation.getAllMessage();
            adapter = new ChatListViewAdapter(this, _messages);

            LogMy.e(this.getClass(), "onCreate initData 列表加载");
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            LogMy.e(this.getClass(), "onCreate initData 列表完成");

        } else {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogMy.e(this.getClass(), "onRestart: 正在重新启动");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogMy.e(this.getClass(), "onStop: userYou = " + userYou);
        LogMy.e(this.getClass(), "onStop: 正在停止");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogMy.e(this.getClass(), "onDestroy: 正在销毁");
        unregisterReceiver(broadcastReceiver);
        LogMy.e(this.getClass(), "onDestroy: 关闭广播服务");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user_key", userYou);
        userYou = "";
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userYou = (String) savedInstanceState.getSerializable("dataset");
    }
}
