package sonicwaves.android.iot_app.firebase.objects.door;

public class DoorDetection {

    private final String sensorName;
    private final String initialTimestamp;
    private final String finalTimestamp;


    public DoorDetection(String sensorName, String initialTimestamp, String finalTimestamp) {
        this.sensorName = sensorName;
        this.initialTimestamp = initialTimestamp;
        this.finalTimestamp = finalTimestamp;
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
}
