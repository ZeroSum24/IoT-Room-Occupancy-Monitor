package no.nordicsemi.android.blinky.firebase.objects.chair;

import java.util.HashMap;
import java.util.Map;

public class Chair {

    private String chairUID;
    private Map<String, ChairDetection> detections = new HashMap<>();

    public void addDetection(ChairDetection detection) {

        String detectionName = "detection_" + Integer.toString(detections.size());
        detections.put(detectionName, detection);
    }

    public String getChairUID() {
        return chairUID;
    }

    public Map<String, ChairDetection> getDetections() {
        return detections;
    }

}
