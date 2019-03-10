package no.nordicsemi.android.blinky.firebase.objects.door;

import java.util.List;

public class DoorSensor {

    private final String timestamp;
    private final String duration;
    private final Boolean detection;


    public DoorSensor(String sensorName, String timestamp, String duration, Boolean detection) {
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
