package com.timprogrammiert.filesystem.permission;

import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class FilePermission {
    public static String DEFAULT_FILE_PERMISSION = "rw-rw-rw-";
    public static String DEFAULT_EXECUTABLE_PERMISSION = "rwxr-xr-x";
    public static String DEFAULT_DIRECTORY_PERMISSION = "drwxrwxrwx";

    private String permissionString;
    private final User user;
    private UserGroup userGroup;

    private FilePermission(String permissionString, User user) {
        this.permissionString = permissionString;
        this.user = user;
    }

    public static FilePermission createPermission(User user, FileType fileType){
        String permissionString = "";
        if(fileType.equals(FileType.Directory)){
            permissionString = DEFAULT_DIRECTORY_PERMISSION;
        } else if (fileType.equals(FileType.RegularFile)) {
            permissionString = DEFAULT_FILE_PERMISSION;
        } else if (fileType.equals(FileType.Executable)) {
            permissionString = DEFAULT_EXECUTABLE_PERMISSION;
        }
        return new FilePermission(permissionString, user);
    }

    public void setPermissionString(){

    }

    public User getUser(){
        return user;
    }

    public UserGroup getUserGroup(){
        return userGroup;
    }
}
