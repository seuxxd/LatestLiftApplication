package com.product.xxd.latestliftapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.ErrorAdapter;
import adapter.StatusAdapter;
import adapter.UploadAdapter;
import ble.BLEService;
import ble.BluetoothController;
import ble.ConvertUtils;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dialog.TaskIDDialog;
import eventbusobject.BLEConnInfo;
import eventbusobject.DialogChange;
import internet.InternetConnectionUtil;
import jsonmodel.Data;
import jsonmodel.ErrorModel;
import jsonmodel.LiftParams;
import internet.TaskIdService;
import internet.UploadService;
import jsonmodel.RunningInfoModel;
import jsonmodel.UploadData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import util.ConstantCode;
import util.ErrorInfo;
import util.RunningStatus;
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
    public boolean mIsChangedToString;
//    判断当前按键类别
    private static int mWhichButton = 0;
//    运行信息的value
    private List<Integer> mRunningInfoNumberValue;
//    运行信息的value字符串表示
    private List<String> mRunningInfoStringValue;
//    故障信息的value
    private List<Integer> mErrorInfoNumberValue;
//    故障信息的value字符串表示
    private List<String> mErrorInfoStringValue;
//    上传的json字符串
    private Gson mParamsGson = new Gson();
    private Data mData = new Data();
//    上传时是否数据齐全的判据
    private int mTaskID = 0;
    private int mScanID = 0;
    private int mAllDataNumber = 0;
    private int mRunningInfoNumber = 0;
    private int mErrorStatusNumber = 0;

    private static boolean mIsAllDataButton = false;
    private LiftParams mLiftParams = new LiftParams();
//    打开蓝牙连接的字符串
    @BindString(R.string.ble)
    String mConnect;
//    断开蓝牙的字符串
    @BindString(R.string.disconnect)
    String mDisconnect;
//    设备ID
    @BindString(R.string.lift_id)
    String mLiftId;
//    扫描二维码的按键
    @BindView(R.id.scan_button)
    Button mButtonScanCode;
//    二维码扫描按钮的事件响应
    @OnClick(R.id.scan_button)
    public void scan(){
        Log.i(TAG, "scanCode: " + "pressed");
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, ConstantCode.SCAN_REQUEST_CODE);
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
                    Log.i(TAG, "onResponse: " + str);
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
        final TaskIDDialog dialog = new TaskIDDialog(this);
        dialog.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list));
//        设置对话框大小
        dialog.getWindow().setLayout(900,450);
//        设置背景为透明，这样就会出现圆角对话框
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
        dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTaskIDText.setText(list.get(position));
                mTaskID = 1;
                checkUploadPossibility();
                dialog.dismiss();
            }
        });
        /*View view = LayoutInflater.from(this).inflate(R.layout.dialog_taskid,null);
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
        });*/
    }

//    打开BLE的按键
    @BindView(R.id.ble_button)
    Button mBLEButton;
    @OnClick(R.id.ble_button)
    public void connectBLE(){
        if (!mIsConnected){
            Intent intent = new Intent(this,BLEActivity.class);
            startActivity(intent);
        }
        else {
            mIsConnected = false;
            BluetoothController.disconnect();
            mBLEButton.setText(mConnect);
        }

    }
//    更智能的判断蓝牙连接状况
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnect(BLEConnInfo info){
        if (info.mIsConnected)
            mIsConnected = true;
        mBLEButton.setText(mDisconnect);
    }

//    数据展示界面
    @BindView(R.id.lift_id)
    TextView mShowId;
    @BindView(R.id.show_params)
    TextView mShowArea;
//    绑定初始化12个按键
//    测量加速度
    @BindView(R.id.accelarate)
    Button mAccelarateButton;
    @OnClick(R.id.accelarate)
    public void setAccelarateButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.ACCELERATE_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SaccE");
    }
//    测量角速度
    @BindView(R.id.palstance)
    Button mPalstanseButton;
    @OnClick(R.id.palstance)
    public void setPalstanseButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.PALSTANCE_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SpalE");
    }
//    测量角度
    @BindView(R.id.angle)
    Button mAngleButton;
    @OnClick(R.id.angle)
    public void setAngleButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.ANGLE_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SangE");
    }

    //    测量距离
    @BindView(R.id.distance)
    Button mDistanceButton;
    @OnClick(R.id.distance)
    public void setDistanceButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.DISTANCE_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SdisE");
    }

//    测量声音
    @BindView(R.id.voice)
    Button mVoiceButton;
    @OnClick(R.id.voice)
    public void setVoiceButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.VOICE_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SvoiE");
    }

//    获取所有数据
    @BindView(R.id.params_all)
    Button mAllParamsButton;
    @OnClick(R.id.params_all)
    public void setAllParamsButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.DATA_BUTTON_PRESSED;
        mIsAllDataButton = true;
        sendCommand("SDataE");
        setTime();
    }
    private void setTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String y = String.valueOf(year);
        String m = (month < 10) ? "0" + month : String.valueOf(month);
        String d = (day < 10) ? "0" + day : String.valueOf(day);
        String h = (hour < 10) ? "0" + hour : String.valueOf(hour);
        String mi = (minute < 10) ? "0" + minute : String.valueOf(minute);
        String s = (second < 10) ? "0" + second : String.valueOf(second);
        String str = "ST" + y + m + d + h + mi + s + "E";
        sendCommand(str);
    }
//    获取运行状态
    @BindView(R.id.running_status)
    Button mRunningStatusButton;
    @OnClick(R.id.running_status)
    public void setRunningStatusButton(){
        mIsChangedToString = false;
        mWhichButton = ConstantCode.RUNNING_STATUS_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SstatusE");
    }
//    测试连接状态
    @BindView(R.id.test_connection)
    Button mTestConnectionButton;
    @OnClick(R.id.test_connection)
    public void setTestConnectionButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.TEST_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("StestE");
    }
//    获取故障信息
    @BindView(R.id.error_info)
    Button mErrorInfoButton;
    @OnClick(R.id.error_info)
    public void setErrorInfoButton(){
        mIsChangedToString = false;
        mWhichButton = ConstantCode.RUNNING_ERROR_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SalarmE");
    }
//    重置数据
    @BindView(R.id.reset_data)
    Button mResetDataButton;
    @OnClick(R.id.reset_data)
    public void setResetDataButton(){
        mIsChangedToString = true;
        mWhichButton = ConstantCode.RESET_BUTTON_PRESSED;
        mIsAllDataButton = false;
        sendCommand("SclearE");
    }
//    上传数据
    @BindView(R.id.upload)
    Button mUploadButton;
    @OnClick(R.id.upload)
    public void setUploadButton(){
        mWhichButton = ConstantCode.UPLOAD_BUTTON_PRESSED;
        String error;
        String running_info;

//        电梯加速度等参数
        String str = mParamsGson.toJson(mLiftParams);
        mIsAllDataButton = false;
        Log.i(TAG, "电梯参数: " + str);
//        电梯故障信息
        ErrorModel errorModel = new ErrorModel();
        for (int i = 0 ; i < mErrorInfoNumberValue.size() ; i ++){
            errorModel
                    .getErrorMaps()
                    .put(ErrorInfo.getmErrorInfo().get(i), mErrorInfoNumberValue.get(i));
        }
        //        电梯运行信息
        RunningInfoModel runningInfoModel = new RunningInfoModel();
        for (int i = 0 ; i < mRunningInfoNumberValue.size() ; i ++){
            runningInfoModel
                    .getRunningInfoMaps()
                    .put(RunningStatus.getStatusList().get(i),mRunningInfoNumberValue.get(i));
        }
        String temp = mParamsGson.toJson(errorModel);
        String temp2 = mParamsGson.toJson(runningInfoModel);

        try {
            JSONObject object = new JSONObject(temp);
            error = object.getString("mErrorMaps");
            object = new JSONObject(temp2);
            running_info = object.getString("mRunningInfoMaps");

        } catch (JSONException e) {
            e.printStackTrace();
            error = "no data";
            running_info = "no data";
        }

        error = error.substring(1,error.length() - 1);
        running_info = running_info.substring(1,running_info.length() - 1);
        String test = mParamsGson.toJson(mData);
        test = test.substring(1,test.length() - 1);
        String uploadString =
                "{" + error +"," + running_info + "," + test + "," + "id" + ":" + mLiftInfo + "}";
        Log.i(TAG, "upload: " + uploadString);
        Data data = mParamsGson.fromJson(uploadString,Data.class);
        UploadData upload = new UploadData();
        upload.setTaskListID(mTaskIDText.getText().toString());
        upload.setTempData(data);
        Log.i(TAG, "setUploadButton: " + mParamsGson.toJson(upload,UploadData.class));

        getUploadShowString(mToken,upload);

    }
//    上传数据时展示的内容
    private void getUploadShowString(final String token , final UploadData uploadData){
        List<String> mKeyList = new ArrayList<>();
        List<String> mValueList = new ArrayList<>();
        mKeyList.add("id");
        mValueList.add(mLiftInfo);
        mKeyList.add("parameters");
        mValueList.add(mLiftParams.toString());
        for (int i = 0 ; i < ErrorAdapter.getmErrorInfoStringValue().size() ; i ++){
            mKeyList.add(ErrorInfo.getmErrorChineseInfo().get(i));
            mValueList.add(ErrorAdapter.getmErrorInfoStringValue().get(i));
        }
        for (int i = 0 ; i < StatusAdapter.getmStatusListStringValue().size() ; i ++){
            mKeyList.add(RunningStatus.getStatusChineseList().get(i));
            mValueList.add(StatusAdapter.getmStatusListStringValue().get(i));
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_upload,null);
        final AlertDialog dialog =
                new AlertDialog
                        .Builder(this)
                        .setView(view)
                        .setCancelable(true)
                        .create();
        ListView listView = (ListView) view.findViewById(R.id.upload_content);
        Button mButtonEnsure = (Button) view.findViewById(R.id.upload_ensure);
        Button mButtonCancel = (Button) view.findViewById(R.id.upload_cancel);

        mButtonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit mRetrofit = InternetConnectionUtil.getRetrofit();

                UploadService service = mRetrofit.create(UploadService.class);
                Call<ResponseBody> call = service.upload(token,uploadData);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();
//                            Log.i(TAG, "onResponse: " + res);
                            JSONObject object = new JSONObject(res);
                            String result = object.getString("message");
                            if (result.equals("添加成功")){
                                Toast.makeText(UIActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(UIActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                dialog.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listView.setAdapter(new UploadAdapter(this,mValueList,mKeyList));
        dialog.show();

    }
//    获取历史信息
    @BindView(R.id.history)
    Button mHistoryButton;
    @OnClick(R.id.history)
    public void setHistoryButton(){
        mIsAllDataButton = true;
        mWhichButton = ConstantCode.HISTORY_BUTTON_PRESED;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_history,null);
        //    历史数据获得调用的对话框
        final AlertDialog mHistoryDialog =
                new AlertDialog.Builder(this)
                        .setView(view)
                        .setCancelable(true)
                        .create();
        mHistoryDialog.show();
        Button mNumberButton = (Button) view.findViewById(R.id.submit_history_number);
        final EditText mNumberText = (EditText) view.findViewById(R.id.history_number);
        mNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "SN" + mNumberText.getText().toString() + "E";
                sendCommand(str);
                mHistoryDialog.dismiss();
            }
        });
    }

//    发送指令到蓝牙模块
    private void sendCommand(String command){
        mController.write(command);
        Log.i(TAG, "sendCommand: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
//        绑定ButterKnife
        ButterKnife.bind(this);
        mShowArea.setMovementMethod(ScrollingMovementMethod.getInstance());
        mToken = getIntent().getStringExtra("token");
        EventBus.getDefault().register(this);
        checkUploadPossibility();

    }
    private void checkUploadPossibility(){
        if (mTaskID + mScanID + mAllDataNumber + mErrorStatusNumber + mRunningInfoNumber != 5){
            mUploadButton.setEnabled(false);
        }
        else {
            mUploadButton.setEnabled(true);
        }
    }

//    有结果返回的Activity的处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
//            这个是扫描二维码的Activity处理部分
            case ConstantCode.SCAN_REQUEST_CODE:
                if (resultCode == ConstantCode.SCAN_RESULT_OK){
                    Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
                    mLiftInfo = data.getStringExtra("result");
                    String str = mLiftId + " : " +mLiftInfo;
                    mScanID = 1;
                    checkUploadPossibility();
                    mShowId.setText(str);
                }
                else if (resultCode == ConstantCode.SCAN_RESULT_FAILED){
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
//        这个因为是在别的控制类里面发出的字符串，所以不需要进行判断和转换
        if (s.equals("connect")){
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new BLEConnInfo(true));
        }
//        这里进行蓝牙传回的字符串判断和解析
//        对于故障和运行，使用字节
//        其他进行转换，变成字符串
        else {
            Log.i(TAG, "onEvent: " + mWhichButton);
            switch (mWhichButton){
                case ConstantCode.ACCELERATE_BUTTON_PRESSED:
                    String mAccelerate = ConvertUtils.dexToString(s);
                    String[] temp = mAccelerate.split("m/s2");
                    mShowArea.setText(
                            "测得的加速度为：" + "\n" +
                                    temp[0] + "\n" +
                                    temp[1] + "\n" +
                                    temp[2]);
                    mLiftParams.setAcc(temp);
                    break;
                case ConstantCode.PALSTANCE_BUTTON_PRESSED:
                    String mPalstance = ConvertUtils.dexToString(s);
                    Log.i(TAG, "onEvent: " + mPalstance);
                    String[] temp2 = mPalstance.split("d/s");
                    mShowArea.setText(
                            "测得的角速度为：" + "\n" +
                                    temp2[0] + "\n" +
                                    temp2[1] + "\n" +
                                    temp2[2]);
                    mLiftParams.setPal(temp2);
                    break;
                case ConstantCode.ANGLE_BUTTON_PRESSED:
                    String mAngle = ConvertUtils.dexToString(s);
                    Log.i(TAG, "onEvent: " + mAngle);
                    String[] temp3 = mAngle.split("d");
                    mShowArea.setText(
                            "测得的角度为：" + "\n" +
                                    temp3[0] + "\n" +
                                    temp3[1] + "\n" +
                                    temp3[2]);
                    mLiftParams.setAngle(temp3);
                    break;
                case ConstantCode.DISTANCE_BUTTON_PRESSED:
                    String mDistance = ConvertUtils.dexToString(s);
                    Log.i(TAG, "onEvent: " + mDistance);
                    String[] temp4 = mDistance.split("cm");
                    mShowArea.setText("测得的距离为："  + "\n" +
                                    temp4[0] + "\n" +
                                    temp4[1] + "\n" +
                                    temp4[2]);
                    mLiftParams.setDis(temp4);
                    break;
                case ConstantCode.VOICE_BUTTON_PRESSED:
                    String mVoice = ConvertUtils.dexToString(s);
                    Log.i(TAG, "onEvent: " + mVoice);
                    mShowArea.setText("测得的噪声大小为：" + "\n" + mVoice);
                    mLiftParams.setVoice(Double.valueOf(mVoice.split("dB")[0]));
                    break;
                case ConstantCode.DATA_BUTTON_PRESSED:
                    mAllDataNumber = 1;
                    checkUploadPossibility();
                    mShowArea.setText(parseDataFromBLE(s));
                    break;
                case ConstantCode.RUNNING_STATUS_BUTTON_PRESSED:
                    mShowArea.setText("");
                    statusInfoConvert(s);
                    break;
                case ConstantCode.TEST_BUTTON_PRESSED:
                    mShowArea.setText(ConvertUtils.dexToString(s));
                    break;
                case ConstantCode.RUNNING_ERROR_BUTTON_PRESSED:
                    mShowArea.setText("");
                    errorInfoConvert(s);
                    break;
                case ConstantCode.RESET_BUTTON_PRESSED:
                    Toast.makeText(this, "采集器存储数据已清空", Toast.LENGTH_SHORT).show();
                    break;
                case ConstantCode.UPLOAD_BUTTON_PRESSED:
                    break;
                case ConstantCode.HISTORY_BUTTON_PRESED:
                    mShowArea.setText(parseHistory(s));
                    break;
                default:
                    break;
            }
        }
    }

//    解析蓝牙返回的全部数据
    private String parseDataFromBLE(String s){
        String mShowString = "";
        if (s.equals("00")){
            mShowString = "请通过手持终端更新数据";
        }
        else {
            String temp = ConvertUtils.dexToString(s);
            Log.i(TAG, "parseDataFromBLE: " + temp);
            mLiftParams = mParamsGson.fromJson(temp,LiftParams.class);
            mData.setRunning_params(mLiftParams);
            mShowString = mLiftParams.toString();
        }
        return mShowString;
    }
//    获取历史记录之后的解析工作
    private String parseHistory(String s){
        String mShowString = "";
        if (s.equals("00")){
            mShowString = "请通过手持终端更新数据";
        }
        else {
            String temp = ConvertUtils.dexToString(s);
            String[] mHistoryString = temp.split("\\}");
            for (int i = 0 ; i < mHistoryString.length ; i ++){
                mHistoryString[i] = mHistoryString[i] + "}";
                mLiftParams = mParamsGson.fromJson(mHistoryString[i],LiftParams.class);
                mShowString += mLiftParams.toString();
                mShowString += "\n";
            }
        }
        return mShowString;

    }
    private AlertDialog mRunningDialog;
//    点击获取运行状态时的对话框处理
    private void showRunningStatusDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_running,null);
        mRunningDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
//        ListView用ButterKnife会出错……不得不用find
        ListView mRunningInfoListView = (ListView) view.findViewById(R.id.running_info_listview);
        Button mRunningInfoSureButton = (Button) view.findViewById(R.id.running_info_sure);
        mRunningInfoSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DialogChange(ConstantCode.RUNNING_INFO_SURE_BUTTON_PRESSED));
            }
        });
        Button mRunningInfoCancelButton = (Button) view.findViewById(R.id.running_info_cancel);
        mRunningInfoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DialogChange(ConstantCode.RUNNING_INFO_CANCEL_BUTTON_PRESSED));
            }
        });
        mRunningInfoListView.setAdapter(new StatusAdapter(this,mRunningInfoNumberValue));
        mRunningDialog.show();
    }

    //    运行数据处理
    private void statusInfoConvert(String s){
        mRunningInfoNumberValue = RunningStatus.getmRunningInfoNumberValue();
        try {
            for (int i = 29; i < 49; i += 2){
                int result = ((int) s.charAt(i)) - 48;
                mRunningInfoNumberValue.set((i - 29) / 2 , result);
                Log.i(TAG, "statusInfoConvert: " + result);
            }
            showRunningStatusDialog();
        } catch (Exception e) {
            Toast.makeText(this, "请重启运行监控装置", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private AlertDialog mErrorDialog;
//    点击获取故障信息的对话框处理
    private void showErrorDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_error,null);
        mErrorDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
//        使用butterknife会报错，找不到响应组件
        ListView mErrorListView = (ListView) view.findViewById(R.id.error_info_listview);
        Button mErrorInfoCancelButton = (Button) view.findViewById(R.id.error_info_cancel);
        mErrorInfoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DialogChange(ConstantCode.ERROR_INFO_CANCEL_BUTTON_PRESSED));
            }
        });
        Button mErrorInfoSureButton = (Button) view.findViewById(R.id.error_info_sure);
        mErrorInfoSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DialogChange(ConstantCode.ERROR_INFO_SURE_BUTTON_PRESSED));
            }
        });
        mErrorListView.setAdapter(new ErrorAdapter(this,mErrorInfoNumberValue));
        mErrorDialog.show();
    }

//    运行故障信息处理
    private void errorInfoConvert(String s){
        Log.i(TAG, "errorInfoConvert: " + s);
        mErrorInfoNumberValue = ErrorInfo.getmErrorNumberValue();
        try {
            for (int i = 21 ; i < 41 ; i += 2){
                int temp = ((int) s.charAt(i)) - 48;
                mErrorInfoNumberValue.set((i - 21) / 2,temp);
                Log.i(TAG, "errorInfoConvert: " + temp);
            }
            showErrorDialog();
        } catch (Exception e) {
            Toast.makeText(this, "请重启运行监控装置", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    点击确定按钮后，更改被保存
//    点击取消按钮后，更改取消
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDialogChange(DialogChange change){
        switch (change.mAction){
            case ConstantCode.ERROR_INFO_SURE_BUTTON_PRESSED:
                Toast.makeText(this, "更改成功", Toast.LENGTH_SHORT).show();
                mErrorStatusNumber = 1;
                checkUploadPossibility();
                mErrorInfoStringValue = ErrorAdapter.getmErrorInfoStringValue();
                mErrorInfoNumberValue = ErrorAdapter.getmErrorInfoNumberValue();
                mErrorDialog.dismiss();
                break;
            case ConstantCode.ERROR_INFO_CANCEL_BUTTON_PRESSED:
                Toast.makeText(this, "取消更改", Toast.LENGTH_SHORT).show();
                mErrorStatusNumber = 1;
                checkUploadPossibility();
                mErrorInfoStringValue = ErrorAdapter.getmErrorInfoStringValueOrigin();
                mErrorInfoNumberValue = ErrorAdapter.getmErrorInfoNumberValueOrigin();
                mErrorDialog.dismiss();
                break;
            case ConstantCode.RUNNING_INFO_SURE_BUTTON_PRESSED:
                Toast.makeText(this, "更改成功", Toast.LENGTH_SHORT).show();
                mRunningInfoNumber = 1;
                checkUploadPossibility();
                mRunningInfoStringValue = StatusAdapter.getmStatusListStringValue();
                mRunningInfoNumberValue = StatusAdapter.getmStatusListNumberValue();
                mRunningDialog.dismiss();
                break;
            case ConstantCode.RUNNING_INFO_CANCEL_BUTTON_PRESSED:
                Toast.makeText(this, "取消更改", Toast.LENGTH_SHORT).show();
                mRunningInfoNumber = 1;
                checkUploadPossibility();
                mRunningInfoStringValue = StatusAdapter.getmStatusListStringValueOrigin();
                mRunningInfoNumberValue = StatusAdapter.getmStatusListNumberValueOrigin();
                mRunningDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BLEService.mIsConnected){
            BluetoothController.disconnect();
            BLEService.mIsConnected = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BLEService.mIsConnected){
            mBLEButton.setText(mConnect);
            mIsConnected = false;
        }
    }

    public static boolean ismIsAllDataButton() {
        return mIsAllDataButton;
    }
    public static int isWhichButtonPressed(){
        return mWhichButton;
    }
}
