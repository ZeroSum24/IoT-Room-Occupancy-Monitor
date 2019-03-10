#include <string>
#include "mbed.h"
#include "ble/BLE.h"
#include <utility>
#include "bluetooth.h"
#include "distance.h"


#define Serial pc(USBTX, USBRX);

Ticker distanceInterrupt;
Ticker scanInterrupt;

void scan() 
{
    scanForDevices(BLE::Instance(BLE::DEFAULT_INSTANCE));
}

void readDistance() 
{
    writeDistanceMeasurements(get_distances());
}

int main()
{
    /* initialize stuff */
    printf("\r\n********* Starting Main Loop *********\r\n");
    BLE& ble = BLE::Instance(BLE::DEFAULT_INSTANCE);
    ble_error_t error = ble.init(bleInitComplete);
    
    initialize_distance_sensors();
    
    
    /* SpinWait for initialization to complete. This is necessary because the
     * BLE object is used in the main loop below. */
    while (ble.hasInitialized()  == false) { 
        printf("BLE Initializing!\r\n");
        wait(2);
    }

    printf("Initialization done!\r\n");
 
//    distanceInterrupt.attach(readDistance, 0.5);
    scanInterrupt.attach(scan, 5);
    /* Infinite loop waiting for BLE interrupt events */
    while (true) {
        ble.waitForEvent(); /* Save power */
    }
}
 
