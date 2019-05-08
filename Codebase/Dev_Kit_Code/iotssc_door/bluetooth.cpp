#include "mbed.h"
#include "main.h"
#include "bluetooth.h"

DistanceService *distanceService;
const static char DEVICE_NAME[] = "SonicWaves-D-001"; // change this
static const uint16_t uuid16_list[] = {0xFFFF}; //Custom UUID, FFFF is reserved for development

void disconnectionCallback(const Gap::DisconnectionCallbackParams_t *)
{
    setConnected(false);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().startAdvertising();
}

void connectionCallback(const Gap::ConnectionCallbackParams_t *)
{
    printf("\r\n***************CONNECTED*************\r\n");
    setConnected(true);
}

void bleInitComplete(BLE::InitializationCompleteCallbackContext *params)
{
    BLE &ble          = params->ble;
    ble_error_t error = params->error;
    
    if (error != BLE_ERROR_NONE) {
        return;
    }

    ble.gap().onDisconnection(disconnectionCallback);
    ble.gap().onConnection(connectionCallback);
    

    /* Setup advertising */
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::BREDR_NOT_SUPPORTED | GapAdvertisingData::LE_GENERAL_DISCOVERABLE); // BLE only, no classic BT
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED); // advertising type
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME)); // add name
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LIST_16BIT_SERVICE_IDS, (uint8_t *)uuid16_list, sizeof(uuid16_list)); // UUID's broadcast in advertising packet
    ble.gap().setAdvertisingInterval(100); // 100ms.
    
    distanceService = new DistanceService(ble);

    /* Start advertising */
    ble.gap().startAdvertising();
}

void updateCharacteristic(uint8_t* readingsArr, uint8_t length)
{
    for (uint8_t i = 0; i < length; i++) 
    {
        distanceService->updateDistanceState((readingsArr + i));     
    }
    wait(0.01);   
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().disconnect(Gap::REMOTE_USER_TERMINATED_CONNECTION);
}