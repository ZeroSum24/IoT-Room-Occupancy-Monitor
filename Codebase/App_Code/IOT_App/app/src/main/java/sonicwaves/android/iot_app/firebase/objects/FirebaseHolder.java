package sonicwaves.android.iot_app.firebase.objects;

import java.util.List;

import sonicwaves.android.iot_app.firebase.objects.chair.Chair;
import sonicwaves.android.iot_app.firebase.objects.door.Door;
import sonicwaves.android.iot_app.firebase.objects.table.Table;

public class FirebaseHolder {

    private List<Chair> chairList;
    private List<Door> doorList;
    private List<Table> tableList;

//
//    public FirebaseHolder(List<Chair> chairList, List<Door> doorList, List<Table> tableList) {
//        this.chairList = chairList;
//        this.doorList = doorList;
//        this.tableList = tableList;
//    }

    public void setChairList(List<Chair> chairList) {
        this.chairList = chairList;
    }

    public void setDoorList(List<Door> doorList) {
        this.doorList = doorList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public List<Chair> getChairList() {
        return chairList;
    }

    public List<Door> getDoorList() {
        return doorList;
    }

    public List<Table> getTableList() {
        return tableList;
    }
}
