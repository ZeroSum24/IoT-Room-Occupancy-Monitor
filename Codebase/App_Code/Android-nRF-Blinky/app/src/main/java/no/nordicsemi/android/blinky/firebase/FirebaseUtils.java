package no.nordicsemi.android.blinky.firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import no.nordicsemi.android.blinky.firebase.objects.chair.Chair;
import no.nordicsemi.android.blinky.firebase.objects.chair.ChairDetection;
import no.nordicsemi.android.blinky.firebase.objects.door.DoorDetection;
import no.nordicsemi.android.blinky.firebase.objects.door.DoorSensor;
import no.nordicsemi.android.blinky.firebase.objects.table.Table;
import no.nordicsemi.android.blinky.firebase.objects.table.TableDetection;


public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";
    private static final String CHAIR_DATA = "chair_data";
    private static final String DOOR_DATA = "door_data";
    private static final String TABLE_DATA = "table_data";
    private FirebaseFirestore db;


    public FirebaseUtils() {
        db = FirebaseFirestore.getInstance();
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
