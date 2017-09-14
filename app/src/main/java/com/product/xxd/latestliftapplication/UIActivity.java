package com.product.xxd.latestliftapplication;

import android.content.Intent;
import android.support.annotation.BinderThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import ble.BluetoothController;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.InternetConnectionUtil;
import internet.TaskIdService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import util.ActivityResultCode;
import zxing.activity.CaptureActivity;

public class UIActivity extends AppCompatActivity {

//    调试用的标记
    private static final String TAG = "UIActivity";
//    上传时的设备编号
    private String mLiftInfo ;
//    用于获取taskId的令牌
    private String mToken;
//    蓝牙控制器
    private BluetoothController mController = BluetoothController.getInstance();

//    扫描二维码的按键
    @BindView(R.id.scan_button)
    Button mButtonScanCode;
//    二维码扫描按钮的事件响应
    @OnClick(R.id.scan_button)
    public void scan(){
        Log.i(TAG, "scanCode: " + "pressed");
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, ActivityResultCode.SCAN_REQUEST_CODE);
    }
//    任务单显示组件
    @BindView(R.id.task_id)
    TextView mTaskIDText;
//    获取任务单的事件响应
    @OnClick(R.id.task_id)
    public void getTaskID(){
        Retrofit mRetrofit = InternetConnectionUtil.getRetrofit();
        TaskIdService mService = mRetrofit.create(TaskIdService.class);
        Call<ResponseBody> mCall = mService.getTaskID(mToken);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    JSONObject object = new JSONObject(str);
                    if (!object.getString("message").equals("查询成功")){
                        Toast.makeText(UIActivity.this, "错误，请重试", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray array = new JSONArray(object.getString("data"));
                        List<String> list = new ArrayList<>();
                        for (int i = 0 ; i < array.length() ; i ++){
                            JSONObject mTaskIDData = array.getJSONObject(i);
                            String mTaskID = mTaskIDData.getString("taskID");
                            list.add(mTaskID);
                        }
                        showTaskIDDialog(list);
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
//    展示任务单的对话框
    private void showTaskIDDialog(final List<String> list){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_taskid,null);
        final AlertDialog mdialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        mdialog.show();
        ListView mTaskIDListView = (ListView) view.findViewById(R.id.taskid_list);
        mTaskIDListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list));
        mTaskIDListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTaskIDText.setText(list.get(position));
                mdialog.dismiss();
            }
        });
    }

//    打开BLE的按键
    @BindView(R.id.ble_button)
    Button mBLEButton;
    @OnClick(R.id.ble_button)
    public void connectBLE(){
        Intent intent = new Intent(this,BLEActivity.class);
        startActivity(intent);
    }
//    绑定初始化12个按键
    @BindView(R.id.accelarate)
    Button mAccelarateButton;
    @BindView(R.id.palstance)
    Button mPalstanseButton;
    @BindView(R.id.angle)
    Button mAngle;
    @BindView(R.id.voice)
    Button mVoice;
    @BindView(R.id.distance)
    Button mDistanceButton;
    @BindView(R.id.params_all)
    Button mAllParamsButton;
    @BindView(R.id.runnint_status)
    Button mRunningStatusButton;
    @BindView(R.id.test_connection)
    Button mTestConnectionButton;
    @BindView(R.id.error_info)
    Button mErrorInfoButton;
    @BindView(R.id.reset_data)
    Button mResetDataButton;
    @BindView(R.id.upload)
    Button mUploadButton;
    @BindView(R.id.history)
    Button mHistoryButton;
//    发送指令到蓝牙模块
    private void sendCommand(String command){
        mController.write(command);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
//        绑定ButterKnife
        ButterKnife.bind(this);
        mToken = getIntent().getStringExtra("token");
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
