#ifndef BLUETOOTH_H
#define BLUETOOTH_H
#include "ble/BLE.h"
#include <string>
#include <vector>

class PressureService {
    public:
        const static uint16_t customServiceUUID  = 0xA000;
        const static uint16_t readPressureUUID = 0xA004;

        PressureService();
        
        PressureService(BLE &_ble, vector<uint16_t> pressureValues) : 
            ble(_ble), readPressure(readPressureUUID, &pressureValues[0], GattCharacteristic::BLE_GATT_CHAR_PROPERTIES_NOTIFY)
        {
            GattCharacteristic *characteristics[] = {&readPressure};
            GattService pressureService(customServiceUUID, characteristics, sizeof(characteristics) / sizeof(GattCharacteristic *));
            ble.addService(pressureService);   
        }
        
        void updatePressure(uint16_t* dataP)
        {
            printf("\r\n UPDATING CHARACTERISTIC \r\n");
            ble.gattServer().write(readPressure.getValueHandle(), (uint8_t *) dataP, sizeof(uint16_t) * 2);    
        }
            
    private:
        BLE &ble;
        ReadOnlyArrayGattCharacteristic<uint16_t, 2> readPressure;
};

extern char DEVICE_NAME[];

void onBleInitError(BLE&, ble_error_t);

void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context);

void bleInitComplete(BLE::InitializationCompleteCallbackContext*);

void updateCharacteristic(uint16_t* readingsArr, uint8_t length);

#endif