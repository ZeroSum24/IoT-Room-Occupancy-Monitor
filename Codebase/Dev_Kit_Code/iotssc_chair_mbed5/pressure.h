#ifndef PRESSURE_H
#define PRESSURE_H

bool readPressure(const int num_readings_until_toggle, bool* chair_occupied, int* successive_readings, bool fsr_back=true);

#endif