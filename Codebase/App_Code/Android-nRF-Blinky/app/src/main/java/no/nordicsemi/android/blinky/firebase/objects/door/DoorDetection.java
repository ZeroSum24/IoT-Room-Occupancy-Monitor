package no.nordicsemi.android.blinky.firebase.objects.door;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoorDetection {

    private Map<String, DoorSensor> doorSensors = new HashMap<>();

    public DoorDetection(DoorSensor dist_external, DoorSensor dist_internal) {
        doorSensors.put("Dist_external", dist_external);
        doorSensors.put("Dist_internal", dist_internal);
    }

    public Map<String, DoorSensor> getDoorSensors() {
        return doorSensors;
    }
}
