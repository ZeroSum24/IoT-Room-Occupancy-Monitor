#include "mbed.h"
#include "VL53L0X.h"
#include "bluetooth.h"
#include "distance.h"

#define Serial pc(USBTX, USBRX)

#define rangeInner_addr (0x56)
#define rangeOuter_addr (0x60)
#define rangeInner_XSHUT   p18
#define rangeOuter_XSHUT   p19
#define VL53L0_I2C_SDA   p30 
#define VL53L0_I2C_SCL   p7  


static DevI2C devI2c(VL53L0_I2C_SDA, VL53L0_I2C_SCL); 
static bool connected = false;
static uint8_t movementReadings[100] = {0};
static uint8_t currentReading = 0;
static uint8_t numReadings = 0;

static DigitalOut shutdown1_pin(rangeInner_XSHUT);
static VL53L0X rangeInner(&devI2c, &shutdown1_pin, NC);
static DigitalOut shutdown2_pin(rangeOuter_XSHUT);
static VL53L0X rangeOuter(&devI2c, &shutdown2_pin, NC);

static uint32_t distanceInner;
static uint32_t distanceOuter;
static int status1;
static int status2;

Ticker ticker;

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

void readDistance() {
    status1 = rangeInner.get_distance(&distanceInner);
    status2 = rangeOuter.get_distance(&distanceOuter);
    if (status1 == VL53L0X_ERROR_NONE) {
        if (distanceInner == 0)
            distanceInner = 1000;
        printf("rangeInner [mm]:            %6ld\r\n", distanceInner);
    } else {
        distanceInner = 1000;
        printf("rangeInner [mm]:                --\r\n");
    }
    if (status2 == VL53L0X_ERROR_NONE) {
        if (distanceOuter == 0)
            distanceOuter = 1000;
        printf("rangeOuter [mm]:            %6ld\r\n", distanceOuter);
    } else {
        distanceOuter = 1000;
        printf("rangeOuter [mm]:                --\r\n");
    }
    uint8_t movement = get_movement(distanceInner, distanceOuter);
    printf("\r\nMovement Detected: %d\r\n", movement);
    if (movement)
        addReading(movement);  
    return;
}

void wakeUp() {}

int main(void)
{
    rangeInner.init_sensor(rangeInner_addr);
    rangeOuter.init_sensor(rangeOuter_addr);
    
    printf("\n\r********* Starting Main Loop *********\n\r");
  
    BLE& ble = BLE::Instance(BLE::DEFAULT_INSTANCE);
    ble.init(bleInitComplete);
    
    ticker.attach(wakeUp, 0.1);
    
    while (ble.hasInitialized()) {
        readDistance();
        sendData();
        ble.waitForEvent(); 
    }
}