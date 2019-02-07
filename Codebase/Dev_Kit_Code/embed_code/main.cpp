#include "mbed.h"
#include "bluetooth.h"

DigitalOut led1(LED1);
DigitalIn alarm(p29, PullNone); //internal pull up

int main() {
    wait(2); //Wait for sensor to take snap shot of still room

    // create BLE instance
    BLE &ble = BLE::Instance();
    // link to events processing handler
    ble.onEventsToProcess(scheduleBleEventsProcessing);
    // initialise the BLE device
    ble.init(bleInitComplete);

    eventQueue.dispatch_forever();

    while(1) {
        if (!alarm){
            led1=1;
            wait(2);
        }
        else
            led1=0;
    }
}
