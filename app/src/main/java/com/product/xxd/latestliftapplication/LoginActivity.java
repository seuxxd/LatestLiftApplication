package com.product.xxd.latestliftapplication;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.InternetConnectionUtil;
import internet.LoginService;
import internet.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
//    用户名的管理布局
    @BindView(R.id.textinputuser)
    TextInputLayout mUsernameTextInput;
//    用户名的编辑框
    @BindView(R.id.username)
    EditText mUsername;
//    密码的管理布局
    @BindView(R.id.textinputpass)
    TextInputLayout mPasswordTextInput;
//    密码的编辑框
    @BindView(R.id.password)
    EditText mPassword;
//    工具栏的引用
    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;
//    Toolbar里面的标题自定义的文本框
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
//    字符串的绑定
//    toolbar显示的字符串
    @BindString(R.string.login_title)
    String mTitleString;
//    当用户名为空时显示的字符串
    @BindString(R.string.empty_username)
    String mEmptyUsername;
//    当密码为空时显示的字符串
    @BindString(R.string.empty_password)
    String mEmptyPassword;


//    按键的事件响应
    @OnClick(R.id.login_button)
    public void login(){
        Retrofit mRetrofit = InternetConnectionUtil.getRetrofit();
        LoginService loginService = mRetrofit.create(LoginService.class);
        Log.i(TAG, "login:");
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (username.trim().equals("")){
            mUsernameTextInput.setError(mEmptyUsername);
        }
        else if (password.trim().equals("")){
            mPasswordTextInput.setError(mEmptyPassword);
        }
        else {
            Call<ResponseBody> call = loginService.getLoginToken(new User(username,password));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.i(TAG, "onResponse: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUsernameTextInput.setHint("Username");
        mPasswordTextInput.setHint("Password");
        mToolbar.setTitle("");
        mToolbarTitle.setText(mTitleString);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_usage:
                Toast.makeText(this, "APP_USAGE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
