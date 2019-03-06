#include "FSR.h"
#include "mbed.h"

FSR::FSR(PinName pin, float resistance) : _ain(pin), _r(resistance)
{
}

float FSR::readRaw()
{
    float read = _ain;
    return read;
}

float FSR::readFSRResistance()
{
    float read = _ain;
    return _r * 1 / read - _r;
}

float FSR::readWeightKG()
{
    float read = _ain;
    float rfsr = _r * 1 / read - _r;
    float slope = (4 - 2) / (log10(6.2) - log10(0.25));
    float a = log10(rfsr);
    if (a < log10(6.2))
    {
        float gram_result = pow(10, ((log10(6.2) - a) * slope + 2));
        return gram_result / 1000;
    }
    else
    {
        return 0;
    }    
}

float FSR::readWeight()
{
    float read = _ain;
    float rfsr = _r * 1 / read - _r;
    float slope = (4 - 2) / (log10(6.2) - log10(0.25));
    float a = log10(rfsr);
    if (a < log10(6.2))
    {
        return pow(10, ((log10(6.2) - a) * slope + 2));
    }
    else
    {
        return 0;
    }
}