package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class ErrorInfo {
    private static List<String> mErrorInfo;
    public static List<String> getmErrorInfo(){
        mErrorInfo = new ArrayList<>();
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
}
