package sonicwaves.android.iot_app.viewmodels.objects;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;

public class Reading {

    private boolean activated;
    private String timestamp;
    private String sensor;

    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";

    private static final String TAG = "FirebaseUtils";

    //    public Reading(String sensor, boolean activated) {
    public Reading(DiscoveredBluetoothDevice device, String sensor, String activated) {
        this.sensor = sensor;
        this.activated = parseString(activated);

        String pattern = "yyyy-MM-dd_hh/mm/ss/a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);

        //This method returns the time in millislong timeMilli = date.getTime();
        // System.out.println(“Time in milliseconds using Date class: ” + timeMilli);
        this.timestamp = simpleDateFormat.format(new Date());

        Log.e("Reading", toString());

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);
        uploadToFirebase(device, user);
    }

    private void uploadToFirebase(DiscoveredBluetoothDevice device, Map testHash) {
        DeviceClass deviceClass = device.getDeviceClass();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            // Add a new document with a generated ID
//            db.collection(CHAIR_DATA).add(device.getName());
//            DocumentReference chairRef =
                    db.collection(CHAIR_DATA).document(device.getName()).collection("test").add(testHash)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }


    public String getTimestamp() {
        return timestamp;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getSensor() {
        return sensor;
    }

    /***
     * Parses the string given from the sensor reading into the appropriate values
     *
     * @param activated Sensor reading string
     *
     * @return boolean to update the activated class variable
     */
    private boolean parseString(String activated) {
        Log.e("Reading", "reading here");

        boolean triggered = false;

        int status = activated.charAt(6) - 48;
        if (status == 1) {
            triggered= true;
        }

        return triggered;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(sensor) + " " + String.valueOf(activated) + " " + String.valueOf(timestamp);
    }
}
