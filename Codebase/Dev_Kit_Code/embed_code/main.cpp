#include <events/mbed_events.h>
#include <mbed.h>
#include "ble/BLE.h"
#include "ble/Gap.h"
#include "ble/services/HeartRateService.h"
#include "bluetooth.h"

DigitalOut led1(LED1);
DigitalIn alarm(p28, PullDown); //internal pull up

int main() {
    wait(2); //Wait for sensor to take snap shot of still room

    // create BLE instance
    BLE &ble = BLE::Instance();
    // link to events processing handler
    ble.onEventsToProcess(scheduleBleEventsProcessing);
    // initialise the BLE device
    ble.init(bleInitComplete);

    while(1) {
       if (!alarm){
           led1=1;
           pirTriggered(true);
           wait(2);
       }
       else {
           led1=0;
           pirTriggered(false);
       }
       eventQueue.dispatch(10);
    }
}
