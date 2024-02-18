package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.filesystem.Filesystem;

import java.util.Set;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class TestList {
    Computer computer;
    public TestList(Computer computer) {
        this.computer = computer;
    }

    public String testCommand(){
        Filesystem filesystem = computer.getOperatingSystem().getFilesystem();
        StringBuilder result = new StringBuilder();
        for (String fileName : filesystem.getRootFolder().getAllFileNames()){
            result.append(fileName).append("\n");
        }
        return result.toString().strip();
    }

}
