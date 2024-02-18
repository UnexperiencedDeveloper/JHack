package com.timprogrammiert.jhack;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.gui.JHackGui;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class JHack extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        Computer computer = new Computer();
        DeviceManager.registerNewDevice(computer);
        DeviceManager.setCurrentDevice(computer);
        new JHackGui(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
