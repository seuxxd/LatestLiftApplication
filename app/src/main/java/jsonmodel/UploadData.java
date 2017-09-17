package jsonmodel;

/**
 * Created by SEUXXD on 2017-09-17.
 */

public class UploadData {
    private String taskListID;
    private Data tempData;

    public void setTaskListID(String taskListID) {
        this.taskListID = taskListID;
    }

    public void setTempData(Data tempData) {
        this.tempData = tempData;
    }

    public String getTaskListID() {

        return taskListID;
    }

    public Data getTempData() {
        return tempData;
    }
}
