package com.timprogrammiert.jhack.filesystem;

import java.util.Random;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class FileSize {
    private static final int MIN_START_SIZE = 0;
    private static final int MAX_START_SIZE = 100;
    int fileSize;

    public FileSize() {
        fileSize = createRandomFileSize();
    }

    private int createRandomFileSize(){
        Random random = new Random();
        return random.nextInt(MIN_START_SIZE, MAX_START_SIZE);
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void adjustCustomFileSize(int fileSize){
        this.fileSize = fileSize;
    }
}
