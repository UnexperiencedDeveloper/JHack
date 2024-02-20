package com.timprogrammiert.jhack.filesystem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Tim
 * Date: 19.02.2024
 * Version: 1.0
 */
public class MetaData {
    String createdDate;
    String modifiedDate;
    int modifiedCounter;
    FileSize fileSize;

    public MetaData() {
        this.createdDate = getCurrentTimestamp();
        this.modifiedDate = this.createdDate;
        this.modifiedCounter = 0;
        this.fileSize = new FileSize();
    }

    private String getCurrentTimestamp(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");
        return simpleDateFormat.format(date).replace(".", "");
    }

    public void updateModified(){
        modifiedDate = getCurrentTimestamp();
        modifiedCounter++;
    }

    public FileSize getFileSizeObject() {
        return fileSize;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getModifiedCounter(){
        return modifiedCounter;
    }
}
