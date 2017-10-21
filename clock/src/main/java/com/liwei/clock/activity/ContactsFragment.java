package com.liwei.clock.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.config.CommonData;
import com.liwei.clock.config.MyExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    /*可拓展列表视图*/
    private ExpandableListView expandableListView;
    private List<UserInfo> childs;

    private SearchView searchView;
    public static TextView tvUserAdd;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Layout = inflater.inflate(R.layout.activity_contacts_fragment, container, false);
        activity = getActivity();

        return Layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }


    void initView() {
        expandableListView = activity.findViewById(R.id.elv);
        searchView = activity.findViewById(R.id.search_user);
        tvUserAdd = activity.findViewById(R.id.tv_user_add_message);
    }

    List<UserInfo> getFriends() {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                if (0 == i) {
                    childs = list;
                } else {
                    //获取好友列表失败
                    childs = null;
                    Log.e(CommonData.ETAG, "获取好友列表失败");
                }
            }
        });
        return childs;
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
                        expandableListView.setAdapter(adapter);

                        Log.e(CommonData.ETAG, "列表加载");
                    }
                } else {
                    //获取好友列表失败
                    childs = null;
                    Log.e(CommonData.ETAG, "获取好友列表失败");
                }
            }
        });
        //添加好友
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String name = s.trim();
                Log.i(CommonData.ITAG, "查询的字段为" + name);
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
                                Log.e(CommonData.ETAG, "gotResult:  添加失败可能无此人\n" + s);
                                Toast.makeText(activity.getApplicationContext(), "无此人", Toast.LENGTH_LONG).show();
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
