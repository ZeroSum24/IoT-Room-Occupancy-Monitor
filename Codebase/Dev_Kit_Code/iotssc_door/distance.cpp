#include "mbed.h"
#include <utility> 
 
#define Serial pc(USBTX, USBRX)

static bool lastOuter = false;
static bool lastInner = false;
static bool bothActive = false;
const static uint32_t DISTANCE_THRESHOLD = 700;


uint8_t get_movement(uint32_t distanceInner, uint32_t distanceOuter) {
    if (distanceInner < DISTANCE_THRESHOLD)
    {
        lastInner = true;
    }
    else
    {
        lastInner = false;
    }

    if (distanceOuter < DISTANCE_THRESHOLD)
    {
        lastOuter = true;
    }
    else
    {
        lastOuter = false;
    }
    if (lastInner && lastOuter)
        bothActive = true;
    else 
    {
        if (bothActive && lastInner == false)
        {
            bothActive = false;
            return 2;
        }
        else if (bothActive && lastOuter == false)
        {
            bothActive = false;
            return 1;  
        }
        bothActive = false;  
    }
    
    return 0;
}
