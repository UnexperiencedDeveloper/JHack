package com.timprogrammiert.jhack.devices;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class Computer extends Device{
    OperatingSystem operatingSystem;

    public Computer() {
        operatingSystem = new OperatingSystem();
    }
    public OperatingSystem getOperatingSystem(){return operatingSystem;}
}
