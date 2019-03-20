#ifndef BLUETOOTH_H
#define BLUETOOTH_H
#include <vector>
#include "ble/BLE.h"
#include <string>

class ScanService {
    public:
        const static uint16_t customServiceUUID  = 0xA000;
        const static uint16_t readScanUUID = 0xA001;

        ScanService();
        
        ScanService(BLE &_ble, vector<uint16_t> scanValues) : 
            ble(_ble), readScan(readScanUUID, &scanValues[0], GattCharacteristic::BLE_GATT_CHAR_PROPERTIES_NOTIFY)
        {
            GattCharacteristic *characteristics[] = {&readScan};
            GattService scanService(customServiceUUID, characteristics, sizeof(characteristics) / sizeof(GattCharacteristic *));
            ble.addService(scanService);   
        }
        
        void updateScan(uint16_t* dataP)
        {
            printf("\r\n UPDATING CHARACTERISTIC \r\n");
            ble.gattServer().write(readScan.getValueHandle(), (uint8_t *) dataP, sizeof(uint16_t) * 3);    
        }
            
    private:
        BLE &ble;
        ReadOnlyArrayGattCharacteristic<uint16_t, 3> readScan;
};

extern char DEVICE_NAME[];

void onBleInitError(BLE&, ble_error_t);

void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context);

void bleInitComplete(BLE::InitializationCompleteCallbackContext*);

string printDevice(const uint8_t* int_p);

void scanCallback(const Gap::AdvertisementCallbackParams_t *params);

void updateCharacteristic(uint16_t* readingsArr, uint8_t length);

void doneScanning();

#endif