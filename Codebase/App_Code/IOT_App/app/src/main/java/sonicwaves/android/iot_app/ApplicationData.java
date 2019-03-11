package sonicwaves.android.iot_app;
import android.app.Application;

import java.util.List;

import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.firebase.FirebaseUtils;


public class ApplicationData extends Application {
    private List<DiscoveredBluetoothDevice> selectedDevices;

    public List<DiscoveredBluetoothDevice> getDevices() {
        return selectedDevices;
    }

    public void setDevices(List<DiscoveredBluetoothDevice> devices) {
        this.selectedDevices = devices;
    }
}
