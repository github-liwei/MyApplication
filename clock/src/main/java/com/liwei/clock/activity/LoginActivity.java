package com.liwei.clock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.interfaceclass.DataC;

public class LoginActivity extends Activity {

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        this.isRestricted();
    }

    void initView() {
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.bt_login);
        etUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        btnRister = findViewById(R.id.bt_register_view);
    }

    void initData() {
        //TODO 这是我加的验证是否已经登录
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo != null) {
            Log.e(DataC.ETAG, "onCreate: 用户已经登录" + myInfo.getUserName());
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    Log.i(DataC.ITAG, "开始网络登录操作, username:" + username + " password:" + password);
                    JMessageClient.login(username, password, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                Log.i(DataC.ITAG, "JMessageClient.login" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                                Log.i(DataC.ITAG, "JMessageClient.login" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            }
                        }
                    });
                }
            });

            btnRister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getApplication(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getSimpleName(), "onDestroy: 正在销毁");
    }
}