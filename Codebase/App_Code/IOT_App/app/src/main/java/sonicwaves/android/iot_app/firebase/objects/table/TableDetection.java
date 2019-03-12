package sonicwaves.android.iot_app.firebase.objects.table;

public class TableDetection {

    private final String sensorName;
    private final String initialTimestamp;
    private final String finalTimestamp;
//    private final Boolean detection;


//    public TableDetection(String sensorName, String initialTimestamp, String finalTimestamp, Boolean detection) {
    public TableDetection(String sensorName, String initialTimestamp, String finalTimestamp) {
        this.sensorName = sensorName;
        this.initialTimestamp = initialTimestamp;
        this.finalTimestamp = finalTimestamp;
//        this.detection = detection;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getInitialTimestamp() {
        return initialTimestamp;
    }

    public String getFinalTimestamp() {
        return finalTimestamp;
    }

//    public Boolean getDetection() {
//        return detection;
//    }
}
