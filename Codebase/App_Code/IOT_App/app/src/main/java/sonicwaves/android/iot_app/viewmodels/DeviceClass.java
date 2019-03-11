package sonicwaves.android.iot_app.viewmodels;

import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;

public class DeviceClass {


    public String CHAIR = "CHAIR";
    public String TABLE = "TABLE";
    public String DOOR = "DOOR";
    private String deviceClass;


    public DeviceClass(DiscoveredBluetoothDevice device) {

        String deviceClassChar = device.getName().substring(11, 12);

        if (deviceClassChar.equals("C")) {
            deviceClass = CHAIR;
        } else if (deviceClassChar.equals("T")) {
            deviceClass = TABLE;
        } else if (deviceClassChar.equals("D")) {
            deviceClass = DOOR;
        }
    }

    public String getDeviceClass() {
        return deviceClass;
    }

}
