package com.product.xxd.latestliftapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;


import ble.BLEService;
import ble.BluetoothController;
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
//    Toolbar的图标
    @BindView(R.id.toolbar_icon)
    ImageView mToolbarIcon;
//    CheckBox的绑定，判断是否需要保存用户名和密码
    @BindView(R.id.check_store)
    CheckBox mStoreUserPass;
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
//    app_usage
    @BindString(R.string.app_usage)
    String mAppUsage;


    String username;
    String password;
//    按键的事件响应
    @OnClick(R.id.login_button)
    public void login(){
        Retrofit mRetrofit = InternetConnectionUtil.getRetrofit();
        LoginService loginService = mRetrofit.create(LoginService.class);
        Log.i(TAG, "login:");
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
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
                        String str = response.body().string();
                        Log.i(TAG, "onResponse: " + str);
                        if (str != null){
                            testResponse(str);
                        }
                    } catch (Exception e) {
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
        mToolbarIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon));
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
                Toast.makeText(this, mAppUsage, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    检测服务器返回数据
    private void testResponse(String response){
        try{
            JSONObject mResponse = new JSONObject(response);
            String mMessage      = mResponse.getString("message");
            if (mMessage.equals("登陆成功!")){
                String mData         = mResponse.getString("data");
                JSONObject mDataJson = new JSONObject(mData);
                String mToken        = mDataJson.getString("token");
                Intent intent = new Intent(LoginActivity.this,UIActivity.class);
                intent.putExtra("token",mToken);
                startActivity(intent);
                storeUserInfo(username,password,mStoreUserPass.isChecked());
            }
            else {
                Log.i(TAG, "testResponse: " + mMessage);
                switch (mMessage){
                    case "密码错误!":
                        mUsernameTextInput.setError("");
                        mPasswordTextInput.setError("password error");
                        break;
                    case "查询不到此用户！":
                        mUsernameTextInput.setError("username not found");
                        mPasswordTextInput.setError("");
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    保存用户名和密码
    private void storeUserInfo(String username , String password , boolean isStore){
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putBoolean("store",isStore);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        boolean store   = sharedPreferences.getBoolean("store",false);
        if (store){
            mUsername.setText(username);
            mPassword.setText(password);
            mStoreUserPass.setChecked(true);
        }
        else{
            mUsername.setText("");
            mPassword.setText("");
            mStoreUserPass.setChecked(false);
        }
    }
}
