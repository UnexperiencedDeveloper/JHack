package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.permission.FilePermission;
import com.timprogrammiert.user.User;

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
    private FileMetaData(User user){
        // for filePermission creation
        createdTimeStamp = LocalDateTime.now();
        accessedTimeStamp = LocalDateTime.now();
        modifiedTimeStamp = LocalDateTime.now();
    }

    public static FileMetaData createMetaData(User user){
        return new FileMetaData(user);
    }


}
