package com.liwei.clock.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import com.liwei.clock.R;
import com.liwei.clock.config.LogMy;
import com.liwei.clock.config.UserrtAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int ADDUSUR = 0;
    public final static String action = "MainActivityAction";
    //默认页面
    private static int currentPage = 1;
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
    private TextView offlineMsgText;
    private TextView refreshEventText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserrtAssert.userInfo(this)) {
            LogMy.e(this.getClass(), "onCreate: 退出这个界面 ");
            finish();
        }
        initView();
        initData();
    }
    void initView() {
        //监听事件开启
        LogMy.e(this.getClass(), "开启事件监听");
        JMessageClient.registerEventReceiver(this);

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
        offlineMsgText = (TextView) findViewById(R.id.tv_showOfflineMsg);
        refreshEventText = (TextView) findViewById(R.id.tv_refresh_event_msg);
        messageLayout.setOnClickListener(this);
        contactsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    void initData() {
        fragmentManager = getFragmentManager();
        //在FragmentManager里面根据Tag去找相应的fragment. 用户屏幕发生旋转，重新调用onCreate方法。否则会发生重叠
        messageFragment = (MessageFragment) fragmentManager.findFragmentByTag("mes");
        contactsFragment = (ContactsFragment) fragmentManager.findFragmentByTag("con");
        newsFragment = (NewsFragment) fragmentManager.findFragmentByTag("new");
        settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("set");
        // 第一次启动时选中第0个tab
        setTabSelection(currentPage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogMy.e(this.getClass(), "onDestroy: 正在销毁");
        LogMy.e(this.getClass(), "onDestroy: 关闭 JMessageClient 监听");
        JMessageClient.unRegisterEventReceiver(this);
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
                    transaction.add(R.id.main_context, messageFragment, "mes");
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
                    transaction.add(R.id.main_context, contactsFragment, "con");
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
                    transaction.add(R.id.main_context, newsFragment, "new");
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
                    transaction.add(R.id.main_context, settingFragment, "set");
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }
                break;
        }
        this.currentPage = index;
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
    public void onEvent(ContactNotifyEvent event) {
        String fromUsername = event.getFromUsername();
        if (ContactsFragment.tvUserAdd == null) {
            LogMy.e(this.getClass(), "页面还没初始化");
        } else {
            switch (event.getType()) {
                case invite_received://收到好友邀请
                    LogMy.e(this.getClass(), "收到好友邀请" + fromUsername);
                    ADDUSUR = ADDUSUR++;
                    ContactsFragment.tvUserAdd.setText(ADDUSUR);
                    break;
                case invite_accepted://对方接收了你的好友邀请
                    LogMy.e(this.getClass(), "对方接收了你的好友邀请");
                    ADDUSUR = ADDUSUR++;
                    ContactsFragment.tvUserAdd.setText(ADDUSUR);
                    break;
                case invite_declined://对方拒绝了你的好友邀请
                    LogMy.e(this.getClass(), "对方拒绝了你的好友邀请");
                    ADDUSUR = ADDUSUR++;
                    break;
                case contact_deleted://对方将你从好友中删除
                    LogMy.e(this.getClass(), "对方将你从好友中删除");
                    break;
                default:
                    break;
            }
        }
    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case custom:
                final ConversationType targetType = event.getMessage().getTargetType();
                final Intent intent = new Intent(action);
                CustomContent customContent = (CustomContent) msg.getContent();
                Map allStringValues = customContent.getAllStringValues();
                if (targetType.equals(ConversationType.group)) {
                    //TODO 还未实现
                    LogMy.e(this.getClass(), "MessageEvent 来个组消息");
//                    intent.putExtra(CREATE_GROUP_CUSTOM_KEY, allStringValues.toString());
//                    intent.setFlags(1);
                } else if (targetType.equals(ConversationType.single)) {
                    String _userYou = ChatActivity.userYou.trim();
                    if (_userYou.equals("")) {
                        LogMy.e(this.getClass(), "MessageEvent userYou为空");
                    } else {
                        UserInfo fromUser = msg.getFromUser();
                        if (fromUser.getUserName().equals(_userYou)) {
                            LogMy.e(this.getClass(), "MessageEvent 来个single消息");
                            intent.putExtra(ChatActivity.CHATKEY, allStringValues.toString());
                            sendBroadcast(intent);
                        }
                        LogMy.e(this.getClass(), "MessageEvent 您不在这个聊天窗口所以无法刷新页面");
                    }
                }
                break;
            //其实sdk是会自动下载语音的.本方法是当sdk自动下载失败时可以手动调用进行下载而设计的.同理于缩略图下载
            case voice:
                LogMy.e(this.getClass(), "接收到了语音消息");
                break;
            case eventNotification:
                LogMy.e(this.getClass(), "接收到了 eventNotification");
                break;
            case image:
                LogMy.e(this.getClass(), "接收到了图片消息");
                break;
            default:
                break;
        }
    }

    //离线消息事件
    public void onEventMainThread(OfflineMessageEvent event) {
        Intent intent = new Intent(MainActivity.action);
        Conversation conversation = event.getConversation();
        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
        List<Integer> offlineMsgIdList = new ArrayList<>();
        if (conversation != null && newMessageList != null) {
            for (Message msg : newMessageList) {
                offlineMsgIdList.add(msg.getId());
            }

            String _userYou = ChatActivity.userYou.trim();
            if (_userYou.equals("")) {
                LogMy.e(this.getClass(), "OfflineMessageEvent userYou为空");
            } else {
                LogMy.e(this.getClass(), "OfflineMessageEvent 来个离线single消息");
                intent.putExtra(ChatActivity.CHATKEY, "离线消息个数" + newMessageList.size());
                sendBroadcast(intent);
            }
            offlineMsgText.append(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
            offlineMsgText.append("会话类型 = " + conversation.getType() + "\n消息ID = " + offlineMsgIdList + "\n\n");
        } else {
            offlineMsgText.setText("conversation is null or new message list is nul");
        }
    }

    //漫游消息
    public void onEventMainThread(ConversationRefreshEvent event) {
        Conversation conversation = event.getConversation();
        ConversationRefreshEvent.Reason reason = event.getReason();
        if (conversation != null) {
            refreshEventText.append(String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
            refreshEventText.append("事件发生的原因 : " + reason + "\n");
        } else {
            refreshEventText.setText("conversation is null");
        }
    }


    /**
     * 在一个主界面(主Activity)上能连接往许多不同子功能模块(子Activity上去)，当子模块的事情做完之后就回到主界面，
     * 或许还同时返回一些子模块完成的数据交给主Activity处理。这样的数据交流就要用到回调函数onActivityResult。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 8) {
            offlineMsgText.setText("");
            refreshEventText.setText("");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
