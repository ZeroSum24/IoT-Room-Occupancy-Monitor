#include "mbed.h"
#include "distance.h"
#include "bluetooth.h"
#include "ble/BLE.h"
#include "VL53L0X.h"
#include <utility>

#define range1_addr (0x56)
#define range2_addr (0x60)
#define range1_XSHUT   p19
#define range2_XSHUT   p18
#define VL53L0_I2C_SDA   p30 
#define VL53L0_I2C_SCL   p7  
 
#define Serial pc(USBTX, USBRX)

static DevI2C devI2c(VL53L0_I2C_SDA,VL53L0_I2C_SCL); 
static bool connected = false;
static uint8_t movementReadings[100] = {0};
static uint8_t currentReading = 0;
static uint8_t numReadings = 0;

void setConnected(bool _connected)
{
    connected = _connected;
}

void sendData()
{
    if (connected && numReadings > 0)
    {
        uint8_t dataCopy[numReadings];
        for (int i = 0; i < numReadings; i++)
        {
            dataCopy[i] = movementReadings[i];     
        }      
        
        updateCharacteristic(dataCopy, numReadings);
        numReadings = 0;  
    }
}

void addReading(const uint8_t measurement)
{   
    if (numReadings == 0)
        currentReading = 0;
    else if (currentReading == 50)
        currentReading = 0;
    movementReadings[currentReading] = measurement;
    if (numReadings < 50)
        numReadings += 1;
    currentReading += 1;
}


int main()
{
    /* initialize stuff */
    printf("\r\n********* Starting Main Loop *********\r\n");
    BLE& ble = BLE::Instance(BLE::DEFAULT_INSTANCE);
    ble_error_t error = ble.init(bleInitComplete);
    if (error)
    {
        printf("\r\nBluetooth Initialization Error %d\r\n", error);
    }
    while (ble.hasInitialized()  == false) { 
        printf("BLE Initializing!\r\n");
    }

    /*Contruct the sensors*/ 
    static DigitalOut shutdown1_pin(range1_XSHUT);
    static VL53L0X range1(&devI2c, &shutdown1_pin, NC);
    static DigitalOut shutdown2_pin(range2_XSHUT);
    static VL53L0X range2(&devI2c, &shutdown2_pin, NC);
    /*Initial all sensors*/   
    range1.init_sensor(range1_addr);
    range2.init_sensor(range2_addr);
    printf("Initialization done!\r\n");
 
    /*Get datas*/
    uint32_t distanceInner;
    uint32_t distanceOuter;
    while(1){
        range1.get_distance(&distanceInner);
        printf("Range1 [mm]:            %6ld\r\n", distanceInner);
 
        range2.get_distance(&distanceOuter);
        printf("Range2 [mm]:            %6ld\r\n", distanceOuter);
        
        uint8_t movement = get_movement(distanceInner, distanceOuter);
        if (movement)
        {
            addReading(movement);
        }   
        sendData();     
    }
}