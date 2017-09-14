package ble;

public class ConstantUtils {
	public final static int WM_STOP_SCAN_BLE=1;
	public final static int WM_UPDATE_BLE_LIST=2;
	public final static int  WM_BLE_CONNECTED_STATE_CHANGE=3;
	public final static int WM_RECEIVE_MSG_FROM_BLE=4;
	public final static int WM_STOP_CONNECT=5;

	public final static String ACTION_UPDATE_DEVICE_LIST="action.update.device.list";
	public final static String  ACTION_CONNECTED_ONE_DEVICE="action.connected.one.device";
	public final static String ACTION_RECEIVE_MESSAGE_FROM_DEVICE="action.receive.message";
	public final static String ACTION_STOP_CONNECT="action.stop.connect";
	public final static String ACTION_BLUETOOTH_CONNECTED = "action.bluetooth.connected";
	

	public final static  String UUID_SERVER="0000ffe0-0000-1000-8000-00805f9b34fb";
	public final static  String UUID_NOTIFY="0000ffe1-0000-1000-8000-00805f9b34fb";

}
