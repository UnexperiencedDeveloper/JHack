package com.timprogrammiert.filesystem.permission;

import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class FilePermission {
    public static String DEFAULT_FILE_PERMISSION = "-rw-rw-rw-";
    public static String DEFAULT_EXECUTABLE_PERMISSION = "-rwxr-xr-x";
    public static String DEFAULT_DIRECTORY_PERMISSION = "drwxrwxrwx";

    private String permissionString;
    private final User user;
    private final UserGroup userGroup;
    private final boolean isDirectory;

    private FilePermission(String permissionString, User user, boolean isDirectory) {
        this.permissionString = permissionString;
        this.user = user;
        userGroup = user.getPrimaryGroup();
        this.isDirectory = isDirectory;
    }

    public static FilePermission createPermission(User user, FileType fileType){
        String permissionString = "";
        if(fileType.equals(FileType.Directory)){
            permissionString = DEFAULT_DIRECTORY_PERMISSION;
            return new FilePermission(permissionString, user, true);
        } else if (fileType.equals(FileType.RegularFile)) {
            permissionString = DEFAULT_FILE_PERMISSION;
            return new FilePermission(permissionString, user, false);
        } else if (fileType.equals(FileType.Executable)) {
            permissionString = DEFAULT_EXECUTABLE_PERMISSION;
            return new FilePermission(permissionString, user, false);
        }
        return new FilePermission(permissionString, user, false); // If something goes wrong, should never be called
    }

    public void setPermissionString(String permissionString){
        if(isDirectory) permissionString = "d" + permissionString;
        this.permissionString = permissionString;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public User getUser(){
        return user;
    }

    public UserGroup getUserGroup(){
        return userGroup;
    }
}
