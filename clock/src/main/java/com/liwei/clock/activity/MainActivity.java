package com.liwei.clock.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import com.liwei.clock.R;
import com.liwei.clock.config.CommonData;
import com.liwei.clock.config.UserrtAssert;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int ADDUSUR = 0;

    /**
     * 用于展示 消息、好友列表、新消息、设置 的Fragment
     */
    private MessageFragment messageFragment;
    private ContactsFragment contactsFragment;
    private NewsFragment newsFragment;
    private SettingFragment settingFragment;
    /**
     * 消息、好友列表、新消息、设置 界面布局
     */
    private View messageLayout;
    private View contactsLayout;
    private View newsLayout;
    private View settingLayout;

    /**
     * 在Tab布局上显示 消息、好友列表、新消息、设置 图标的控件
     */
    private ImageView messageImage;
    private ImageView contactsImage;
    private ImageView newsImage;
    private ImageView settingImage;

    /**
     * 在Tab布局上显示 消息、好友列表、新消息、设置 标题的控件
     */
    private TextView messageText;
    private TextView contactsText;
    private TextView newsText;
    private TextView settingText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserrtAssert.userInfo(getApplicationContext())) {
            Log.i(CommonData.ETAG, "onCreate: 退出这个界面 ");
            finish();
        }

        //监听事件开启
        Log.i(CommonData.ITAG, "开启事件监听");
        JMessageClient.registerEventReceiver(this);

        initView();
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //关闭监听事件
        JMessageClient.unRegisterEventReceiver(this);
    }

    void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        messageLayout = findViewById(R.id.message_layout);
        contactsLayout = findViewById(R.id.contacts_layout);
        newsLayout = findViewById(R.id.news_layout);
        settingLayout = findViewById(R.id.setting_layout);
        messageImage = (ImageView) findViewById(R.id.message_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        newsImage = (ImageView) findViewById(R.id.news_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        messageText = (TextView) findViewById(R.id.message_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        newsText = (TextView) findViewById(R.id.news_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        messageLayout.setOnClickListener(this);
        contactsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.contacts_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.news_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                //TODO 图片还没来 当点击了消息tab时，改变控件的图片和文字颜色
                //messageImage.setImageResource(R.drawable.message_selected);
                messageText.setTextColor(Color.WHITE);
                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.main_context, messageFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                //contactsImage.setImageResource(R.drawable.contacts_selected);
                contactsText.setTextColor(Color.WHITE);
                if (contactsFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    contactsFragment = new ContactsFragment();
                    transaction.add(R.id.main_context, contactsFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(contactsFragment);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                //newsImage.setImageResource(R.drawable.news_selected);
                newsText.setTextColor(Color.WHITE);
                if (newsFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.main_context, newsFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(newsFragment);
                }
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                //settingImage.setImageResource(R.drawable.setting_selected);
                settingText.setTextColor(Color.WHITE);
                if (settingFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.main_context, settingFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        messageImage.setImageResource(android.R.drawable.ic_dialog_map);
        messageText.setTextColor(Color.parseColor("#82858b"));
        contactsImage.setImageResource(android.R.drawable.ic_dialog_map);
        contactsText.setTextColor(Color.parseColor("#82858b"));
        newsImage.setImageResource(android.R.drawable.ic_dialog_map);
        newsText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(android.R.drawable.ic_dialog_map);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }

    //监听好友添加
    void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();
        if (ContactsFragment.tvUserAdd == null) {
            Log.e(CommonData.ETAG, "页面还没初始化");
        } else {
            switch (event.getType()) {
                case invite_received://收到好友邀请
                    Log.i(CommonData.ITAG, "收到好友邀请" + fromUsername);
                    ADDUSUR = ADDUSUR++;
                    ContactsFragment.tvUserAdd.setText(ADDUSUR);
                    break;
                case invite_accepted://对方接收了你的好友邀请
                    Log.i(CommonData.ITAG, "对方接收了你的好友邀请");
                    ADDUSUR = ADDUSUR++;
                    ContactsFragment.tvUserAdd.setText(ADDUSUR);
                    break;
                case invite_declined://对方拒绝了你的好友邀请
                    Log.i(CommonData.ITAG, "对方拒绝了你的好友邀请");
                    ADDUSUR = ADDUSUR++;
                    break;
                case contact_deleted://对方将你从好友中删除
                    Log.i(CommonData.ITAG, "对方将你从好友中删除");
                    break;
                default:
                    break;
            }
        }
    }

}
