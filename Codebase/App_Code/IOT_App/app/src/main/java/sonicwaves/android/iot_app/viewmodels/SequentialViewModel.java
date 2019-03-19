package sonicwaves.android.iot_app.viewmodels;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import sonicwaves.android.iot_app.ApplicationData;
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
    private MutableLiveData<Boolean> isConnectedMut = new MutableLiveData<>();
    private Boolean isConnected = true;
    private MutableLiveData<Boolean> isSupported = new MutableLiveData<>();
    private MutableLiveData<String> isCalibrated = new MutableLiveData<>();
    private Date initTimestamp;
    private Date currentDate =  new Date();

    private final static String DIST_ONE = new Door().DIST_INTERNAL;
    private final static String DIST_TWO = new Door().DIST_EXTERNAL;
    private final static String DEVICE_SIGNAL_STRENGTH = "DEVICE_SIGNAL_STRENGTH";
    private final static String PRESSURE = "Pressure";
    private ApplicationData app;


    /**
     * Iterate through all the devices and get their readings
     *
     * @param activity     application for the BlinkyViewModel
     * @param dBDeviceList list of discovered bluetooth devices
     * @return true when all the devices are complete
     */
    public HashMap<DiscoveredBluetoothDevice, List<Reading>> iterateThroughDevices(Activity activity, List<DiscoveredBluetoothDevice> dBDeviceList) {
        HashMap<DiscoveredBluetoothDevice, List<Reading>> deviceReadings = new HashMap<>();

        for (DiscoveredBluetoothDevice device : dBDeviceList) {

            device.setDeviceClass(new DeviceClass(device));
            currentDeviceIndex = dBDeviceList.indexOf(device);

            //connect to further device
            mViewModel = new BlinkyViewModel(activity.getApplication(), device.getDeviceClass());
            lifecycleOwner = (LifecycleOwner) activity;
            System.out.print("HERE1");
            observeDeviceConnection(mViewModel);
            System.out.print("HERE");
            readingsForClass(mViewModel, device);
            // do something here
            //Log.d(TAG, device.getName() + " " + device.getDeviceClass());

            // add the device to the device readings
//            deviceReadings.put(device, readingsList);
        }
        return deviceReadings;
    }

    /**
     * Iterate through all the devices and get their readings
     *
     * @param activity application for the BlinkyViewModel
     * @param device   current discovered bluetooth devices
     * @return true when all the devices are complete
     */
    public void iterateThroughDevices(Activity activity, DiscoveredBluetoothDevice device) {
//        HashMap<DiscoveredBluetoothDevice, List<Reading>> deviceReadings = new HashMap<>();


        device.setDeviceClass(new DeviceClass(device));

        //connect to further device
        mViewModel = new BlinkyViewModel(activity.getApplication(), device.getDeviceClass());
        lifecycleOwner = (LifecycleOwner) activity;
        System.out.print("HERE1");
        observeDeviceConnection(mViewModel);
        System.out.print("HERE");
        app = (ApplicationData) activity.getApplication();
        readingsForClass(mViewModel, device);
        // do something here
//            //Log.d(TAG, device.getName() + " " + device.getDeviceClass());
//
//            // add the device to the device readings
//            deviceReadings.put(device, readingsList);

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
     * <p>
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a chair object
     */
    private Chair convertToChair(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getAppTimestamp();
        String finalTimestamp = "";
        Chair chair = new Chair();
        int i = 0;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size() - 1) {

            Reading reading = readingList.get(i);
            String readingName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size() - 1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (readingName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated() && comparedReading.isActivated() != isActivated) { //should always be true
                        finalTimestamp = reading.getAppTimestamp();
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
     * <p>
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a chair object
     */
    private Door convertToDoor(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getAppTimestamp();
        String finalTimestamp = "";
        Door door = new Door();
        int i = 0;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size() - 1) {

            Reading reading = readingList.get(i);
            String sensorName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size() - 1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (sensorName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated() && comparedReading.isActivated() != isActivated) { //should always be true
                        finalTimestamp = reading.getAppTimestamp();

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
     * <p>
     * NB: Assuming the readings are order on arrival
     *
     * @param readingList a list of device readings
     * @return a table object
     */
    private Table convertToTable(List<Reading> readingList) {

        String initialTimestamp = readingList.get(0).getAppTimestamp();
        String finalTimestamp = "";
        Table table = new Table();
        int i = 0;
        int j;

        // iterate over all the readings in the table
        while (i <= readingList.size() - 1) {

            Reading reading = readingList.get(i);
            String readingName = reading.getSensor();
            boolean isActivated = reading.isActivated();
            j = i;

            while (j <= readingList.size() - 1) {

                Reading comparedReading = readingList.get(j);

                // sets the final timestamp for the reading
                if (readingName.equals(readingList.get(j).getSensor())) {

                    if (!comparedReading.isActivated() && comparedReading.isActivated() != isActivated) { //should always be true
                        finalTimestamp = reading.getAppTimestamp();
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
        mViewModel.isConnected().observe(lifecycleOwner, connected -> {
            isConnectedMut.setValue(connected);
            isConnected = connected;
            Log.e("Reading watcher", connected.toString());
        });
        //Log.d(TAG, String.valueOf(connected));
        mViewModel.isSupported().observe(lifecycleOwner, supported -> {
            isSupported.setValue(supported);
        });
        mViewModel.getIsCalibrated().observe(lifecycleOwner, calibrated -> {
            isCalibrated.setValue(calibrated);
        });
    }

    // Flag to determine if the device is connected
    private void readingsForClass(BlinkyViewModel mViewModel, DiscoveredBluetoothDevice device) {
        DeviceClass deviceClass = new DeviceClass(device);
        mViewModel.connect(device);
        System.out.print("Reading For Class");


        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            //CHAIR readings update
            Log.e(TAG, "chair here");
            mViewModel.getmPressure().observe(lifecycleOwner,
                    pressed -> {
                        initTimestamp = parseInitTimestampString(pressed, initTimestamp, currentDate);
                        Reading reading = (new Reading(device, PRESSURE, pressed, initTimestamp));
                    });

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            //TABLE readings update
            Log.e(TAG, "table here");

            mViewModel.getmDeviceSignalStrength().observe(lifecycleOwner,
                    tripped -> {Reading reading = (new Reading(device, DEVICE_SIGNAL_STRENGTH, tripped, initTimestamp));});

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            //DOOR readings update
            Log.e(TAG, "door here");

            mViewModel.getmDistOne().observe(lifecycleOwner,
                    tripped -> {Reading reading = (new Reading(device, DIST_ONE, tripped, initTimestamp));});

            mViewModel.getmDistTwo().observe(lifecycleOwner,
                    tripped -> {Reading reading = (new Reading(device, DIST_TWO, tripped, initTimestamp));});
        }

    }

    public int getCurrentDeviceIndex() {
        return currentDeviceIndex;
    }

    public MutableLiveData<Boolean> getIsConnectedMut() {
        return isConnectedMut;
    }

    public MutableLiveData<Boolean> getIsSupported() {
        return isSupported;
    }

    /***
     * Parses the string given from the sensor reading into the appropriate values
     *
     * @param activated Sensor reading string
     *
     * @return boolean to update the activated class variable
     */
    private Date parseInitTimestampString(String activated, Date initTimestamp, Date currentDate) {

        String timestampStr;
        Date timestampOut;

        if (initTimestamp == null) {
            timestampStr = activated.substring(8, 10) + activated.substring(5, 7);
            long hexVal = Long.parseLong(timestampStr, 16);
            long secs = (currentDate.getTime())/1000;
            long sensorTimeLong = secs - hexVal;
            Date initSensorTime = new Date(sensorTimeLong*1000);

            Log.e(TAG, "date: " + initSensorTime.toString() + " timestamp: "+String.valueOf(timestampStr)
                    + " hexVal: " + String.valueOf(hexVal) + " secs: " + String.valueOf(secs)
                    + " sensorTime: " + String.valueOf(sensorTimeLong));

            timestampOut = initSensorTime;
        } else {
            timestampOut = initTimestamp;
        }

        return timestampOut;
    }

}
