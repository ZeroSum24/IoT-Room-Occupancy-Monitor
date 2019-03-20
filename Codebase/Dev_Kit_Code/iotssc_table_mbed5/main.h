#ifndef MAIN_H
#define MAIN_H

extern EventQueue eventQueue;

void setConnected(bool connected);

void addReading(const uint16_t timestamp, const uint16_t chair_id, const uint16_t signal_strength);

#endif