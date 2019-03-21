/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package sonicwaves.android.iot_app.profile;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.data.Data;
import sonicwaves.android.iot_app.profile.callback.CalibratedDataCallback;
import sonicwaves.android.iot_app.profile.callback.DistanceDataOneCallback;
import sonicwaves.android.iot_app.profile.callback.DistanceDataTwoCallback;
import sonicwaves.android.iot_app.profile.callback.DeviceSignalStrengthDataCallback;
import sonicwaves.android.iot_app.profile.callback.PressureOneDataCallback;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;
import sonicwaves.android.iot_app.profile.callback.SetTimeDataCallback;
import sonicwaves.android.iot_app.viewmodels.objects.DeviceClass;

public class BlinkyManager extends BleManager<BlinkyManagerCallbacks> {

    /**
     * SonicWaves Service Branding.
     */
    public final static String SONICWAVES_UUID_START = "SonicWaves";

    /**
     * SonicWaves Service UUID.
     */
    public final static UUID SW_UUID_SERVICE = UUID.fromString("0000a000-0000-1000-8000-00805f9b34fb");
    /**
     * DEVICE SIGNAL STRENGTH characteristic UUID.
     */
    private final static UUID SW_UUID_DEVICE_SIGNAL_STRENGTH_CHAR = UUID.fromString("0000a001-0000-1000-8000-00805f9b34fb");
    /**
     * DISTANCE ONE characteristic UUID.
     */
    private final static UUID SW_UUID_DISTONE_CHAR = UUID.fromString("0000a002-0000-1000-8000-00805f9b34fb");
    /**
     * DISTANCE TWO characteristic UUID.
     */
    private final static UUID SW_UUID_DISTTWO_CHAR = UUID.fromString("0000a003-0000-1000-8000-00805f9b34fb");
    /**
     * PRESSURE ONE characteristic UUID.
     */
    private final static UUID SW_UUID_PRESSURE_CHAR = UUID.fromString("0000a004-0000-1000-8000-00805f9b34fb");
//    /**
//     * SETTIME characteristic UUID.
//     */
//    private final static UUID SW_UUID_SETTIME_CHAR = UUID.fromString("0000a008-0000-1000-8000-00805f9b34fb");
//    /**
//     * CALIBRATED characteristic UUID.
//     */
//    private final static UUID SW_UUID_CALIBRATED_CHAR = UUID.fromString("0000a009-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mPressureOneCharacteristic, mPressureTwoCharacteristic, mDeviceSignalStrengthCharacteristic, mDistanceOneCharacteristic,
            mDistanceTwoCharacteristic, mSetTimeCharacteristic, mCalibratedCharacteristic;
    private LogSession mLogSession;
    private boolean mSupported;
    private BleManagerGattCallback mGattCallback;

    public BlinkyManager(@NonNull final Context context, @NonNull final DeviceClass deviceClass) {
        super(context);

        mGattCallback = selectGattCallback(deviceClass);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return mGattCallback;
    }

    /**
     * Sets the log session to be used for low level logging.
     *
     * @param session the session, or null, if nRF Logger is not installed.
     */
    public void setLogger(@Nullable final LogSession session) {
        this.mLogSession = session;
    }

    @Override
    public void log(final int priority, @NonNull final String message) {
        // The priority is a Log.X constant, while the Logger accepts it's log levels.
        Logger.log(mLogSession, LogContract.Log.Level.fromPriority(priority), message);
    }

    @Override
    protected boolean shouldClearCacheWhenDisconnected() {
        return !mSupported;
    }

    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link DeviceSignalStrengthDataCallback#onDeviceSignalStrengthStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link DeviceSignalStrengthDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final DeviceSignalStrengthDataCallback deviceSignalStrengthCallback = new DeviceSignalStrengthDataCallback() {
        @Override
        public void onDeviceSignalStrengthStateChanged(@NonNull final BluetoothDevice device,
                                                       final String detected) {
            log(LogContract.Log.Level.APPLICATION, "PIR " + detected);
            mCallbacks.onDeviceSignalStrengthStateChanged(device, detected);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };

    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link DistanceDataOneCallback#onDistanceOneStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link DistanceDataOneCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final DistanceDataOneCallback mDistanceOneCallback = new DistanceDataOneCallback() {
        @Override
        public void onDistanceOneStateChanged(@NonNull final BluetoothDevice device,
                                              final String tripped) {
            log(LogContract.Log.Level.APPLICATION, "Distance tripped " + tripped);
            mCallbacks.onDistanceOneStateChanged(device, tripped);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };

    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link DistanceDataTwoCallback#onDistanceTwoStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link DistanceDataTwoCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final DistanceDataTwoCallback mDistanceTwoCallback = new DistanceDataTwoCallback() {
        @Override
        public void onDistanceTwoStateChanged(@NonNull final BluetoothDevice device,
                                              final String tripped) {
            log(LogContract.Log.Level.APPLICATION, "Distance tripped " + tripped);
            mCallbacks.onDistanceTwoStateChanged(device, tripped);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };

    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link PressureOneDataCallback#onPressureOneStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link PressureOneDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final PressureOneDataCallback mPressureOneCallback = new PressureOneDataCallback() {
        @Override
        public void onPressureOneStateChanged(@NonNull final BluetoothDevice device,
                                              final String pressed) {
            log(LogContract.Log.Level.APPLICATION, "Pressure " + pressed);
            mCallbacks.onPressureOneStateChanged(device, pressed);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };


//    /**
//     * The Button callback will be notified when a notification from Button characteristic
//     * has been received, or its data was read.
//     * <p>
//     * If the data received are valid (single byte equal to 0x00 or 0x01), the
//     * {@link PressureTwoDataCallback#onPressureTwoStateChanged(BluetoothDevice, String)} will be called.
//     * Otherwise, the {@link PressureTwoDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
//     * will be called with the data received.
//     */
//    private final PressureTwoDataCallback mPressureTwoCallback = new PressureTwoDataCallback() {
//        @Override
//        public void onPressureTwoStateChanged(@NonNull final BluetoothDevice device,
//                                              final String pressed) {
//            log(LogContract.Log.Level.APPLICATION, "Pressure " + pressed);
//            mCallbacks.onPressureOneStateChanged(device, pressed);
//        }
//
//        @Override
//        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
//                                          @NonNull final Data data) {
//            log(Log.WARN, "Invalid data received: " + data);
//        }
//    };

    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link SetTimeDataCallback#onSetTimeStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link SetTimeDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final SetTimeDataCallback mSetTimeCallback = new SetTimeDataCallback() {
        @Override
        public void onSetTimeStateChanged(@NonNull final BluetoothDevice device,
                                          final String pressed) {
            log(LogContract.Log.Level.APPLICATION, "Pressure " + pressed);
            mCallbacks.onSetTimeStateChanged(device, pressed);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };
    /**
     * The Button callback will be notified when a notification from Button characteristic
     * has been received, or its data was read.
     * <p>
     * If the data received are valid (single byte equal to 0x00 or 0x01), the
     * {@link CalibratedDataCallback#onCalibratedStateChanged(BluetoothDevice, String)} will be called.
     * Otherwise, the {@link CalibratedDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final CalibratedDataCallback mCalibratedCallback = new CalibratedDataCallback() {
        @Override
        public void onCalibratedStateChanged(@NonNull final BluetoothDevice device,
                                             final String pressed) {
            log(LogContract.Log.Level.APPLICATION, "Pressure " + pressed);
            mCallbacks.onCalibratedStateChanged(device, pressed);
        }

        @Override
        public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
                                          @NonNull final Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }
    };


    /***
     * Method selects the appropriate Gatt Callback based on the class of the device
     *
     * @param deviceClass indicates the device class
     * @return the appropriate gatt callback
     */
    private BleManagerGattCallback selectGattCallback(@NonNull DeviceClass deviceClass) {

        BleManagerGattCallback mGattCallback = null;

        if (deviceClass.getDeviceClass().equals(deviceClass.CHAIR)) {
            // CHAIR Callback

            mGattCallback = new BleManagerGattCallback() {
                @Override
                protected void initialize() {
                    setNotificationCallback(mPressureOneCharacteristic).with(mPressureOneCallback);
                    readCharacteristic(mPressureOneCharacteristic).with(mPressureOneCallback).enqueue();
                    enableNotifications(mPressureOneCharacteristic).enqueue();

//                    setNotificationCallback(mPressureTwoCharacteristic).with(mPressureTwoCallback);
//                    readCharacteristic(mPressureTwoCharacteristic).with(mPressureTwoCallback).enqueue();
//                    enableNotifications(mPressureTwoCharacteristic).enqueue();

                    setNotificationCallback(mSetTimeCharacteristic).with(mSetTimeCallback);
                    readCharacteristic(mSetTimeCharacteristic).with(mSetTimeCallback).enqueue();
                    enableNotifications(mSetTimeCharacteristic).enqueue();

                    setNotificationCallback(mCalibratedCharacteristic).with(mCalibratedCallback);
                    readCharacteristic(mCalibratedCharacteristic).with(mCalibratedCallback).enqueue();
                    enableNotifications(mCalibratedCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mPressureOneCharacteristic = service.getCharacteristic(SW_UUID_PRESSURE_CHAR);
//                        mPressureTwoCharacteristic = service.getCharacteristic(SW_UUID_PRESSURETWO_CHAR);
//                        mSetTimeCharacteristic = service.getCharacteristic(SW_UUID_SETTIME_CHAR);
//                        mCalibratedCharacteristic = service.getCharacteristic(SW_UUID_CALIBRATED_CHAR);
                    }

//                    mSupported = mPressureOneCharacteristic != null;
                    mSupported = true;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mPressureOneCharacteristic = null;
//                    mPressureTwoCharacteristic = null;
//                    mSetTimeCharacteristic = null;
//                    mCalibratedCharacteristic = null;
                }
            };

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            // TABLE Callback
            Log.e("Reading", "table gatt callback");

            mGattCallback = new BleManagerGattCallback() {
                @Override
                protected void initialize() {
                    setNotificationCallback(mDeviceSignalStrengthCharacteristic).with(deviceSignalStrengthCallback);
                    readCharacteristic(mDeviceSignalStrengthCharacteristic).with(deviceSignalStrengthCallback).enqueue();
                    enableNotifications(mDeviceSignalStrengthCharacteristic).enqueue();

//                    setNotificationCallback(mSetTimeCharacteristic).with(mSetTimeCallback);
//                    readCharacteristic(mSetTimeCharacteristic).with(mSetTimeCallback).enqueue();
//                    enableNotifications(mSetTimeCharacteristic).enqueue();
//
//                    setNotificationCallback(mCalibratedCharacteristic).with(mCalibratedCallback);
//                    readCharacteristic(mCalibratedCharacteristic).with(mCalibratedCallback).enqueue();
//                    enableNotifications(mCalibratedCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mDeviceSignalStrengthCharacteristic = service.getCharacteristic(SW_UUID_DEVICE_SIGNAL_STRENGTH_CHAR);
//                        mSetTimeCharacteristic = service.getCharacteristic(SW_UUID_SETTIME_CHAR);
//                        mCalibratedCharacteristic = service.getCharacteristic(SW_UUID_CALIBRATED_CHAR);
                    }

//                    mSupported = mDeviceSignalStrengthCharacteristic != null;
                    mSupported = true;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mDeviceSignalStrengthCharacteristic = null;
//                    mSetTimeCharacteristic = null;
//                    mCalibratedCharacteristic = null;
                }
            };

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            // DOOR Callback

            mGattCallback = new BleManagerGattCallback() {
                @Override
                protected void initialize() {
                    setNotificationCallback(mDistanceOneCharacteristic).with(mDistanceOneCallback);
                    readCharacteristic(mDistanceOneCharacteristic).with(mDistanceOneCallback).enqueue();
                    enableNotifications(mDistanceOneCharacteristic).enqueue();

//                    setNotificationCallback(mDistanceTwoCharacteristic).with(mDistanceTwoCallback);
//                    readCharacteristic(mDistanceTwoCharacteristic).with(mDistanceTwoCallback).enqueue();
//                    enableNotifications(mDistanceTwoCharacteristic).enqueue();

//                    setNotificationCallback(mSetTimeCharacteristic).with(mSetTimeCallback);
//                    readCharacteristic(mSetTimeCharacteristic).with(mSetTimeCallback).enqueue();
//                    enableNotifications(mSetTimeCharacteristic).enqueue();
//
//                    setNotificationCallback(mCalibratedCharacteristic).with(mCalibratedCallback);
//                    readCharacteristic(mCalibratedCharacteristic).with(mCalibratedCallback).enqueue();
//                    enableNotifications(mCalibratedCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mDistanceOneCharacteristic = service.getCharacteristic(SW_UUID_DISTONE_CHAR);
//                        mDistanceTwoCharacteristic = service.getCharacteristic(SW_UUID_DISTTWO_CHAR);
//                        mSetTimeCharacteristic = service.getCharacteristic(SW_UUID_SETTIME_CHAR);
//                        mCalibratedCharacteristic = service.getCharacteristic(SW_UUID_CALIBRATED_CHAR);
                    }

//                    mSupported = mDistanceOneCharacteristic != null && mDistanceTwoCharacteristic != null;
                    mSupported = true;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mDistanceOneCharacteristic = null;
//                    mDistanceTwoCharacteristic = null;
//                    mSetTimeCharacteristic = null;
//                    mCalibratedCharacteristic = null;
                }
            };
        }

        return mGattCallback;
    }
}
