#include <events/mbed_events.h>
#include <mbed.h>
#include "ble/BLE.h"
#include "ble/Gap.h"
#include "ble/services/HeartRateService.h"
#include "bluetooth.h"

char DEVICE_NAME[] = "SONICONE";
EventQueue eventQueue(/* event count */ 16 * EVENTS_EVENT_SIZE);
uint8_t hrmCounter = 100;
HeartRateService *hrServicePtr;


void onBleInitError(BLE &ble, ble_error_t error)
{
    (void)ble;
    (void)error;
   /* Initialization error handling should go here */
   // TODO: Add error logic
}

// Initiialization routine goes here
void bleInitComplete(BLE::InitializationCompleteCallbackContext *params)
{

    BLE&        ble   = params->ble;
    ble_error_t error = params->error;

    // healthy error checking
    if (error != BLE_ERROR_NONE) {
        onBleInitError(ble, error);
        return;
    }

    if (ble.getInstanceID() != BLE::DEFAULT_INSTANCE) {
        return;
    }

    hrServicePtr = new HeartRateService(ble, hrmCounter, HeartRateService::LOCATION_FINGER);

    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::GENERIC_HEART_RATE_SENSOR);
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME));
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED);
    ble.gap().setAdvertisingInterval(1000);
    ble.gap().startAdvertising();
}

// events processing handler
void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context)
{
    eventQueue.call(callback(&(context->ble), &BLE::processEvents));
}
