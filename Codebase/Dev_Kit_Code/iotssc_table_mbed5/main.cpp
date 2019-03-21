#include <string>
#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include "bluetooth.h"
#include "time.h"

#define Serial pc(USBTX, USBRX);

Ticker scanInterrupt;

static bool connected = false;
static uint16_t scanReadings[90] = {0};
static uint8_t currentReading = 0;
static uint8_t numReadings = 0;
EventQueue eventQueue(4 * EVENTS_EVENT_SIZE);



void setConnected(bool _connected)
{
    connected = _connected;
}

void addReading(const uint16_t timestamp, const uint16_t chair_id, const uint16_t signal_strength)
{   
    printf("\r\nAdding Reading: TIME: %d...CHAIR: %d...RSSI: %d...\r\n", timestamp, chair_id, signal_strength);
    if (numReadings == 0)
        currentReading = 0;
    else if (currentReading == 90)
        currentReading = 0;
    scanReadings[currentReading] = timestamp;
    scanReadings[currentReading+1] = chair_id;
    scanReadings[currentReading+2] = signal_strength;
    if (numReadings < 88)
        numReadings += 3;
    currentReading += 3;
}

void scan() 
{
    ble_error_t error = BLE::Instance(BLE::DEFAULT_INSTANCE).gap().startScan(scanCallback);
    if (error)
    {
        printf("\r\nr caused by Gap::startScan...%d\r\n", error);
    }
    doneScanning();
}


void sendData()
{
    if (connected && numReadings > 0)
    {
        uint16_t dataCopy[numReadings];
        for (int i = 0; i < numReadings; i+=3)
        {
            dataCopy[i] = scanReadings[i];
            dataCopy[i+1] = scanReadings[i+1]; 
            dataCopy[i+2] = scanReadings[i+2];       
        }      
        
        updateCharacteristic(dataCopy, numReadings);
        numReadings = 0;  
    }
}


void wakeUp() {}


int main()
{
    /* initialize stuff */
    printf("\r\n********* Starting Main Loop *********\r\n");
    BLE& ble = BLE::Instance(BLE::DEFAULT_INSTANCE);
    ble.onEventsToProcess(scheduleBleEventsProcessing);
    ble_error_t error = ble.init(bleInitComplete);
    ble.gap().setScanParams(200, 200, 1);
    
    eventQueue.call_every(10000, scan);
    eventQueue.call_every(2000, sendData);
    /* SpinWait for initialization to complete. This is necessary because the
     * BLE object is used in the main loop below. */
    while (ble.hasInitialized()  == false) { 
        printf("BLE Initializing!\r\n");
    }
    
    initTime();
    
    printf("Initialization done!\r\n");
 
    eventQueue.dispatch_forever(); 
}
 
