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
import sonicwaves.android.iot_app.profile.callback.DistanceDataOneCallback;
import sonicwaves.android.iot_app.profile.callback.DistanceDataTwoCallback;
import sonicwaves.android.iot_app.profile.callback.PIRDataCallback;
import sonicwaves.android.iot_app.profile.callback.PressureDataCallback;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;
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
     * PIR characteristic UUID.
     */
    private final static UUID SW_UUID_PIR_CHAR = UUID.fromString("0000a001-0000-1000-8000-00805f9b34fb");
    /**
     * DISTANCE ONE characteristic UUID.
     */
    private final static UUID SW_UUID_DISTONE_CHAR = UUID.fromString("0000a002-0000-1000-8000-00805f9b34fb");
    /**
     * DISTANCE TWO characteristic UUID.
     */
    private final static UUID SW_UUID_DISTTWO_CHAR = UUID.fromString("0000a003-0000-1000-8000-00805f9b34fb");
    /**
     * PRESSURE characteristic UUID.
     */
    private final static UUID SW_UUID_PRESSURE_CHAR = UUID.fromString("0000a004-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mPressureCharacteristic, mPIRCharacteristic, mDistanceOneCharacteristic,
                                        mDistanceTwoCharacteristic;
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
     * {@link PIRDataCallback#onPIRStateChanged(BluetoothDevice, boolean)} will be called.
     * Otherwise, the {@link PIRDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final PIRDataCallback mPIRCallback = new PIRDataCallback() {
        @Override
        public void onPIRStateChanged(@NonNull final BluetoothDevice device,
                                         final boolean detected) {
            log(LogContract.Log.Level.APPLICATION, "PIR " + (detected ? "detected" : "released"));
            mCallbacks.onPIRStateChanged(device, detected);
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
     * {@link DistanceDataOneCallback#onDistanceOneStateChanged(BluetoothDevice, boolean)} will be called.
     * Otherwise, the {@link DistanceDataOneCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final DistanceDataOneCallback mDistanceOneCallback = new DistanceDataOneCallback() {
        @Override
        public void onDistanceOneStateChanged(@NonNull final BluetoothDevice device,
                                         final boolean tripped) {
            log(LogContract.Log.Level.APPLICATION, "Distance tripped " + (tripped ? "tripped" : "static"));
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
     * {@link DistanceDataTwoCallback#onDistanceTwoStateChanged(BluetoothDevice, boolean)} will be called.
     * Otherwise, the {@link DistanceDataTwoCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final DistanceDataTwoCallback mDistanceTwoCallback = new DistanceDataTwoCallback() {
        @Override
        public void onDistanceTwoStateChanged(@NonNull final BluetoothDevice device,
                                              final boolean tripped) {
            log(LogContract.Log.Level.APPLICATION, "Distance tripped " + (tripped ? "tripped" : "static"));
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
     * {@link PressureDataCallback#onPressureStateChanged(BluetoothDevice, boolean)} will be called.
     * Otherwise, the {@link PressureDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
     * will be called with the data received.
     */
    private final PressureDataCallback mPressureCallback = new PressureDataCallback() {
        @Override
        public void onPressureStateChanged(@NonNull final BluetoothDevice device,
                                         final boolean pressed) {
            log(LogContract.Log.Level.APPLICATION, "Pressure " + (pressed ? "pressed" : "released"));
            mCallbacks.onPressureStateChanged(device, pressed);
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
                    setNotificationCallback(mPressureCharacteristic).with(mPressureCallback);
                    readCharacteristic(mPressureCharacteristic).with(mPressureCallback).enqueue();
                    enableNotifications(mPressureCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mPressureCharacteristic = service.getCharacteristic(SW_UUID_PRESSURE_CHAR);
                    }

                    mSupported = mPressureCharacteristic != null;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mPressureCharacteristic = null;
                }
            };

        } else if (deviceClass.getDeviceClass().equals(deviceClass.TABLE)) {
            // TABLE Callback

            mGattCallback = new BleManagerGattCallback() {
                @Override
                protected void initialize() {
                    setNotificationCallback(mPIRCharacteristic).with(mPIRCallback);

                    readCharacteristic(mPIRCharacteristic).with(mPIRCallback).enqueue();

                    enableNotifications(mPIRCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mPIRCharacteristic = service.getCharacteristic(SW_UUID_PIR_CHAR);
                    }

                    mSupported = mPIRCharacteristic != null;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mPIRCharacteristic = null;
                }
            };

        } else if (deviceClass.getDeviceClass().equals(deviceClass.DOOR)) {
            // DOOR Callback

            mGattCallback = new BleManagerGattCallback() {
                @Override
                protected void initialize() {
                    setNotificationCallback(mDistanceOneCharacteristic).with(mDistanceOneCallback);
                    setNotificationCallback(mDistanceTwoCharacteristic).with(mDistanceTwoCallback);

                    readCharacteristic(mDistanceOneCharacteristic).with(mDistanceOneCallback).enqueue();
                    readCharacteristic(mDistanceTwoCharacteristic).with(mDistanceTwoCallback).enqueue();

                    enableNotifications(mDistanceOneCharacteristic).enqueue();
                    enableNotifications(mDistanceTwoCharacteristic).enqueue();
                }

                @Override
                public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                    final BluetoothGattService service = gatt.getService(SW_UUID_SERVICE);
                    if (service != null) {
                        mDistanceOneCharacteristic = service.getCharacteristic(SW_UUID_DISTONE_CHAR);
                        mDistanceTwoCharacteristic = service.getCharacteristic(SW_UUID_DISTTWO_CHAR);
                    }

                    mSupported = mDistanceOneCharacteristic != null && mDistanceTwoCharacteristic != null;
                    return mSupported;
                }

                @Override
                protected void onDeviceDisconnected() {
                    mDistanceOneCharacteristic = null;
                    mDistanceTwoCharacteristic = null;
                }
            };
        }

        return mGattCallback;
    }
}
