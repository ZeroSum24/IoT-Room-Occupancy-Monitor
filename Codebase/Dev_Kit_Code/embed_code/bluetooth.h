#ifndef BLUETOOTH_H
#define BLUETOOTH_H

extern char DEVICE_NAME[];

void onBleInitError(BLE&, ble_error_t);

void bleInitComplete(BLE::InitializationCompleteCallbackContext*);

void pirTriggered(bool triggered);

void writeDistanceMeasurements(std::pair<int, int> measurements);

void scanForDevices(BLE &bledevice); 

string printDevice(const uint8_t* int_p);

#endif