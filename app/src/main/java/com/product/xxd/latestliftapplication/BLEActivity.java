package com.product.xxd.latestliftapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

import ble.BLEService;
import ble.BluetoothController;
import ble.ConstantUtils;
import ble.DeviceAdapter;
import ble.EntityDevice;

public class BLEActivity extends AppCompatActivity
{
    public static BLEActivity sBLEActivity;

    private Button mSearch;
    private Button mStop;
    private ListView mListView;
    private ArrayList<EntityDevice> list = new ArrayList<>();
    private DeviceAdapter adapter;
    private Intent intentService;
    private MsgReceiver receiver;


    private static final String TAG = "BLEActivity";
    public static String readString;


    BluetoothController controller = BluetoothController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sBLEActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        initView();
        initService();
        initData();
        addListener();
        registerReceiver();

    }

    private void addListener()
    {
        mListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {

//            点击之后传递连接成功，返回到主界面
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BluetoothController.getInstance().connect(list.get(position));
                Intent intent = new Intent(BLEActivity.this, UIActivity.class);
                intent.putExtra("isConnect", true);
                setResult(3, intent);
                BLEActivity.this.finish();
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new GetDataTask().execute();// 搜索任务
                Log.i(TAG, "onClick: " + "search");
            }
        });
        mStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BluetoothController.getInstance().stopScanBLE();
            }
        });
    }

    private void initData() {
        adapter = new DeviceAdapter(this, list);
        mListView.setAdapter(adapter);
    }

    /**
     * 开始服务, 初始化蓝牙
     */
    private void initService() {
        //开始服务
        intentService = new Intent(BLEActivity.this, BLEService.class);
        startService(intentService);
        // 初始化蓝牙
        BluetoothController.getInstance().initBLE();
    }

    /**
     * findViewById
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.list_devices);
        mSearch = (Button) findViewById(R.id.btn_search);
        mStop = (Button) findViewById(R.id.btn_stop);
    }

    private void registerReceiver() {
        receiver=new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        registerReceiver(receiver, intentFilter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]>
    {

        @Override
        protected String[] doInBackground(Void... params)
        {
            if(BluetoothController.getInstance().isBleOpen())
            {
                BluetoothController.getInstance().startScanBLE();
            }// 开始扫描
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

    /**
     * 广播接收器
     *
     */
    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    ConstantUtils.ACTION_UPDATE_DEVICE_LIST)) {
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");
                boolean found=false;//记录该条记录是否已在list中，
                for(EntityDevice device:list){
                    if(device.getAddress().equals(address)){
                        found=true;
                        break;
                    }
                }// for
                if(!found){
                    EntityDevice temp = new EntityDevice();
                    temp.setName(name);
                    temp.setAddress(address);
                    list.add(temp);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "onReceive: " + name);
                }
            }
            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE)){
            }

            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_CONNECT)){
                toast("连接已断开");
            }

            else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE)){
//                readString = intent.getStringExtra("message");
                Log.i(TAG, "onReceive: " + intent.getStringExtra("message"));
            }
        }
    }


    private void toast(String str) {
        Toast.makeText(BLEActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentService);
        unregisterReceiver(receiver);
    }
}
