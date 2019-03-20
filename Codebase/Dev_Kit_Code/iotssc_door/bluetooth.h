#ifndef BLUETOOTH_H
#define BLUETOOTH_H
#include "ble/BLE.h"

class DistanceService {
    public:
        const static uint16_t customServiceUUID  = 0xA000;
        const static uint16_t readMovementUUID  = 0xA002;
    
        DistanceService();
        
        DistanceService(BLE &_ble, uint8_t movement=0) : 
            ble(_ble), readMovement(readMovementUUID, &movement, GattCharacteristic::BLE_GATT_CHAR_PROPERTIES_NOTIFY)
        {
            GattCharacteristic *characteristics[] = {&readMovement};
            GattService distanceService(readMovementUUID, characteristics, sizeof(characteristics) / sizeof(GattCharacteristic *));
            ble.addService(distanceService);   
        }
        
        void updateDistanceState(uint8_t* movement) {
            ble.gattServer().write(readMovement.getValueHandle(), movement, sizeof(uint8_t));    
        }

        
    private:
        BLE &ble;
        ReadOnlyGattCharacteristic<uint8_t> readMovement;
};

extern char DEVICE_NAME[];

void onBleInitError(BLE&, ble_error_t);

void bleInitComplete(BLE::InitializationCompleteCallbackContext*);

void updateCharacteristic(uint8_t* readingsArr, uint8_t length);

#endif