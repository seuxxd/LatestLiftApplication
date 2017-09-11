package com.product.xxd.latestliftapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.ActivityResultCode;
import zxing.activity.CaptureActivity;

public class UIActivity extends AppCompatActivity {

//    调试用的标记
    private static final String TAG = "UIActivity";
//    上传时的设备编号
    private String mLiftInfo ;

//    扫描二维码的按键
    @BindView(R.id.scan_button)
    Button mButtonScanCode;
//    二维码扫描按钮的事件响应
    @OnClick(R.id.scan_button)
    public void scan_button(){
        Log.i(TAG, "scanCode: " + "pressed");
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, ActivityResultCode.SCAN_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
//        绑定ButterKnife
        ButterKnife.bind(this);
    }

//    有结果返回的Activity的处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
//            这个是扫描二维码的Activity处理部分
            case ActivityResultCode.SCAN_REQUEST_CODE:
                if (resultCode == ActivityResultCode.SCAN_RESULT_OK){
                    Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
                    mLiftInfo = data.getStringExtra("result");
                }
                else if (resultCode == ActivityResultCode.SCAN_RESULT_FAILED){
                    Toast.makeText(this, "扫码失败，请重试", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
