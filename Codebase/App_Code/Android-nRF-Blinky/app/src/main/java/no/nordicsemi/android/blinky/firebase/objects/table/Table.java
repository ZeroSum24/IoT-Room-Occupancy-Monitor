package no.nordicsemi.android.blinky.firebase.objects.table;

import java.util.HashMap;
import java.util.Map;

public class Table {

    private String tableUID;
    private Map<String, TableDetection> detections = new HashMap<>();

    public void addDetection(TableDetection detection) {

        String detectionName = "detection_" + Integer.toString(detections.size());
        detections.put(detectionName, detection);
    }

    public String getTableUID() {
        return tableUID;
    }

    public Map<String, TableDetection> getDetections() {
        return detections;
    }

}
