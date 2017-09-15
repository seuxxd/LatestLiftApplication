package com.product.xxd.latestliftapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.ErrorAdapter;
import adapter.StatusAdapter;
import ble.BluetoothController;
import ble.ConvertUtils;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import internet.InternetConnectionUtil;
import internet.TaskIdService;
import internet.UploadService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import util.ActivityConstantCode;
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
//    判断连接/断开蓝牙
    private boolean mIsConnected = false;
//    判断蓝牙数据解析方式，true是转换成字符串，false是保持字节流行时
    public static boolean mIsChangedToString = true;
//    判断当前按键类别
    private int mWhichButton = 0;
//    打开蓝牙连接的字符串
    @BindString(R.string.ble)
    String mConnect;
//    断开蓝牙的字符串
    @BindString(R.string.disconnect)
    String mDisconnect;
//    扫描二维码的按键
    @BindView(R.id.scan_button)
    Button mButtonScanCode;
//    二维码扫描按钮的事件响应
    @OnClick(R.id.scan_button)
    public void scan(){
        Log.i(TAG, "scanCode: " + "pressed");
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, ActivityConstantCode.SCAN_REQUEST_CODE);
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
        if (!mIsConnected){
            Intent intent = new Intent(this,BLEActivity.class);
            startActivity(intent);
            mIsConnected = true;
            mBLEButton.setText(mDisconnect);
        }
        else {
            mIsConnected = false;
            BluetoothController.disconnect();
            mBLEButton.setText(mConnect);
        }

    }

//    数据展示界面
    @BindView(R.id.show_params)
    TextView mShowArea;
//    绑定初始化12个按键
//    测量加速度
    @BindView(R.id.accelarate)
    Button mAccelarateButton;
    @OnClick(R.id.accelarate)
    public void setAccelarateButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.ACCELERATE_BUTTON_PRESSED;
        sendCommand("SaccE");
    }
//    测量角速度
    @BindView(R.id.palstance)
    Button mPalstanseButton;
    @OnClick(R.id.palstance)
    public void setPalstanseButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.PALSTANCE_BUTTON_PRESSED;
        sendCommand("SpalE");
    }
//    测量角度
    @BindView(R.id.angle)
    Button mAngleButton;
    @OnClick(R.id.angle)
    public void setAngleButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.ANGLE_BUTTON_PRESSED;
        sendCommand("SangE");
    }

    //    测量距离
    @BindView(R.id.distance)
    Button mDistanceButton;
    @OnClick(R.id.distance)
    public void setDistanceButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.DISTANCE_BUTTON_PRESSED;
        sendCommand("SdisE");
    }

//    测量声音
    @BindView(R.id.voice)
    Button mVoiceButton;
    @OnClick(R.id.voice)
    public void setVoiceButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.VOICE_BUTTON_PRESSED;
        sendCommand("SvoiE");
    }

//    获取所有数据
    @BindView(R.id.params_all)
    Button mAllParamsButton;
    @OnClick(R.id.params_all)
    public void setAllParamsButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.DATA_BUTTON_PRESSED;
        sendCommand("SDataE");
    }
//    获取运行状态
    @BindView(R.id.runnint_status)
    Button mRunningStatusButton;
    @OnClick(R.id.runnint_status)
    public void setRunningStatusButton(){
        mIsChangedToString = false;
        mWhichButton = ActivityConstantCode.RUNNING_STATUS_BUTTON_PRESSED;
        sendCommand("SstatusE");
        showRunningStatusDialog();
    }
//    测试连接状态
    @BindView(R.id.test_connection)
    Button mTestConnectionButton;
    @OnClick(R.id.test_connection)
    public void setTestConnectionButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.TEST_BUTTON_PRESSED;
        sendCommand("StestE");
    }
//    获取故障信息
    @BindView(R.id.error_info)
    Button mErrorInfoButton;
    @OnClick(R.id.error_info)
    public void setErrorInfoButton(){
        mIsChangedToString = false;
        mWhichButton = ActivityConstantCode.RUNNING_ERROR_BUTTON_PRESSED;
        sendCommand("SalarmE");
        showErrorDialog();
    }
//    重置数据
    @BindView(R.id.reset_data)
    Button mResetDataButton;
    @OnClick(R.id.reset_data)
    public void setResetDataButton(){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.RESET_BUTTON_PRESSED;
        sendCommand("SclearE");
    }
//    上传数据
    @BindView(R.id.upload)
    Button mUploadButton;
    @OnClick(R.id.upload)
    public void setUploadButton(){
        mWhichButton = ActivityConstantCode.UPLOAD_BUTTON_PRESSED;
        Retrofit mRetrofit = InternetConnectionUtil.getRetrofit();
        UploadService service = mRetrofit.create(UploadService.class);
        Call<ResponseBody> call = service.upload(mToken,null);
    }
//    获取历史信息
    @BindView(R.id.history)
    Button mHistoryButton;
//    发送指令到蓝牙模块
    private void sendCommand(String command){
        mIsChangedToString = true;
        mWhichButton = ActivityConstantCode.HISTORY_BUTTON_PRESED;
        mController.write(command);
        Log.i(TAG, "sendCommand: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
//        绑定ButterKnife
        ButterKnife.bind(this);
        mToken = getIntent().getStringExtra("token");
        EventBus.getDefault().register(this);
    }

//    有结果返回的Activity的处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
//            这个是扫描二维码的Activity处理部分
            case ActivityConstantCode.SCAN_REQUEST_CODE:
                if (resultCode == ActivityConstantCode.SCAN_RESULT_OK){
                    Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
                    mLiftInfo = data.getStringExtra("result");
                }
                else if (resultCode == ActivityConstantCode.SCAN_RESULT_FAILED){
                    Toast.makeText(this, "扫码失败，请重试", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

//    处理蓝牙返回的字符串
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s){
//        Log.i(TAG, "onEvent: " + "onEvent" + " " + s);
        if (s.equals("connect")){
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        }
        else {
            if (s.equals(ConvertUtils.dexToString("00")))
                mShowArea.setText("no data");
            else mShowArea.setText(s);
        }

    }
//    点击获取运行状态时的对话框处理
    private void showRunningStatusDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_running,null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
//        ListView用ButterKnife会出错……不得不用find
        ListView mRunningInfoListView = (ListView) view.findViewById(R.id.running_info_listview);
        mRunningInfoListView.setAdapter(new StatusAdapter(this));
        dialog.show();
    }
//    点击获取故障信息的对话框处理
    private void showErrorDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_error,null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        ListView mErrorListView = (ListView) view.findViewById(R.id.error_info_listview);
        mErrorListView.setAdapter(new ErrorAdapter(this));
        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
