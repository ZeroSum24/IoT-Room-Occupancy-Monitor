package sonicwaves.android.iot_app.viewmodels;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.firebase.objects.FirebaseHolder;
import sonicwaves.android.iot_app.firebase.objects.chair.Chair;
import sonicwaves.android.iot_app.firebase.objects.chair.ChairDetection;
import sonicwaves.android.iot_app.firebase.objects.door.Door;
import sonicwaves.android.iot_app.firebase.objects.door.DoorDetection;
import sonicwaves.android.iot_app.firebase.objects.table.Table;
import sonicwaves.android.iot_app.firebase.objects.table.TableDetection;
import sonicwaves.android.iot_app.viewmodels.objects.DeviceClass;
import sonicwaves.android.iot_app.viewmodels.objects.Reading;

public class SequentialViewModel {

    private final static String TAG = "SViewModel";
    private BlinkyViewModel mViewModel;
    private LifecycleOwner lifecycleOwner;
    private int currentDeviceIndex;
    private MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSupported = new MutableLiveData<>();

    private final static String DIST_ONE = new Door().DIST_INTERNAL;
    private final static String DIST_TWO = new Door().DIST_EXTERNAL;
    private final static String PIR = "PIR";
    private final static String PRESSURE = "Pressure";


    /**
     * Iterate through all the devices and get their readings
     *
     * @param activity application for the BlinkyViewModel
     * @param dBDeviceList list of discovered bluetooh devices
     * @return true when all the devices are complete
     */
    private HashMap<DiscoveredBluetoothDevice, List<Reading>> iterateThroughDevices(Activity activity, List<DiscoveredBluetoothDevice> dBDeviceList) {
        HashMap<DiscoveredBluetoothDevice, List<Reading>> deviceReadings = new HashMap<>();

        for (DiscoveredBluetoothDevice device: dBDeviceList) {

            device.setDeviceClass(new DeviceClass(device));
            currentDeviceIndex = dBDeviceList.indexOf(device);

            //connect to further device
            mViewModel = new BlinkyViewModel(activity.getApplication(), device.getDeviceClass());
            lifecycleOwner = (LifecycleOwner) activity;
            observeDeviceConnection(mViewModel);
            List<Reading> readingsList = readingsForClass(mViewModel, device);
            // do something here
            //Log.d(TAG, device.getName() + " " + device.getDeviceClass());

            // add the device to the device readings
            deviceReadings.put(device, readingsList);
        }
        return deviceReadings;
    }

    /***
     * Converts the readings for each device into the appropriate object for their class
     *
     * @param activity passed to submethods
     * @param dBDeviceList  a list of discovered bluetooth devices
     * @return the Firebase data holder
     */
    public FirebaseHolder getFirebaseInfo(Activity activity, List<DiscoveredBluetoothDevice> dBDeviceList) {

        HashMap<DiscoveredBluetoothDevice, List<Reading>> deviceReadings = iterateThroughDevices(activity, dBDeviceList);
        List<Chair> chairList = new ArrayList<>();
        List<Door> doorList = new ArrayList<>();
        List<Table> tableList = new ArrayList<>();
        FirebaseHolder firebaseHolder = new FirebaseHolder();
        firebaseHolder.setDeviceReadings(deviceReadings);

        Iterator it = deviceReadings.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry curIter = (Map.Entry) it.next();

            DiscoveredBluetoothDevice device = (DiscoveredBluetoothDevice) curIter.getKey();

            boolean isChairDevice = device.getDeviceClass().getDeviceClass().equals(device.getDeviceClass().CHAIR);
            boolean isDoorDevice = device.getDeviceClass().getDeviceClass().equals(device.getDeviceClass().DOOR);
            boolean isTableDevice = device.getDeviceClass().getDeviceClass().equals(device.getDeviceClass().TABLE);

            List<Reading> readings = deviceReadings.get(device);
            if (readings != null && readings.size() != 0) {

                if (isChairDevice) {
                    chairList.add(convertToChair(readings));
                } else if (isDoorDevice) {
                    doorList.add(convertToDoor(readings));
                } else if (isTableDevice) {
                    tableList.add(convertToTable(readings));
                }
            }

            //System.out.printlnL(curIter.getKey() + " = " + curIter.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        // update chairList
        if (chairList.size() != 0) {
            firebaseHolder.setChairList(chairList);
        }

        // update doorList
        if (doorList.size() != 0) {
            firebaseHolder.setDoorList(doorList);
        }

        // update tableList
        if (tableList.size() != 0) {
            firebaseHolder.setTableList(tableList);
        }

        return firebaseHolder;
    }

    /**
     * Method converts the readings from the chair sensors to the table object
     *
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a chair object
     */
    private Chair convertToChair(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getTimestamp();
        String finalTimestamp = "";
        Chair chair = new Chair();
        int i = 0 ;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size()-1) {

            Reading reading = readingList.get(i);
            String readingName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size()-1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (readingName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated()  && comparedReading.isActivated()!=isActivated) { //should always be true
                        finalTimestamp = reading.getTimestamp();
                        chair.addDetection(new ChairDetection("001", initialTimestamp, finalTimestamp));

                        // remove the used readings from the list
                        readingList.remove(i);
                        readingList.remove(j);
                    }
                }
                j++;

            }
            i++;
        }

        return chair;
    }

    /**
     * Method converts the readings from the chair sensors to the table object
     *
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a chair object
     */
    private Door convertToDoor(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getTimestamp();
        String finalTimestamp = "";
        Door door = new Door();
        int i = 0;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size()-1) {

            Reading reading = readingList.get(i);
            String sensorName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size()-1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (sensorName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated()  && comparedReading.isActivated()!=isActivated) { //should always be true
                        finalTimestamp = reading.getTimestamp();

                        door.addDetection(new DoorDetection(sensorName, initialTimestamp, finalTimestamp));

                        // remove the used readings from the list
                        readingList.remove(i);
                        readingList.remove(j);
                    }
                }
                j--;

            }
            i--;
        }

        return door;
    }

    /**
     * Method converts the readings from the table sensors to the table object
     *
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a table object
     */
    private Table convertToTable(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getTimestamp();
        String finalTimestamp = "";
        Table table = new Table();
        int i = 0;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size()-1) {

            Reading reading = readingList.get(i);
            String readingName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size()-1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (readingName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated()  && comparedReading.isActivated()!=isActivated) { //should always be true
                        finalTimestamp = reading.getTimestamp();
                        table.addDetection(new TableDetection(reading.getSensor(), initialTimestamp, finalTimestamp));

                        // remove the used readings from the list
                        readingList.remove(i);
                        readingList.remove(j);
                    }
                }
                j--;

            }
            i--;
        }

        return table;
    }

    private void observeDeviceConnection(BlinkyViewModel mViewModel) {

        // watching the text value of the connection state
        mViewModel.getConnectionState().observe(lifecycleOwner, text -> {
            if (text != null) {
                Log.d(TAG, text);
            }
		});

        // watching the viewModel connection state
        mViewModel.isConnected().observe(lifecycleOwner, connected -> {isConnected.setValue(connected);});
        //Log.d(TAG, String.valueOf(connected));
        mViewModel.isSupported().observe(lifecycleOwner, supported -> {isSupported.setValue(supported);});
    }

    // Flag to determine if the device is connected
    private List<Reading> readingsForClass(BlinkyViewModel mViewModel, DiscoveredBluetoothDevice device){
        List<Reading> readingsList = new ArrayList<>();
        DeviceClass deviceClass = new DeviceClass(device);
        mViewModel.connect(device);

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            //CHAIR readings update
            Log.e(TAG, "chair here");
            mViewModel.getmPressure().observe(lifecycleOwner,
                    pressed -> readingsList.add(new Reading(PRESSURE, pressed)));

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            //TABLE readings update

            mViewModel.getmPIR().observe(lifecycleOwner,
                    tripped -> readingsList.add(new Reading(PIR, tripped)));

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            //DOOR readings update
            Log.e(TAG, "door here");

            mViewModel.getmDistOne().observe(lifecycleOwner,
                    tripped -> readingsList.add(new Reading(DIST_ONE, tripped)));

            mViewModel.getmDistTwo().observe(lifecycleOwner,
                    tripped -> readingsList.add(new Reading(DIST_TWO, tripped)));
        }

        if (readingsList.size() == 0) {
            Log.e(TAG, "No readings for device");
        } else {
            //Log all readings
            for (Reading reading: readingsList) {
                if (reading != null) {
                    Log.e(TAG, reading.toString());
                } else {
                    Log.e(TAG, "reading is null");
                }
            }
        }

        //disconnect from device
//            mViewModel.disconnect();
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
