package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class RunningStatus {
    private static List<String> mStatusList;
    public static List<String> getStatusList(){
        mStatusList = new ArrayList<>();
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
}
