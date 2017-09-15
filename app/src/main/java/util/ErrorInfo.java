package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class ErrorInfo {
//    上传时的KEY
    public static List<String> getmErrorInfo(){
        List<String> mErrorInfo = new ArrayList<>();
        mErrorInfo.add("ETE");
        mErrorInfo.add("ESCB");
        mErrorInfo.add("EULA");
        mErrorInfo.add("EDLA");
        mErrorInfo.add("EOD");
        mErrorInfo.add("ERO");
        mErrorInfo.add("EROCD");
        mErrorInfo.add("ELOD");
        mErrorInfo.add("ENLS");
        mErrorInfo.add("EAB");

        return mErrorInfo;
    }
//    显示在客户端的KEY
    public static List<String> getmErrorChineseInfo(){
        List<String> mErrorInfo = new ArrayList<>();
        mErrorInfo.add("困人故障");
        mErrorInfo.add("安全回路断开");
        mErrorInfo.add("上极限动作");
        mErrorInfo.add("下极限动作");
        mErrorInfo.add("开门走梯");
        mErrorInfo.add("运行超时");
        mErrorInfo.add("反复开关门");
        mErrorInfo.add("长时间开门");
        mErrorInfo.add("非平层停梯");
        mErrorInfo.add("报警按钮动作");

        return mErrorInfo;
    }
//    上传到服务器的VALUE数字
    public static List<Integer> getmErrorNumberValue(){
        List<Integer> mList = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i ++)
            mList.add(3);
        return mList;
    }

}
