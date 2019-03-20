#include <events/mbed_events.h>
#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include <vector>
#include <string>
#include "bluetooth.h"
#include "main.h"

// SonicWaves-[C|T|D]-001

PressureService* pressureService;
char DEVICE_NAME[] = "SonicWaves-C-010";
string safe_device_name("SonicWaves-C-010\0");
const uint8_t DEVICE_NAME_LENGTH = 16;


static const uint16_t uuid16_list[]        = {0xFFFF};


const int THRESHOLD_DETECT = 500;
/*
 *  Restart advertising when phone app disconnects
*/
void disconnectionCallback(const Gap::DisconnectionCallbackParams_t *)
{
    setConnected(false);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().startAdvertising();
}

void connectionCallback(const Gap::ConnectionCallbackParams_t *)
{
    setConnected(true);
}

void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context) {
    BLE &ble = BLE::Instance();
    eventQueue.call(Callback<void()>(&ble, &BLE::processEvents));
}

/*
 * Initialization callback
 */
void bleInitComplete(BLE::InitializationCompleteCallbackContext *params)
{
    BLE &ble          = params->ble;
    ble_error_t error = params->error;
    
    if (error != BLE_ERROR_NONE) {
        return;
    }

    ble.gap().onDisconnection(disconnectionCallback);
    ble.gap().onConnection(connectionCallback);
    //ble.gattServer().onDataWritten(writeCharCallback);

    vector<uint16_t> pressureArr(2);
    pressureService = new PressureService(ble, pressureArr);
    /* Setup advertising */
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::BREDR_NOT_SUPPORTED | GapAdvertisingData::LE_GENERAL_DISCOVERABLE); // BLE only, no classic BT
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED); // advertising type
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME)); // add name
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LIST_16BIT_SERVICE_IDS, (uint8_t *)uuid16_list, sizeof(uuid16_list)); // UUID's broadcast in advertising packet
    ble.gap().setAdvertisingInterval(100); // 100ms.

    /* Start advertising */
    ble.gap().startAdvertising();
}

void updateCharacteristic(uint16_t* readingsArr, uint8_t length)
{
    uint16_t measurements[2] = {0};
    for (uint8_t i = 0; i < length; i+=2) 
    {
        measurements[0] = *(readingsArr + i);
        printf("\r\n Measurement Timestamp: %d \r\n", measurements[0]);
        measurements[1] = *(readingsArr + i + 1);  
        printf("\r\n Measurement Data: %d \r\n", measurements[1]);
        pressureService->updatePressure(measurements);     
    }   
    wait(0.01);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().disconnect(Gap::REMOTE_USER_TERMINATED_CONNECTION);
}