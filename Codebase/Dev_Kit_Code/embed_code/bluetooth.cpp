#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include <string>
#include "bluetooth.h"

// SonicWaves-[C|T|D]-001

char DEVICE_NAME[] = "SonicWaves-T-001";
string safe_device_name("SonicWaves-T-001\0");
const uint8_t DEVICE_NAME_LENGTH = 16;
uint16_t customServiceUUID  = 0xA000;
uint16_t readPIRUUID        = 0xA001;
uint16_t readDistance1UUID  = 0xA002;
uint16_t readDistance2UUID  = 0xA003;
static const uint16_t uuid16_list[]        = {0xFFFF};

// Set Up custom Characteristics
static uint16_t readPIRValue[10] = {0};
ReadOnlyArrayGattCharacteristic<uint16_t, sizeof(readPIRValue)> readPIR(
  readPIRUUID, readPIRValue
);

static uint16_t distance1Value[10] = {0};
ReadOnlyArrayGattCharacteristic<uint16_t, sizeof(distance1Value)> readDist1(
  readDistance1UUID, distance1Value
);
static uint16_t distance2Value[10] = {0};
ReadOnlyArrayGattCharacteristic<uint16_t, sizeof(distance2Value)> readDist2(
  readDistance2UUID, distance2Value
);


GattCharacteristic *characteristics[] = {&readPIR, &readDist1, &readDist2};
GattService        customService(customServiceUUID, characteristics, sizeof(characteristics) / sizeof(GattCharacteristic *));

const int THRESHOLD_DETECT = 500;
/*
 *  Restart advertising when phone app disconnects
*/
void disconnectionCallback(const Gap::DisconnectionCallbackParams_t *)
{
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().startAdvertising();
}

void writeDistanceMeasurements(std::pair<int, int> measurements)
{

    uint8_t distance1Tripped = 0;
    uint8_t distance2Tripped = 0;

    if (measurements.first < THRESHOLD_DETECT && measurements.first != 0)
    {
        distance1Tripped = 1;
    }
    if (measurements.second < THRESHOLD_DETECT && measurements.second != 0)
    {
        distance2Tripped = 1;
    }
    printf("\r\nDistance in gatt writer: %d and %d\r\n", measurements.first, measurements.second);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gattServer().write(readDist1.getValueHandle(), &distance1Tripped, 1);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gattServer().write(readDist2.getValueHandle(), &distance2Tripped, 1);
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
    //ble.gattServer().onDataWritten(writeCharCallback);

    /* Setup advertising */
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::BREDR_NOT_SUPPORTED | GapAdvertisingData::LE_GENERAL_DISCOVERABLE); // BLE only, no classic BT
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED); // advertising type
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME)); // add name
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LIST_16BIT_SERVICE_IDS, (uint8_t *)uuid16_list, sizeof(uuid16_list)); // UUID's broadcast in advertising packet
    ble.gap().setAdvertisingInterval(100); // 100ms.

    /* Add our custom service */
    ble.addService(customService);

    /* Start advertising */
    ble.gap().startAdvertising();
}

/*
 * Callback issued whenever a device is discovered
 */
// Byte 5 is where name of device starts
void deviceDiscovered(const Gap::AdvertisementCallbackParams_t *params) {   
    const uint8_t* address_of_device = (params->advertisingData);
    string name_found = "";
    if (*(address_of_device + 5) == 83 && *(address_of_device + 6) == 111)
    {
        name_found = printDevice(address_of_device + 5);
    } 
    
    
    if (name_found.find("SonicWaves-C") != string::npos && name_found != safe_device_name)
    {
        printf("\r\nDevice Name: %s\r\n", name_found.c_str());
        int8_t relative_signal_strength = params->rssi;
        printf("\r\nGot device with signal strength: %d\r\n", relative_signal_strength);
    }
}

string printDevice(const uint8_t* int_p)
{
    char* first_chars = new char[DEVICE_NAME_LENGTH + 1];  
    first_chars = (char *)int_p;
    first_chars[DEVICE_NAME_LENGTH] = '\0';
    string name(first_chars);
    printf("\r\n String Recovered: %s \r\n", first_chars); 
    return name;
}

void scanForDevices(BLE &bledevice) 
{
    bledevice.setScanParams(200, 200, 1);
    bledevice.startScan(deviceDiscovered);
}
