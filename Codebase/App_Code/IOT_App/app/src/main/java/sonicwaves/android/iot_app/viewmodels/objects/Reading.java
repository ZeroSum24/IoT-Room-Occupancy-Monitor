package sonicwaves.android.iot_app.viewmodels.objects;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;

public class Reading {

    private Boolean activated;
    private String sensor_timestamp;
    private Date currentDate;
    private String app_timestamp;
    private String sensor;
    private Date initialDeviceTime;
    private String doorStr;

    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";

    private static final String TAG = "FirebaseUtils";

    public Reading(DiscoveredBluetoothDevice device, String sensor, String activated, Date initialDeviceTime) {
        this.sensor = sensor;
        this.activated = null;
        this.currentDate = new Date();
        this.app_timestamp = formatDate(this.currentDate);

        if (initialDeviceTime != null) {
            this.initialDeviceTime = initialDeviceTime;
            this.sensor_timestamp = formatDate(sensorTimestamp(activated));
        } else {
            this.doorStr = parseDoor(activated);
        }

        Log.e("Reading", toString());
        Log.e("Reading", activated);
        Map<String, Object> data = createDataDeviceClass(device, activated);

        uploadToFirebase(device, data);
    }

    private Map createDataDeviceClass(DiscoveredBluetoothDevice device, String activated) {

        DeviceClass deviceClass = device.getDeviceClass();
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", this.app_timestamp);

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {

            this.activated = statusValue(activated);
            data.put("sensor_name", this.sensor);
            data.put("activated", this.activated);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {

//            this.activated =
// (activated);
            data.put("sensor_name", this.sensor);
            data.put("activated", this.doorStr);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            //Iterate over all the sensor ids for the chairs
            // TODO check this works with a real table device
            Log.e(TAG, "data update here");

            int iterLen = activated.length();
            Log.e(TAG, activated.substring(11, 16));

            // calculating the chair id
            String chairID = activated.substring(14, 16) + activated.substring(11, 13);
            Log.e(TAG, "chair hex id:" + chairID);
            Log.e(TAG, "long chair val: " + String.valueOf(Long.parseLong(chairID, 16)));

            chairID = String.valueOf(Long.parseLong(chairID, 16));

            String rssiVal = activated.substring(20, 22) + activated.substring(17, 19);
            rssiVal = String.valueOf(Long.parseLong(rssiVal, 16));

            Log.e(TAG, "iterLen: " + String.valueOf(iterLen) + " chairID: " + chairID + " rssiVal: " + rssiVal);

            data.put("chair_id", chairID);
            data.put("rssi_val", rssiVal);

        }

        return data;
    }

    private void uploadToFirebase(DiscoveredBluetoothDevice device, Map data) {
        DeviceClass deviceClass = device.getDeviceClass();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String deviceName = device.getName().substring(0, 16);

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            // Add a new document with a generated ID
            db.collection(CHAIR_DATA).document(deviceName)
                    .collection("detections")
                    .document(this.getAppTimestamp())
                    .set(data);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            // Add a new document with a generated ID
            Log.e(TAG, deviceName);
            db.collection(DOOR_DATA).document(deviceName)
                    .collection("detections")
                    .document(this.getAppTimestamp())
                    .set(data);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            // Add a new document with a generated ID
            db.collection(TABLE_DATA).document(deviceName)
                    .collection("detections")
                    .document(this.getAppTimestamp())
                    .set(data);
        }
    }


    public String getAppTimestamp() {
        return app_timestamp;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getSensor() {
        return sensor;
    }

    /***
     * Parses the string given from the sensor reading into a device status value
     *
     * @param activated Sensor reading string
     *
     * @return boolean to update the activated class variable
     */
    private boolean statusValue(String activated) {
        Log.e("Reading", "reading here");

        boolean triggered = false;

        int status = Integer.valueOf(activated.substring(12, 13));
        if (status == 1) {
            triggered = true;
        }
        Log.e(TAG, "status str: " + String.valueOf(status) + " " + String.valueOf(triggered));

        return triggered;
    }


    /***
     * Parses the string given from the sensor reading into a sensor timestamp
     *
     * @param activated Sensor reading string
     *
     * @return date to update the sensor timestamp variable
     */
    private Date sensorTimestamp(String activated) {
        Log.e("Reading", "reading here");

        String timestamp = activated.substring(8, 10) + activated.substring(5, 7);
        long hexVal = Long.parseLong(timestamp, 16);
        long secs = (this.initialDeviceTime.getTime() / 1000);
        long sensorTimeLong = secs + hexVal;
        Date sensorDate = new Date(sensorTimeLong * 1000);

        Log.e(TAG, "date: " + sensorDate.toString() + " timestamp: " + String.valueOf(timestamp)
                + " hexVal: " + String.valueOf(hexVal) + " secs: " + String.valueOf(secs)
                + " sensorTime: " + String.valueOf(sensorTimeLong));

        return sensorDate;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(sensor) + " " + String.valueOf(activated) + " " + String.valueOf(app_timestamp) + String.valueOf(sensor_timestamp);
    }

    private String parseDoor(String activated) {

        String test = activated.substring(6,7);
        Log.e(TAG, test);
        return test;
    }

    private String formatDate(Date date) {

        String pattern = "yyyy-MM-dd_HH-mm-ss-SSS";
        TimeZone zone = TimeZone.getTimeZone("GMT");

        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        format.setTimeZone(zone);

        //This method returns the time in millislong timeMilli = date.getTime();
        return format.format(date);
    }

}
