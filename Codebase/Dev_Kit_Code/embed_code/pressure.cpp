// #include <events/mbed_events.h>
// #include "mbed.h"
// #include "ble/BLE.h"
// #include "ble/Gap.h"
// #include "ble/services/HeartRateService.h"
// #include "bluetooth.h"
#include "FSR.h"
#include "pressure.h"
// To connect use screen /dev/ttyACM0 9600
#define Serial pc(USBTX, USBRX);

FSR fsr(p2, 8.4);
DigitalOut led1(LED1, 1);
DigitalOut led2(LED2, 1);
DigitalOut led3(LED3, 1);
DigitalOut led4(LED4, 1);
DigitalIn alarm(p28, PullDown); //internal pull up
const int READINGS_UNTIL_TOGGLE = 5;

// int main() {
//     wait(2); //Wait for sensor to take snap shot of still room

//     // create BLE instance
//     BLE &ble = BLE::Instance();
//     // link to events processing handler
//     ble.onEventsToProcess(scheduleBleEventsProcessing);
//     // initialise the BLE device
//     ble.init(bleInitComplete);

//     int successive_readings = 0;
//     bool occupied = 0;
//     while(1) {
//         // if (!alarm){
//         //     led1=1;
//         //     pirTriggered(true);
//         //     wait(2);
//         // }
//         // else {
//         //     led1=0;
//         //     pirTriggered(false);
//         // }

        
//         bool is_occupied = readPressure(READINGS_UNTIL_TOGGLE, &occupied, &successive_readings);
//         printf("Chair Occupied: %d\r\n", is_occupied);
//         wait(2);
//         eventQueue.dispatch(10);
//     }
// }

bool readPressure(const int num_readings_until_toggle, bool* chair_occupied, int* successive_readings) {
    float rawVal = fsr.readRaw();
    printf("Raw Val: %f\r\n", rawVal);
    if (*chair_occupied){
        if (rawVal < 0.95) {
            (*successive_readings)++;
        }
        else {
            *successive_readings = 0;
        }
        if (*successive_readings >= num_readings_until_toggle) {
            *chair_occupied = !(*chair_occupied);
            *successive_readings = 0;
        } 
    }
    else {
        if (rawVal > 0.95) {
            (*successive_readings)++;
        }
        else {
            *successive_readings = 0;
        }
        if (*successive_readings >= num_readings_until_toggle) {
            *chair_occupied = !(*chair_occupied);
            *successive_readings = 0;
        } 
    }

    return *chair_occupied;
}
