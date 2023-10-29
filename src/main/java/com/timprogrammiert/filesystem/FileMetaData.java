package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.permission.FilePermission;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileTypeEnum;

import java.time.LocalDateTime;

/**
 * @author tmatz
 */
public class FileMetaData {
    private FilePermission filePermission;
    private String fileSize;
    private LocalDateTime createdTimeStamp, accessedTimeStamp, modifiedTimeStamp;

    public FileMetaData() {

    }
    private FileMetaData(User user, FileTypeEnum fileTypeEnum){
        // for filePermission creation
        createdTimeStamp = LocalDateTime.now();
        accessedTimeStamp = LocalDateTime.now();
        modifiedTimeStamp = LocalDateTime.now();
        filePermission = FilePermission.createPermission(user, fileTypeEnum);
    }

    public static FileMetaData createMetaData(User user, FileTypeEnum fileTypeEnum){
        return new FileMetaData(user, fileTypeEnum);
    }

    public LocalDateTime getAccessedTimeStamp() {
        return accessedTimeStamp;
    }

    public LocalDateTime getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public LocalDateTime getModifiedTimeStamp() {
        return modifiedTimeStamp;
    }

    public FilePermission getFilePermission() {
        return filePermission;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFilePermission(FilePermission filePermission) {
        this.filePermission = filePermission;
    }

    public void setAccessedTimeStamp() {
        this.accessedTimeStamp = LocalDateTime.now();
    }

    public void setModifiedTimeStamp() {
        this.modifiedTimeStamp = LocalDateTime.now();
    }
}
