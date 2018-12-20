package com.example.perry.yoursidesystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.perry.yoursidesystem.database.User;
import com.example.perry.yoursidesystem.test.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by perry on 2017/12/19.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USER_NAME_ARG = "username";
    public static final String PASSWORD_ARG = "password";
    public static final String IS_REMEMBER_TAG = "is_remember";
    public static final int REGISTER_FLAG = 120;
    private Button cancelBtn, loginBtn, registerBtn;
    private EditText userNameET, passwordET;
    private CheckBox rememberPwdCB, aotoLoginCB;
    public static Activity loginActivity;
    private TextView titleTV;
    private boolean isRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        cancelBtn = (Button) findViewById(R.id.cancel_login);
        loginBtn = (Button) findViewById(R.id.login2);
        registerBtn = (Button) findViewById(R.id.register);
        userNameET = (EditText) findViewById(R.id.user_name);
        passwordET = (EditText) findViewById(R.id.password);
        titleTV = (TextView) findViewById(R.id.heath_title);
        rememberPwdCB = (CheckBox) findViewById(R.id.remeber_password);
        cancelBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.getFlags() == REGISTER_FLAG) {
            isRegister = true;
            loginBtn.setText(R.string.register);
            titleTV.setText(R.string.register);
            registerBtn.setVisibility(View.GONE);
            rememberPwdCB.setVisibility(View.GONE);
        } else {
            loadPassword();
        }

    }

    private void loadPassword() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean isRemember = preferences.getBoolean(IS_REMEMBER_TAG, false);
        userNameET.setText(preferences.getString(USER_NAME_ARG, ""));
        if (isRemember) {
            rememberPwdCB.setChecked(true);
            passwordET.setText(preferences.getString(PASSWORD_ARG, ""));
        }

    }

    private void savePassword() {
        String userName = userNameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        SharedPreferences.Editor editor = getSharedPreferences("user",
                MODE_PRIVATE).edit();
        editor.putString(USER_NAME_ARG, userName);
        editor.putBoolean(IS_REMEMBER_TAG, rememberPwdCB.isChecked());
        if (rememberPwdCB.isChecked()) {
            editor.putString(PASSWORD_ARG, password);
        }
        editor.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_login:
                finish();
                break;
            case R.id.login2:
                String userName = userNameET.getText().toString();
                String password = passwordET.getText().toString();
                if ("".equals(userName)) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast
                            .LENGTH_SHORT).show();
                    break;
                }
                if ("".equals(password)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast
                            .LENGTH_SHORT).show();
                    break;
                }
                /**
                 * 因为登录和注册都是用这个活动，所以需分情况判断
                 * 如果是注册，则判断是否存在该用户，无该用户才能注册
                 * 如果是登录，也判断是否存在该用户，有该用户才能登录
                 */
                if (isRegister) {
                    //检测是否重复，否加入用户表
                    if (checkUserExist(userName)) {
                        Toast.makeText(LoginActivity.this, "已存在该用户", Toast
                                .LENGTH_SHORT).show();
                        break;
                    }
                    User user = new User(userName, password);
                    user.save();
                    finish();
                    break;
                } else {
                    if (checkUserExist(userName)) {
                        if (checkPwd(userName, password)) {
                            savePassword();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra(USER_NAME_ARG, userName);
                            intent.putExtra(PASSWORD_ARG, password);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast
                                    .LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "无该用户，请先注册", Toast
                                .LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.register:
                Intent intent1 = new Intent(this, LoginActivity.class);
                intent1.addFlags(REGISTER_FLAG);
                startActivity(intent1);
                break;
        }
    }

    private boolean checkUserExist(String name) {
        List<User> users = DataSupport.select("name").find(User.class);
        for (User user : users) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPwd(String name, String pwd) {
        List<User> userList = DataSupport.select("pwd").where("name=?", name).find(User
                .class);
        for (User user : userList) {
            LogUtil.i("tag", user.getPwd());
        }
        LogUtil.i("tag", pwd);
        return userList.get(0).getPwd().equals(pwd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePassword();
    }


}













































