#include <events/mbed_events.h>
#include <mbed.h>
#include "ble/BLE.h"
#include "ble/Gap.h"
#include "ble/services/HeartRateService.h"
#include "bluetooth.h"

char DEVICE_NAME[] = "sonicone";
EventQueue eventQueue(/* event count */ 16 * EVENTS_EVENT_SIZE);
uint16_t customServiceUUID  = 0xA000;
uint16_t readPIRUUID       = 0xA001;
static const uint16_t uuid16_list[]        = {0xFFFF};

// Set Up custom Characteristics
static uint8_t readValue[10] = {0};
ReadOnlyArrayGattCharacteristic<uint8_t, sizeof(readValue)> readPIR(
  readPIRUUID, readValue
);

GattCharacteristic *characteristics[] = {&readPIR};
GattService        customService(customServiceUUID, characteristics, sizeof(characteristics) / sizeof(GattCharacteristic *));

void disconnectionCallback(const Gap::DisconnectionCallbackParams_t *)
{
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().startAdvertising();
}

void pirTriggered(bool triggered)
{
  if (triggered)
  {
    uint8_t new_value[10] = {1};
    BLE::Instance(BLE::DEFAULT_INSTANCE).gattServer().write(
      readPIR.getValueHandle(), new_value, sizeof(new_value)
    );
    return;
  }
  uint8_t new_value[10] = {0};
  BLE::Instance(BLE::DEFAULT_INSTANCE).gattServer().write(
    readPIR.getValueHandle(), new_value, sizeof(new_value)
  );

}

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

    ble.gap().onDisconnection(disconnectionCallback);

    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LIST_16BIT_SERVICE_IDS, (uint8_t *)uuid16_list, sizeof(uuid16_list)); // UUID's broadcast in advertising packet
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME));
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED);
    ble.gap().setAdvertisingInterval(1000);

    // Add custom service
    ble.addService(customService);

    ble.gap().startAdvertising();
}

// events processing handler
void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context)
{
    eventQueue.call(callback(&(context->ble), &BLE::processEvents));
}
