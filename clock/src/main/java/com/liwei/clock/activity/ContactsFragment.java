package com.liwei.clock.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.config.LogMy;
import com.liwei.clock.interfaceclass.DataC;
import com.liwei.clock.config.MyExpandableListViewAdapter;
import com.liwei.clock.interfaceclass.impl.OnClickChilds;

import java.util.List;

public class ContactsFragment extends Fragment {
    /*可拓展列表视图*/
    @BindView(R.id.elv)
    public ExpandableListView expandableListView;
    @BindView(R.id.search_user)
    public EditText searchView;
    @BindView(R.id.tv_user_add_message)
    public TextView tvUserAdd;
    public Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_contacts_fragment, container, false);
        activity = getActivity();
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    void initData() {
        /* 1.1 创建一个adapter实例*/
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                if (0 == i) {
                    if (list != null) {
                        MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(activity, list);
                        adapter.setOnInnerItemOnClickListener(new OnClickChilds());
                         /* 1. 设置适配器*/
                        LogMy.e(this.getClass(), "onCreate initData 列表加载");
                        expandableListView.setAdapter(adapter);
                        LogMy.e(this.getClass(), "onCreate initData列表完成");
                    }
                } else {
                    //获取好友列表失败
                    LogMy.e(this.getClass(), "onCreate initData 获取好友列表失败");
                }
            }
        });
        //添加好友
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String name = textView.getText().toString().trim();
                LogMy.e(this.getClass(), "查询的字段为" + name);
                if (name.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "字段为空", Toast.LENGTH_LONG).show();
                } else {
                    //TODO 发送好友请求，请求说明暂时为 你好帅
                    ContactManager.sendInvitationRequest(name, null, "你好帅", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (0 == i) {
                                Toast.makeText(activity.getApplicationContext(), "好友添加发送成功", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e(DataC.ETAG, "gotResult:  添加失败可能无此人\n" + s);
                                Toast.makeText(activity.getApplicationContext(), "无此人", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    searchView.clearFocus();
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getSimpleName(), "onDestroy: 正在销毁");
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.clearFocus();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
    }
}
