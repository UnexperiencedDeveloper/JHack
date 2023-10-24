package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.permission.FilePermission;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileType;

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
    private FileMetaData(User user, FileType fileType){
        // for filePermission creation
        createdTimeStamp = LocalDateTime.now();
        accessedTimeStamp = LocalDateTime.now();
        modifiedTimeStamp = LocalDateTime.now();
        filePermission = FilePermission.createPermission(user, fileType);
    }

    public static FileMetaData createMetaData(User user, FileType fileType){
        return new FileMetaData(user, fileType);
    }


}
