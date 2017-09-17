package jsonmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SEUXXD on 2017-09-17.
 */

public class RunningInfoModel {
    private Map<String,Integer> mRunningInfoMaps = new HashMap<>();

    public void setRunningInfoMaps(Map<String, Integer> runningInfoMaps) {
        mRunningInfoMaps = runningInfoMaps;
    }

    public Map<String, Integer> getRunningInfoMaps() {

        return mRunningInfoMaps;
    }
}
