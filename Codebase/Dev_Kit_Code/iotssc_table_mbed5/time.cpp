#include "mbed.h"
#include "bluetooth.h"

void initTime()
{
    set_time(0);   
}

time_t getTime()
{
    return time(NULL);    
}
