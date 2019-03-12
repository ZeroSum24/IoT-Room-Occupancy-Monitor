package sonicwaves.android.iot_app.firebase.objects.chair;

public class ChairDetection {

    private final String tableUID;
    private final String initialTimestamp;
    private final String finalTimestamp;


    public ChairDetection(String tableUID, String initialTimestamp, String finalTimestamp) {
        this.tableUID = tableUID;
        this.initialTimestamp = initialTimestamp;
        this.finalTimestamp = finalTimestamp;
    }

    public String getTableUID() {
        return tableUID;
    }

    public String getInitialTimestamp() {
        return initialTimestamp;
    }

    public String getFinalTimestamp() {
        return finalTimestamp;
    }
}
