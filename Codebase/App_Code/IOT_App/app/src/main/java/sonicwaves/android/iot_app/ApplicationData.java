package sonicwaves.android.iot_app;
import android.app.Application;
import android.content.Context;

import java.util.List;

import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.firebase.FirebaseUtils;
import sonicwaves.android.iot_app.firebase.objects.FirebaseHolder;


public class ApplicationData extends Application {
    private List<DiscoveredBluetoothDevice> selectedDevices;
    private FirebaseUtils firebaseUtils;
    private FirebaseHolder firebaseHolder;

    public List<DiscoveredBluetoothDevice> getDevices() {
        return selectedDevices;
    }

    public void setDevices(List<DiscoveredBluetoothDevice> devices) {
        this.selectedDevices = devices;
    }

    public FirebaseUtils getFirebaseUtils(Context context) {

        if (firebaseUtils == null) {
            firebaseUtils = new FirebaseUtils(context);
        }
        return firebaseUtils;
    }

    public void setFirebaseHolder(FirebaseHolder firebaseHolder) {
        this.firebaseHolder = firebaseHolder;
    }

    public FirebaseHolder getFirebaseHolder() {
        return firebaseHolder;
    }
}
