package sonicwaves.android.iot_app.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import sonicwaves.android.iot_app.R;
import sonicwaves.android.iot_app.firebase.objects.FirebaseHolder;
import sonicwaves.android.iot_app.firebase.objects.chair.Chair;
import sonicwaves.android.iot_app.firebase.objects.chair.ChairDetection;
import sonicwaves.android.iot_app.firebase.objects.door.Door;
import sonicwaves.android.iot_app.firebase.objects.door.DoorDetection;
import sonicwaves.android.iot_app.firebase.objects.table.Table;
import sonicwaves.android.iot_app.firebase.objects.table.TableDetection;


public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";
    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public FirebaseUtils(Context context) {


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(context.getResources().getString(R.string.firebase_application_id)) // Required for Analytics.
                .setApiKey(context.getResources().getString(R.string.firebase_api_key)) // Required for Auth.
                .setProjectId(context.getResources().getString(R.string.firebase_project_id))
                .build();
        FirebaseApp.initializeApp(context /* Context */, options, "secondary");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    /**
     * Calls the upload methods to firebase if the values are not null for the respective data type
     *
     * @param firebaseHolder the info to be uploaded to firebase
     * @return value indicating whether it is uploading
     */
    public boolean uploadToFirebase(FirebaseHolder firebaseHolder) {

        boolean isUploading = false;

        if (firebaseHolder.getChairList() != null) {
            updateChairData(firebaseHolder.getChairList());
            isUploading = true;
        }

        if (firebaseHolder.getDoorList() != null) {
            updateDoorData(firebaseHolder.getDoorList());
            isUploading = true;
        }

        if (firebaseHolder.getTableList() != null) {
            updateTableData(firebaseHolder.getTableList());
            isUploading = true;
        }

        return isUploading;
    }


    /**
     * Method updates the chair data on firebase
     *
     * @param chairs list of chairs
     */
    private void updateChairData(List<Chair> chairs) {

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

    /**
     * Method updates the table data on firebase
     *
     * @param tables list of chairs
     */
    private void updateTableData(List<Table> tables) {

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

    /**
     * Method updates the door data on firebase
     *
     * @param doors list of doors
     */
    private void updateDoorData(List<Door> doors) {


        // Add a new document with a generated ID
        CollectionReference doorRef = db.collection(DOOR_DATA);

        for (Door door : doors) {
            Map<String, DoorDetection> doorSensors = door.getDoorSensors();

            for (Map.Entry<String, DoorDetection> sensor : doorSensors.entrySet()) {

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
}
