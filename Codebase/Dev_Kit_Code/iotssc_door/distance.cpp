#include "mbed.h"
#include <utility> 
 
#define Serial pc(USBTX, USBRX)

static bool lastOuter = false;
static bool lastInner = false;
static bool currentOuter = false;
static bool currentInner = false;
const static uint32_t DISTANCE_THRESHOLD = 700;


uint8_t get_movement(uint32_t distanceInner, uint32_t distanceOuter) {
    currentOuter = false; 
    currentInner = false;
    if (distanceInner < DISTANCE_THRESHOLD)
        currentInner = true;
    else
        currentInner = false;

    if (distanceOuter < DISTANCE_THRESHOLD)
        currentOuter = true;
    else
        currentOuter = false;
        
    if (currentInner && lastOuter && !currentOuter)
    {
        lastOuter = false;
        lastInner = false;
        return 1; // Someone entered the room
    }
    else if (currentOuter && lastInner && !currentInner)
    {
        lastOuter = false;
        lastInner = false
        return 2; // Exit
    }
    
    lastOuter = currentOuter;
    lastInner = currentInner;
    
    return 0;
}
