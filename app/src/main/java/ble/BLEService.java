package ble;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class BLEService extends Service
{
	BluetoothController bleCtrl;
	String readString = "";
	public BLEBinder mBinder = new BLEBinder();
	private static final String TAG = "BLEService";
    public static boolean mIsConnected = false;

	@Override
	public IBinder onBind(Intent arg0)
	{
		bleCtrl = BluetoothController.getInstance();
		bleCtrl.setServiceHandler(handler);
		Log.i(TAG, "onBind: ");
		return mBinder;
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what) {
			case ConstantUtils.WM_BLE_CONNECTED_STATE_CHANGE:
				Bundle bundle = (Bundle) msg.obj;
				String address = bundle.getString("address");
				String name = bundle.getString("name");
				Bundle bundle1 = new Bundle();
				bundle1.putString("address", address);
				bundle1.putString("name", name);
				Intent intentDevice = new Intent(
						ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
				intentDevice.putExtras(bundle1);
				sendBroadcast(intentDevice);
				Log.i(TAG, "handleMessage: " + "connect");
                EventBus.getDefault().post("connect");
                mIsConnected = true;
                bleCtrl.stopScanBLE();
//				sendBroadcast(new Intent(ConstantUtils.ACTION_BLUETOOTH_CONNECTED));
				break;

			case ConstantUtils.WM_STOP_CONNECT:
				Intent stopConnect = new Intent(
						ConstantUtils.ACTION_STOP_CONNECT);
				sendBroadcast(stopConnect);
				break;

			case ConstantUtils.WM_STOP_SCAN_BLE:
				Log.i(TAG, "handleMessage: " + "stop scan");
				bleCtrl.stopScanBLE();
				break;
			case ConstantUtils.WM_UPDATE_BLE_LIST:
				Intent intent = new Intent(
						ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
				BluetoothDevice device = (BluetoothDevice) msg.obj;
				intent.putExtra("name", device.getName());
				intent.putExtra("address", device.getAddress());
				sendBroadcast(intent);
                Log.i(TAG, "handleMessage: " + "update list");
                break;

			case ConstantUtils.WM_RECEIVE_MSG_FROM_BLE:
				String mes = (String) msg.obj;
				Intent mesDevice = new Intent(
						ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
				mesDevice.putExtra("message", mes);
				sendBroadcast(mesDevice);
				readString = mes;
				Log.i(TAG, "handleMessage: 收到消息" + readString);
				break;

			}
		}
	};
	public class BLEBinder extends Binder
	{
		public BLEService getService()
		{
			return BLEService.this;
		}
	}
	public String getString()
	{
		return readString;
	}
	public Boolean getIsSuccess()
	{
		return true;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		bleCtrl = BluetoothController.getInstance();
		bleCtrl.setServiceHandler(handler);
	}

	public void onStart(Intent intent, int startId)
	{
		bleCtrl = BluetoothController.getInstance();
		bleCtrl.setServiceHandler(handler);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bleCtrl = BluetoothController.getInstance();
        bleCtrl.setServiceHandler(handler);
        return super.onStartCommand(intent, flags, startId);
    }
}
