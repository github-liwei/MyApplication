package com.liwei.clock.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import com.liwei.clock.R;
import com.liwei.clock.interfaceclass.DataC;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Button btLogin;
    private Button btReregister;
    private Button btUpdateUser;
    private Button btLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Layout = inflater.inflate(R.layout.activity_setting_fragment, container, false);
        return Layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        btLogin.setOnClickListener(this);
        btReregister.setOnClickListener(this);
        btUpdateUser.setOnClickListener(this);
        btLogout.setOnClickListener(this);
    }

    void initView() {
        Activity _a = getActivity();
        btLogin = _a.findViewById(R.id.bt_login_view);
        btReregister = _a.findViewById(R.id.bt_register_view);
        btUpdateUser = _a.findViewById(R.id.bt_update_user_view);
        btLogout = _a.findViewById(R.id.bt_logout);
    }


    @Override
    public void onClick(View v) {
        Context context = getActivity().getApplicationContext();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_login_view:
                UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo != null) {
                    Log.e(DataC.ETAG, "onCreate: 用户已经登录" + myInfo.getUserName());
                } else {
                    intent.setClass(context, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.bt_register_view:
                intent.setClass(context, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_update_user_view:
                //TODO  还没写完
//                intent.setClass(context,);
//                startActivity(intent);
                break;
            case R.id.bt_logout:
                JMessageClient.logout();
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);

                //TODO  销毁的是MainActivity和它上面所有Fragment
                getActivity().finish();
                break;
            default:
                Toast.makeText(context, "没找到按钮", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getSimpleName(), "onDestroy: 正在销毁");
    }
}
