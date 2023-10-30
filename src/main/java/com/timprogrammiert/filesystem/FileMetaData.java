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
    private LocalDateTime createdTimeStamp, accessedTimeStamp, modifiedTimeStamp;
    private FileSize fileSize;
    private FileMetaData(User user, FileTypeEnum fileTypeEnum, FileObject fileObject){
        // for filePermission creation
        createdTimeStamp = LocalDateTime.now();
        accessedTimeStamp = LocalDateTime.now();
        modifiedTimeStamp = LocalDateTime.now();
        filePermission = FilePermission.createPermission(user, fileTypeEnum);
        fileSize = new FileSize(fileObject);
    }

    public static FileMetaData createMetaData(User user, FileTypeEnum fileTypeEnum, FileObject fileObject){
        return new FileMetaData(user, fileTypeEnum, fileObject);
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

    public void setFilePermission(FilePermission filePermission) {
        this.filePermission = filePermission;
    }

    public void setAccessedTimeStamp() {
        this.accessedTimeStamp = LocalDateTime.now();
    }

    public void setModifiedTimeStamp() {
        this.modifiedTimeStamp = LocalDateTime.now();
    }

    public FileSize getFileSize() {
        return fileSize;
    }
}
