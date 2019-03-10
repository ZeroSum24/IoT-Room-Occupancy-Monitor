package sonicwaves.android.iot_app.firebase.objects.table;

import java.util.List;

public class TableDetection {

    private final String timestamp;
    private final String duration;
    private final Boolean detection;


    public TableDetection(String timestamp, String duration, Boolean detection) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.detection = detection;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDuration() {
        return duration;
    }

    public Boolean getDetection() {
        return detection;
    }
}
