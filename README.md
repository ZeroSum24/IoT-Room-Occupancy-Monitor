# IoTSSC - Internet of Things Systems Security and the Cloud

## A full-stack room occupancy and localization tool. 

Developed by Stephen Waddell and Samuel Knight

Supervised by Paul Patras

---

## System Overview
#### Embedded Devices: 

Devices used to capture data from sensors. Edge computing consisted of data aggregation and simple computations such as determining if a person entered or exited the room based on the recent distance sensor readings. All embedded devices were nRF52 DK models.

* Door-mounted device:

    Device connected to two distance sensors on either side of the doorway. In charge of reporting enter and exit readings. 

    Distance Sensors - [Time of Flight Adafruit VL53L0X](https://www.adafruit.com/product/3317)  

* Chair-mounted devices:

    Device connected to two pressure sensors monitoring the seat and back of the chair respectively. In charge of reporting chair occupancy based on both sensors being active (or inactive) for a duration of time. Each chair device advertises its presence via BLE for the table-mounted device.

    Pressure Sensors - [Force Sensitive Resistor Active Robots](https://www.active-robots.com/force-sensitive-resistor-square.html)  

* Table-mounted devices:

    Device connected to the center of each table. Responsible for scanning bluetooth signals emitted by the chair devices and determining their relative signal strengh (RSSI values). These values are used to determine if the chair is positioned at the table.

#### Gateway Application:

Application running on an android device for the purpose of routing data from the embedded devices up to the cloud. Application scans nearby embedded devices and reads in data asynchronously from multiple devices. Gateway is in charge with providing accurate timestamps for the data recorded. The data is then sent to the cloud database for futher processing.


#### Database:

Google's Firestore database used to store data organized into specific categories. Raw data is stored on a device-type basis and is timestamped. Other categorizations are used for processing data, holding real-time metrics and for the final visualization of the data.

#### Server and Website:

The server is hosted on AWS and is in charge of reading in raw data pushed to Firestore, processing the data and updating real-time metrics. The server used event listeners on all device topics in the database, avoiding costly polling of the entire database for new entries.

The website (also hosted on AWS) reads in processed data, as well as real-time metrics and visualizes these accordingly. Visualization is achieved using chartist.js for simple and clean graphs.