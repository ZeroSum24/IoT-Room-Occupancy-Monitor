package sonicwaves.android.iot_app.viewmodels;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;

public class SequentialViewModel {

    private final static String TAG = "SViewModel";
    private BlinkyViewModel mViewModel;

    public boolean iterateThroughDevices(Activity activity, List<DiscoveredBluetoothDevice> dBDeviceList) {

        for (DiscoveredBluetoothDevice device: dBDeviceList) {

            DeviceClass deviceClass = new DeviceClass(device);
            mViewModel = new BlinkyViewModel(activity.getApplication(), deviceClass);
            mViewModel.connect(device);

            Log.d(TAG, device.getName() + " " + deviceClass);

            // do something here
            mViewModel.disconnect();
        }
        return true;
    }

}
