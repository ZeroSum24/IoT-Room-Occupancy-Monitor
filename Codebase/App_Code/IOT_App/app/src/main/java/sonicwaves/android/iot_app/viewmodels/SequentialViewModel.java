package sonicwaves.android.iot_app.viewmodels;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.viewmodels.objects.DeviceClass;
import sonicwaves.android.iot_app.viewmodels.objects.Reading;

public class SequentialViewModel {

    private final static String TAG = "SViewModel";
    private BlinkyViewModel mViewModel;
    private int currentDeviceIndex;
    private MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSupported = new MutableLiveData<>();
    private HashMap<String, List<Reading>> deviceReadings;

    private final static String DIST_ONE = "Dist_One";
    private final static String DIST_TWO = "Dist_Two";
    private final static String PIR = "PIR";
    private final static String PRESSURE = "Pressure";

    public SequentialViewModel() {
        deviceReadings = new HashMap<>();
    }

    /**
     * Iterate through all the devices and get their readings
     *
     * @param activity application for the BlinkyViewModel
     * @param dBDeviceList list of discovered bluetooh devices
     * @return true when all the devices are complete
     */
    public boolean iterateThroughDevices(Activity activity, List<DiscoveredBluetoothDevice> dBDeviceList) {

        for (DiscoveredBluetoothDevice device: dBDeviceList) {

            DeviceClass deviceClass = new DeviceClass(device);
            currentDeviceIndex = dBDeviceList.indexOf(device);
            mViewModel = new BlinkyViewModel(activity.getApplication(), deviceClass);
            observeDeviceConnection(activity, mViewModel);

            List<Reading> readingsList = readingsForClass(activity, mViewModel, device, deviceClass);

            // do something here
            Log.d(TAG, device.getName() + " " + deviceClass);

            // add the device to the device readings
            deviceReadings.put(device.getName(), readingsList);
        }
        return true;
    }

    private void observeDeviceConnection(Activity activity, BlinkyViewModel mViewModel) {
        // watching the viewModel connection state
        mViewModel.isConnected().observe(activity, connected -> {isConnected.setValue(connected);});
        mViewModel.isSupported().observe(activity, supported -> {isSupported.setValue(supported);});
    }

    // Flag to determine if the device is connected
    private List<Reading> readingsForClass(Activity activity, BlinkyViewModel mViewModel, DiscoveredBluetoothDevice device, DeviceClass deviceClass){
        List<Reading> readingsList = new ArrayList<>();
        mViewModel.connect(device);

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            //CHAIR readings update

            mViewModel.getmPressure().observe(activity,
                    pressed -> readingsList.add(new Reading(PRESSURE, pressed)));

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            //TABLE readings update

            mViewModel.getmPIR().observe(activity,
                    tripped -> readingsList.add(new Reading(PIR, tripped)));

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            //DOOR readings update

            mViewModel.getmDistOne().observe(activity,
                    tripped -> readingsList.add(new Reading(DIST_ONE, tripped)));

            mViewModel.getmDistTwo().observe(activity,
                    tripped -> readingsList.add(new Reading(DIST_TWO, tripped)));
        }

        mViewModel.disconnect();
        return readingsList;
    }

    public int getCurrentDeviceIndex() {
        return currentDeviceIndex;
    }

    public MutableLiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    public MutableLiveData<Boolean> getIsSupported() {
        return isSupported;
    }
}
