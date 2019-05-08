#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include <string>
#include "bluetooth.h"
#include "main.h"
#include "time.h"

// SonicWaves-[C|T|D]-001
ScanService* scanService;
char DEVICE_NAME[] = "SonicWaves-T-001";
static const uint8_t DEVICE_NAME_LENGTH = 16;
static uint8_t chair_ids[10] = {0};
static const uint16_t uuid16_list[] = {0xFFFF};


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

bool alreadyScanned(uint8_t chair_id)
{
    for(int i = 0; i < 10; i++)
    {
        if (chair_ids[i] == 0)
            return false;
        if (chair_ids[i] == chair_id)
            return true;  
    }
    return false;
}

void addToScanned(uint8_t chair_id)
{
    for(int i = 0; i < 10; i++)
    {
        if (chair_ids[i] == 0)
        {
            chair_ids[i] = chair_id;
            return;
        }
    }
    printf("\r\nCouldn't add to buffer, more than 10 chairs detected.\r\n");    
}

void doneScanning()
{
    memset(chair_ids, 0, sizeof(chair_ids));    
}

void scanCallback(const Gap::AdvertisementCallbackParams_t *params) {   
    const uint8_t* data_pointer = params->advertisingData;
    uint8_t chair_id = 0;
    string name_found = "";
    if (*(data_pointer + 5) == 83 && *(data_pointer + 6) == 111)
    {
        chair_id = ((*(data_pointer + 18) - 48) * 100) + ((*(data_pointer + 19) - 48) * 10) + (*(data_pointer + 20) - 48);
        name_found = printDevice(data_pointer + 5);
    } 
    
    if (name_found.find("SonicWaves-C") != string::npos && !alreadyScanned(chair_id))
    {
        printf("\r\nDevice Name: %s\r\n", name_found.c_str());
        uint8_t relative_signal_strength = params->rssi * -1;
        printf("\r\nGot device with signal strength: %d\r\n", relative_signal_strength);
        addToScanned(chair_id);
        addReading(getTime(), chair_id, relative_signal_strength);
    }
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
 
    vector<uint16_t> scanningArr(3);
    scanService = new ScanService(ble, scanningArr);
    
    /* Setup advertising */
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::BREDR_NOT_SUPPORTED | GapAdvertisingData::LE_GENERAL_DISCOVERABLE); // BLE only, no classic BT
    ble.gap().setAdvertisingType(GapAdvertisingParams::ADV_CONNECTABLE_UNDIRECTED); // advertising type
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LOCAL_NAME, (uint8_t *)DEVICE_NAME, sizeof(DEVICE_NAME)); // add name
    ble.gap().accumulateAdvertisingPayload(GapAdvertisingData::COMPLETE_LIST_16BIT_SERVICE_IDS, (uint8_t *)uuid16_list, sizeof(uuid16_list)); // UUID's broadcast in advertising packet
    ble.gap().setAdvertisingInterval(100); // 100ms.

    /* Start advertising */
    ble.gap().startAdvertising();
}

/*
 * Callback issued whenever a device is discovered
 */
string printDevice(const uint8_t* int_p)
{
    char* first_chars = new char[DEVICE_NAME_LENGTH + 1];  
    first_chars = (char *)int_p;
    first_chars[DEVICE_NAME_LENGTH] = '\0';
    string name(first_chars);
    printf("\r\n String Recovered: %s \r\n", first_chars); 
    return name;
}


void updateCharacteristic(uint16_t* readingsArr, uint8_t length)
{
    uint16_t measurements[3] = {0};
    for (uint8_t i = 0; i < length; i+=3) 
    {
        measurements[0] = *(readingsArr + i);
        printf("\r\n Measurement Timestamp: %d \r\n", measurements[0]);
        measurements[1] = *(readingsArr + i + 1);  
        printf("\r\n Measurement Chair: %d \r\n", measurements[1]);
        measurements[2] = *(readingsArr + i + 2);
        printf("\r\n Measurement RSSI: %d \r\n", measurements[2]);  
        scanService->updateScan(measurements);     
    }   
    wait(0.01);
    BLE::Instance(BLE::DEFAULT_INSTANCE).gap().disconnect(Gap::REMOTE_USER_TERMINATED_CONNECTION);
}
