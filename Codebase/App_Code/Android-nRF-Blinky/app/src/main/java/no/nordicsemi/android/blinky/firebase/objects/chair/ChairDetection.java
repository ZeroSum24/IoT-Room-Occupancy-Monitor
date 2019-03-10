package no.nordicsemi.android.blinky.firebase.objects.chair;

import java.util.List;

public class ChairDetection {

    private final String tableUID;
    private final String timestamp;
    private final float pressure_amount;
    private final float pressure_duration;


    public ChairDetection(String tableUID, String timestamp, float pressure_amount, float pressure_duration) {
        this.tableUID = tableUID;
        this.timestamp = timestamp;
        this.pressure_amount = pressure_amount;
        this.pressure_duration = pressure_duration;
    }

    public String getTableUID() {
        return tableUID;
    }

    public float getPressure_duration() {
        return pressure_duration;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getPressure_amount() {
        return pressure_amount;
    }
}
