#include <events/mbed_events.h>
#include <mbed.h>
#include "ble/BLE.h"
#include "ble/Gap.h"
#include "ble/services/HeartRateService.h"
#include "bluetooth.h"

DigitalOut led1(LED1);
//DigitalOut led2(LED2);
DigitalIn alarm1(p29, PullNone); //internal pull up
//DigitalIn alarm2(p28, PullNone);

void checkAlarm(DigitalIn& alarm, DigitalOut& led_to_trigger)
{
    if (!alarm){
        led_to_trigger=1;
        wait(2);
    }
    else
        led_to_trigger=0;
}


int main() {
    wait(2); //Wait for sensor to take snap shot of still room

    // create BLE instance
    BLE &ble = BLE::Instance();
    // link to events processing handler
    //ble.onEventsToProcess(scheduleBleEventsProcessing);
    // initialise the BLE device
    //ble.init(bleInitComplete);

    //eventQueue.dispatch_forever();

    //while(1) {
        //checkAlarm(alarm1, led1);
    //}
}
