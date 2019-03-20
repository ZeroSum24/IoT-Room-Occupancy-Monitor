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
FSR fsr2(p3, 8.4);

bool readPressure(const int num_readings_until_toggle, bool* chair_occupied, int* successive_readings, bool fsr_back) {
    float rawVal = 0.0;
    if (fsr_back)
    {
        rawVal = fsr2.readRaw();
    }
    else 
    {
        rawVal = fsr.readRaw();
    }
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
