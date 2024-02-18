package com.timprogrammiert.jhack.devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class DeviceManager {
    private static List<Computer> allDevices = new ArrayList<>();
    private static Computer currentDevice;

    public static void registerNewDevice(Computer device){
        allDevices.add(device);
    }
    public static void setCurrentDevice(Computer device){
        currentDevice = device;
    }
    public static Computer getCurrentDevice(){return currentDevice;}
}
