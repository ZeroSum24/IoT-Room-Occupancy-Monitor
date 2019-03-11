package sonicwaves.android.iot_app.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import sonicwaves.android.iot_app.R;
import sonicwaves.android.iot_app.firebase.objects.chair.Chair;
import sonicwaves.android.iot_app.firebase.objects.chair.ChairDetection;
import sonicwaves.android.iot_app.firebase.objects.door.DoorDetection;
import sonicwaves.android.iot_app.firebase.objects.door.DoorSensor;
import sonicwaves.android.iot_app.firebase.objects.table.Table;
import sonicwaves.android.iot_app.firebase.objects.table.TableDetection;


public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";
    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";
    private FirebaseFirestore db;


    public FirebaseUtils(Context context) {


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(context.getResources().getString(R.string.firebase_application_id)) // Required for Analytics.
                .setApiKey(context.getResources().getString(R.string.firebase_api_key)) // Required for Auth.
                .setProjectId(context.getResources().getString(R.string.firebase_project_id))
                .build();
        FirebaseApp.initializeApp(context /* Context */, options, "secondary");

        db = FirebaseFirestore.getInstance();
    }

    public void testDb() {
        Log.d(TAG, "Test Database method");
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
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

    public void testDb2() {
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
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

    public void updateChairData(Chair[] chairs) {

        for (Chair chair : chairs) {

            // Add a new document with a generated ID
            db.collection(CHAIR_DATA).add(chair.getChairUID());
            DocumentReference chairRef = db.collection(CHAIR_DATA).document(chair.getChairUID());

            Map<String, ChairDetection> detections = chair.getDetections();

            for (Map.Entry<String, ChairDetection> detection : detections.entrySet()) {
                chairRef.collection(detection.getKey()).add(detection.getValue())
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
    }

    public void updateTableData(Table[] tables) {

        for (Table table : tables) {

            // Add a new document with a generated ID
            db.collection(TABLE_DATA).add(table.getTableUID());
            DocumentReference tableRef = db.collection(TABLE_DATA).document(table.getTableUID());

            Map<String, TableDetection> detections = table.getDetections();

            for (Map.Entry<String, TableDetection> detection : detections.entrySet()) {
                tableRef.collection(detection.getKey()).add(detection.getValue())
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
    }

    public void updateDoorData(DoorDetection[] detections) {


        // Add a new document with a generated ID
        CollectionReference doorRef = db.collection(DOOR_DATA);

        for (DoorDetection detection : detections) {
            Map<String, DoorSensor> doorSensors = detection.getDoorSensors();

            for (Map.Entry<String, DoorSensor> sensor : doorSensors.entrySet()) {

                doorRef.add(sensor.getValue())
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
    }
}
