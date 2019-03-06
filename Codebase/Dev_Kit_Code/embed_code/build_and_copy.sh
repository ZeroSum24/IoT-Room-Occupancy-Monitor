#!/bin/bash

mbed compile -t GCC_ARM -m NRF51_DK;

sleep 2;

cp ./BUILD/NRF51_DK/GCC_ARM/embed_code.hex /media/samdknight/DAPLINK;
