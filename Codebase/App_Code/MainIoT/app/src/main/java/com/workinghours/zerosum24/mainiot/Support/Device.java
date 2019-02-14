package com.workinghours.zerosum24.mainiot.Support;

public class Device {

    private String name;
    private String macAddress;
    private String connectionStatus;

    public Device(String name, String macAddress, String connectionStatus) {
        this.name = name;
        this.macAddress = macAddress;
        this.connectionStatus = connectionStatus;
    }

    public int connectionStatusToImageID() {

        return 0;
    }

    public String getName() {
        return name;
    }
}
