package sonicwaves.android.iot_app.firebase.objects.door;

import java.util.HashMap;
import java.util.Map;

public class Door {

    private Map<String, DoorDetection> detections = new HashMap<>();
    public String DIST_INTERNAL = "Dist_internal";
    public String DIST_EXTERNAL = "Dist_external";

    public Map<String, DoorDetection> getDoorSensors() {
        return detections;
    }

    /**
     * Method adds a detection to the list of door detection.
     *
     * @param doorDetection detection at the door
     */
    public void addDetection(DoorDetection doorDetection) {
        String detectionName = "detection_" + Integer.toString(detections.size());
        detections.put(detectionName, doorDetection);
    }
}
