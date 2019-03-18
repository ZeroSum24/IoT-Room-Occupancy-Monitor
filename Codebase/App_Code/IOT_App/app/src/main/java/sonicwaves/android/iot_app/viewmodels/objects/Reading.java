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

    private boolean activated;
    private String sensor_timestamp;
    private Date currentDate;
    private String app_timestamp;
    private String sensor;

    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";

    private static final String TAG = "FirebaseUtils";

    //    public Reading(String sensor, boolean activated) {
    public Reading(DiscoveredBluetoothDevice device, String sensor, String activated) {
        this.sensor = sensor;
        this.activated = statusValue(activated);
        this.currentDate = new Date();
        this.app_timestamp = formatDate(this.currentDate);
        this.sensor_timestamp = formatDate(sensorTimestamp(activated));

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

            data.put("sensor_name", this.sensor);
            data.put("activated", this.activated);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {

            data.put("sensor_name", this.sensor);
            data.put("activated", this.activated);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            //Iterate over all the sensor ids for the chairs
            // TODO check this works with a real table device

            activated = "(0x) 72-0E-00-00-72-0E-00-D5-72-0E-0f-30-26-0E-00-00-62-0E-13-20-72-0E-00-50-72-0E-D0-00";
            int iterLen = activated.length()/4;

            for (int i = 8; i < iterLen; i+=8 ) {
               String chairID = activated.substring(i, i+4);
               String rssiVal = activated.substring(i+4, i+8);

               Log.e(TAG, "iterLen: " + String.valueOf(iterLen) + " chairID: " + chairID + " rssiVal: " + rssiVal );

               int rssi = Integer.valueOf(rssiVal);
               data.put(chairID, rssi);
            }
        }

        return data;
    }

    private void uploadToFirebase(DiscoveredBluetoothDevice device, Map data) {
        DeviceClass deviceClass = device.getDeviceClass();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String deviceName = device.getName().substring(0,16);

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            // Add a new document with a generated ID
            db.collection(CHAIR_DATA).document(deviceName)
                    .collection("detections")
                    .document(this.getAppTimestamp())
                    .set(data);

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            // Add a new document with a generated ID
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

        int status = Integer.valueOf( activated.substring(12,13));
        if (status == 1) {
            triggered= true;
        }
        Log.e(TAG, "status str: "+String.valueOf(status) + " " + String.valueOf(triggered));

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

        String timestamp =  activated.substring(8,10) + activated.substring(5,7);
        long hexVal = Long.parseLong(timestamp, 16);
        long secs = (this.currentDate.getTime())/1000;
        long sensorTimeLong = secs - hexVal;
        Date sensorDate = new Date(sensorTimeLong*1000);

        Log.e(TAG, "date: " + sensorDate.toString() + " timestamp: "+String.valueOf(timestamp)
                + " hexVal: " + String.valueOf(hexVal) + " secs: " + String.valueOf(secs)
                + " sensorTime: " + String.valueOf(sensorTimeLong));

        return sensorDate;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(sensor) + " " + String.valueOf(activated) + " " + String.valueOf(app_timestamp) + String.valueOf(sensor_timestamp);
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
