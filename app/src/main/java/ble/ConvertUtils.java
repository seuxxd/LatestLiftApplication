package ble;

public class ConvertUtils {

	static ConvertUtils instance = null;

	private ConvertUtils() {
	}

	public static ConvertUtils getInstance() {
		if (instance == null)
			instance = new ConvertUtils();
		return instance;
	}


	public String bytesToHexString(byte[] b) {
		if (b.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < b.length; i++) {
			int value = b[i] & 0xFF;
			String hv = Integer.toHexString(value);
			if (hv.length() < 2) {
				sb.append(0);
			}

			sb.append(hv);
		}
		return sb.toString();
	}
	//    十六进制转换成字符串
	public static String dexToString(String str)
	{
		String mSample = "0123456789ABCDEF";
		str = str.toUpperCase();
		char[] dex = str.toCharArray();
		byte[] result = new byte[dex.length / 2];
		int count;
		for (int i = 0 ; i < result.length ; i ++)
		{
			count = mSample.indexOf(dex[2 * i]) * 16;
			count += mSample.indexOf(dex[2 * i + 1]);
			result[i] = (byte)(count & 0xff);
		}
		return new String(result);
	}
}
