package jsonmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SEUXXD on 2017-09-17.
 */

public class ErrorModel {
    private Map<String,Integer> mErrorMaps = new HashMap<>();

    public void setErrorMaps(Map<String, Integer> errorMaps) {
        mErrorMaps = errorMaps;
    }

    public Map<String, Integer> getErrorMaps() {
        return mErrorMaps;
    }
}
