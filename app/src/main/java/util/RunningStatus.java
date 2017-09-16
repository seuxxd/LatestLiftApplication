package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class RunningStatus {

//    上传时的KEY
    public static List<String> getStatusList(){
        List<String> mStatusList = new ArrayList<>();
        mStatusList.add("RTC");
        mStatusList.add("RRC");
        mStatusList.add("RSC");
        mStatusList.add("RRS");
        mStatusList.add("RDS");
        mStatusList.add("RLU");
        mStatusList.add("RLD");
        mStatusList.add("RLS");
        mStatusList.add("RUL");
        mStatusList.add("RDL");
        mStatusList.add("RAB");


        return mStatusList;
    }
//    显示在客户端的KEY
    public static List<String> getStatusChineseList(){
        List<String> mStatusList = new ArrayList<>();
        mStatusList.add("总接触器");
        mStatusList.add("运行接触器");
        mStatusList.add("安全回路");
        mStatusList.add("运行状态");
        mStatusList.add("门状态");
        mStatusList.add("轿厢上行");
        mStatusList.add("轿厢下行");
        mStatusList.add("平层状态");
        mStatusList.add("上极限");
        mStatusList.add("下极限");
        mStatusList.add("报警按钮");


        return mStatusList;
    }
//    显示在客户端的value字符串对于0
    public static List<String> getZeroRunningInfoValue(){
        List<String> mList = new ArrayList<>();
        mList.add("断开");
        mList.add("断开");
        mList.add("断开");
        mList.add("检修");
        mList.add("打开");
        mList.add("停止");
        mList.add("停止");
        mList.add("未平层");
        mList.add("正常");
        mList.add("正常");
        mList.add("正常");

        return mList;
    }
//   显示在客户端的value字符串对于1
    public static List<String> getOneRunningInfoValue(){
        List<String> mList = new ArrayList<>();
        mList.add("吸合");
        mList.add("运行");
        mList.add("正常");
        mList.add("正常");
        mList.add("关闭");
        mList.add("上行");
        mList.add("下行");
        mList.add("平层");
        mList.add("动作");
        mList.add("动作");
        mList.add("动作");
        return mList;
    }

//    上传到服务器的运行信息value数字表示
    public static List<Integer> getmRunningInfoNumberValue(){
        List<Integer> mList = new ArrayList<>();
        for (int i = 0 ; i < 11 ; i ++)
            mList.add(3);
        return mList;
    }
}
