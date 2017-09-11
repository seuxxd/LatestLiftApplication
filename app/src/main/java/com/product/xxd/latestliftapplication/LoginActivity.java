package com.product.xxd.latestliftapplication;

import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
//    Toolbar里面的标题自定义的文本框
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
        mToolbar.setTitle("");
        mToolbarTitle.setText(mTitleString);
        /*mToolbar.inflateMenu(R.menu.login_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.app_usage:
                        Toast.makeText(LoginActivity.this, "用法", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
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
