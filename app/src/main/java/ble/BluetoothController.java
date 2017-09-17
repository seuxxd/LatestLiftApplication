package ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.product.xxd.latestliftapplication.BLEActivity;
import com.product.xxd.latestliftapplication.UIActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import static ble.ConvertUtils.dexToString;


public class BluetoothController {
	private String deviceAddress;
	private String deviceName;

	private BluetoothAdapter bleAdapter;
	private Handler serviceHandler;

	static BluetoothGatt bleGatt;
	static BluetoothGattCharacteristic bleGattCharacteristic;
    //最终发送的字符
    private String mResult = "";

	private static final String TAG = "BluetoothController";


	private static BluetoothController instance = null;

	private BluetoothController()
	{
	}

//	蓝牙控制单例模式
	public static BluetoothController getInstance()
	{
		if (instance == null)
			instance = new BluetoothController();
		return instance;
	}


//    初始化BLE
	public boolean initBLE()
	{

		if (!BLEActivity.sBLEActivity.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			return false;
		}

		final BluetoothManager bluetoothManager = (BluetoothManager) BLEActivity.sBLEActivity.getSystemService(Context.BLUETOOTH_SERVICE);
		bleAdapter = bluetoothManager.getAdapter();
		if (bleAdapter == null)
			return false;
		else
		{
			bleAdapter.enable();
			return true;
		}

	}

//    设置消息传递Handler
	public void setServiceHandler(Handler handler) {

		serviceHandler = handler;
	}

//    扫描回调
	BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback()
	{
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = device.getName();
            if (name == null){
                return;
            }
//            每次扫描到设备后都通过Handler发送一个消息，然后在BLEActivity中进行处理
            if (BluetoothController.this.serviceHandler != null && !name.isEmpty()){
                Log.i(TAG, "onLeScan: " + name);
                Message msg = new Message();
                msg.what = ConstantUtils.WM_UPDATE_BLE_LIST;
                msg.obj  = device;
                BluetoothController.this.serviceHandler.sendMessage(msg);
            }
            else
                Log.i(TAG, "onLeScan: " + "else");
        }
	};


	public void startScanBLE()
	{
		bleAdapter.startLeScan(bleScanCallback);
		/*if (serviceHandler != null)
			serviceHandler.sendEmptyMessageDelayed(ConstantUtils.WM_STOP_SCAN_BLE, 5000);*/
	}


	public void stopScanBLE() {
		bleAdapter.stopLeScan(bleScanCallback);
	}


	public boolean isBleOpen() {
		return bleAdapter.isEnabled();
	}

//    连接BLE设备
	public void connect(EntityDevice device)
	{
		deviceAddress = device.getAddress();
		deviceName = device.getName();
		BluetoothDevice localBluetoothDevice = bleAdapter
				.getRemoteDevice(device.getAddress());
		if (bleGatt != null)
		{

			bleGatt.disconnect();
			bleGatt.close();
			bleGatt = null;
		}
		bleGatt = localBluetoothDevice.connectGatt(BLEActivity.sBLEActivity, false, bleGattCallback);
	}
	public static void disconnect()
	{
		bleGatt.disconnect();
	}


	public BluetoothGattCallback bleGattCallback = new BluetoothGattCallback()
	{

		/*public void onCharacteristicChanged(
				BluetoothGatt paramAnonymousBluetoothGatt,
				BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
		{
//			获取这一次变化的数据
			byte[] arrayOfByte_new = paramAnonymousBluetoothGattCharacteristic
					.getValue();
			if (BluetoothController.this.serviceHandler != null)
			{
				Message msg = new Message();
				msg.what = ConstantUtils.WM_RECEIVE_MSG_FROM_BLE;
				Log.i(TAG, "onCharacteristicChanged: " + arrayOfByte_new.length);
//				msg.obj = ConvertUtils.getInstance().bytesToHexString(arrayOfByte);
				String mResult = ConvertUtils.getInstance().bytesToHexString(arrayOfByte_new);
				mAllResult += mResult;
				if (arrayOfByte_new.length < 20)
				{
					msg.obj = mAllResult;
					BluetoothController.this.serviceHandler.sendMessage(msg);
					Log.i(TAG, "onCharacteristicChanged: mAllResult" + mAllResult);
					mAllResult = "";
				}
				else if (arrayOfByte_new.length == 20)
				{
					if (mResult.endsWith("}"))
					{
						msg.obj = mAllResult;
						BluetoothController.this.serviceHandler.sendMessage(msg);
						mAllResult = "";
					}
					else if (arrayOfByte_new[19] == 'd')//角度的时候正好是20位，所以判断最后一位是d单位也发送
					{
						msg.obj = mAllResult;
						BluetoothController.this.serviceHandler.sendMessage(msg);
						mAllResult = "";
					}
					else
						Log.i(TAG, "onCharacteristicChanged: " + mResult);
				}
			}
		}*/

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
//            获取字节流内容
            byte[] content = characteristic.getValue();
//            本次获取的字节转换成字符串的结果
            String mReadOnce = ConvertUtils.getInstance().bytesToHexString(content);
            mResult += mReadOnce;
//            接下来发送数据
//            首先先进行一系列逻辑判断
            int length = content.length;
            Log.i(TAG, "onCharacteristicChanged: " + mResult);
//            因为通信协议默认发送20个字节，所以判断是否数据还没有接收完
//            如果小于20，说明本次接收完毕，可以发送
            if (length < 20){
                EventBus.getDefault().post(mResult);
                mResult = "";
            }
//            如果等于20，还需要分类讨论一下
            else if (length == 20){
                if (mReadOnce.endsWith("}")){
                    EventBus.getDefault().post(mResult);
                    mResult = "";
                }
                else if (content[19] == 'd'){
					if (!UIActivity.ismIsAllDataButton()){
						EventBus.getDefault().post(mResult);
						mResult = "";
					}
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            判断逻辑，通过newState判断连接状态
            if (newState == 2){
                Message msg = new Message();
                msg.what = ConstantUtils.WM_BLE_CONNECTED_STATE_CHANGE;
                Bundle bundle = new Bundle();
                bundle.putString("address", deviceAddress);
                bundle.putString("name", deviceName);
                msg.obj = bundle;
                serviceHandler.sendMessage(msg);
                gatt.discoverServices();

                return;
            }
            else if (newState == 0){
                serviceHandler.sendEmptyMessage(ConstantUtils.WM_STOP_CONNECT);
                return;
            }
            gatt.disconnect();
            gatt.close();
            return;

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothController.this
                    .findService(gatt.getServices());
        }


	};


	public boolean write(byte byteArray[])
	{
		if (bleGattCharacteristic == null)
			return false;
		if (bleGatt == null)
			return false;
		bleGattCharacteristic.setValue(byteArray);
		return bleGatt.writeCharacteristic(bleGattCharacteristic);
	}

	public boolean write(String str)
	{
		if (bleGattCharacteristic == null)
			return false;
		if (bleGatt == null)
			return false;
		bleGattCharacteristic.setValue(str);
        Log.i(TAG, "write: " + str);
        return bleGatt.writeCharacteristic(bleGattCharacteristic);
	}


	public void findService(List<BluetoothGattService> paramList)
	{

		Iterator localIterator1 = paramList.iterator();
		while (localIterator1.hasNext()) {
			BluetoothGattService localBluetoothGattService = (BluetoothGattService) localIterator1.next();
			if (localBluetoothGattService.getUuid().toString().equalsIgnoreCase(ConstantUtils.UUID_SERVER))
			{
				List localList = localBluetoothGattService.getCharacteristics();
				Iterator localIterator2 = localList.iterator();
				while (localIterator2.hasNext())
				{
					BluetoothGattCharacteristic localBluetoothGattCharacteristic =
							(BluetoothGattCharacteristic) localIterator2.next();
					if (localBluetoothGattCharacteristic.getUuid().toString()
							.equalsIgnoreCase(ConstantUtils.UUID_NOTIFY))
					{
						bleGattCharacteristic = localBluetoothGattCharacteristic;
						break;
					}
				}
				break;
			}

		}

		bleGatt.setCharacteristicNotification(bleGattCharacteristic, true);
	}

//    通过Handler发送一次接收的数据
    private void sendResult(String result){
        if (BluetoothController.this.serviceHandler != null){
            Message msg = new Message();
            msg.what    = ConstantUtils.WM_RECEIVE_MSG_FROM_BLE;
            msg.obj     = result;
            BluetoothController.this.serviceHandler.sendMessage(msg);
        }
    }

}
