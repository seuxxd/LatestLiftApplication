package com.product.xxd.latestliftapplication;

import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

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
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
//    字符串的绑定
    @BindString(R.string.login_title)
    String mTitleString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUsernameTextInput.setHint("Username");
        mPasswordTextInput.setHint("Password");
//        mToolbar.setTitle(mTitle);
        mToolbarTitle.setText(mTitleString);
        setSupportActionBar(mToolbar);
    }

}
