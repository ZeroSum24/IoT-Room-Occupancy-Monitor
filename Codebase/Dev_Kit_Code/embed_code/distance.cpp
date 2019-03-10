#include "mbed.h"
#include "VL53L0X.h"
#include <utility>

#define range1_addr (0x56)
#define range2_addr (0x60)
#define range1_XSHUT   p18
#define range2_XSHUT   p19
#define VL53L0_I2C_SDA   p30 
#define VL53L0_I2C_SCL   p7  
 
#define Serial pc(USBTX, USBRX);

static DevI2C devI2c(VL53L0_I2C_SDA,VL53L0_I2C_SCL); 
static DigitalOut shutdown1_pin(range1_XSHUT);
static VL53L0X range1(&devI2c, &shutdown1_pin, NC);
static DigitalOut shutdown2_pin(range2_XSHUT);
static VL53L0X range2(&devI2c, &shutdown2_pin, NC);

void initialize_distance_sensors() {
    range1.init_sensor(range1_addr);
    range2.init_sensor(range2_addr);
    printf("\r\nSensors Initialized!\r\n");
}

std::pair<int, int> get_distances() {
    uint32_t distance1 = 0;
    uint32_t distance2 = 0;
    int status1;
    int status2;

    status1 = range1.get_distance(&distance1);
    if (status1 == VL53L0X_ERROR_NONE) {
        printf("Range1 [mm]:            %6ld\r\n", distance1);
    } else {
        printf("Range1 [mm]:                --\r\n");
    }

    status2 = range2.get_distance(&distance2);
    if (status2 == VL53L0X_ERROR_NONE) {
        printf("Range2 [mm]:            %6ld\r\n", distance2);
    } else {
        printf("Range2 [mm]:                --\r\n");
    }
    std::pair<int, int> distances(distance1, distance2);
    return distances;
}
