#include <string>
#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include <queue>
#include "time.h"
#include "bluetooth.h"
#include "pressure.h"

#define Serial pc(USBTX, USBRX);

const int READINGS_UNTIL_TOGGLE = 5;
static bool connected = false;
static bool lastPressureReading = false;
static uint16_t pressureReadings[50] = {0};
static uint8_t currentReading = 0;
static uint8_t numReadings = 0;
EventQueue eventQueue(4 * EVENTS_EVENT_SIZE);

void setConnected(bool _connected)
{
    connected = _connected;
}

void addReading(const uint16_t timestamp, const uint16_t data)
{   
    if (numReadings == 0)
        currentReading = 0;
    else if (currentReading == 50)
        currentReading = 0;
    pressureReadings[currentReading] = timestamp;
    pressureReadings[currentReading+1] = data;
    if (numReadings < 49)
        numReadings += 2;
    currentReading += 2;
}
  

void writePressureReading(bool butt_occupied, bool back_occupied)
{
    uint16_t occupied = butt_occupied && back_occupied;
    if (occupied != lastPressureReading)
    {
        lastPressureReading = occupied;
        uint16_t current_time = getTime();
        addReading(current_time, occupied);
    }    
}

void getPressureReading(bool* occupied_butt, int* successive_readings_butt, bool* occupied_back, int* successive_readings_back)
{
    bool is_butt_occupied = readPressure(READINGS_UNTIL_TOGGLE, occupied_butt, successive_readings_butt, 0);
    printf("\r\nPressure Reading Butt: %d\r\n", is_butt_occupied);
    bool is_back_occupied = readPressure(READINGS_UNTIL_TOGGLE, occupied_back, successive_readings_back, 1);
    printf("\r\nPressure Reading Back: %d\r\n", is_back_occupied);
    writePressureReading(is_butt_occupied, is_back_occupied);
}


void sendData()
{
    if (connected && numReadings > 0)
    {
        uint16_t dataCopy[numReadings];
        for (int i = 0; i < numReadings; i+=2)
        {
            dataCopy[i] = pressureReadings[i];
            dataCopy[i+1] = pressureReadings[i+1];    
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
    
    
    int successive_readings_butt = 0;
    bool occupied_butt = 0;
    int successive_readings_back = 0;
    bool occupied_back = 0;
    
    eventQueue.call_every(500, getPressureReading, &occupied_butt, &successive_readings_butt, &occupied_back, &successive_readings_back);
    eventQueue.call_every(2000, sendData);
    /* SpinWait for initialization to complete. This is necessary because the
     * BLE object is used in the main loop below. */
    while (ble.hasInitialized()  == false) { 
        printf("BLE Initializing!\r\n");
    }
    
    initTime();
    
    printf("Initialization done!\r\n");
 
    /* Infinite loop waiting for BLE interrupt events */
    eventQueue.dispatch_forever();
    
    return 0;

}
 
