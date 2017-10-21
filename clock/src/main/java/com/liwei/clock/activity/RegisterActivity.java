package com.liwei.clock.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import com.liwei.clock.R;
import com.liwei.clock.interfaceclass.CommonData;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    void initView() {
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.bt_register);
        etUsername = (EditText) findViewById(R.id.et_register_username);
        etPassword = (EditText) findViewById(R.id.et_register_password);
    }

    void initData() {

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                JMessageClient.register(userName, password, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            Log.i(CommonData.ITAG, "JMessageClient.register " + ", responseCode = " + i + " ; registerDesc = " + s);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                            Log.e(CommonData.ETAG, "JMessageClient.register " + ", responseCode = " + i + " ; registerDesc = " + s);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getSimpleName(), "onDestroy: 正在销毁");
    }
}
