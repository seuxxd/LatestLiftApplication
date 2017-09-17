package internet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SEUXXD on 2017-09-17.
 */

public class TestClass {
    private String id;
    private Map<String,String> runningInfoMap;

    public TestClass() {
        runningInfoMap = new HashMap<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRunningInfoMap(Map<String, String> runningInfoMap) {
        this.runningInfoMap = runningInfoMap;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getRunningInfoMap() {
        return runningInfoMap;
    }
}
