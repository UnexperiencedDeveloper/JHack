package com.timprogrammiert.filesystem.permission;

import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;
import com.timprogrammiert.util.FileTypeEnum;

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

    public static FilePermission createPermission(User user, FileTypeEnum fileTypeEnum){
        String permissionString = "";
        if(fileTypeEnum.equals(FileTypeEnum.Directory)){
            permissionString = DEFAULT_DIRECTORY_PERMISSION;
            return new FilePermission(permissionString, user, true);
        } else if (fileTypeEnum.equals(FileTypeEnum.RegularFile)) {
            permissionString = DEFAULT_FILE_PERMISSION;
            return new FilePermission(permissionString, user, false);
        } else if (fileTypeEnum.equals(FileTypeEnum.Executable)) {
            permissionString = DEFAULT_EXECUTABLE_PERMISSION;
            return new FilePermission(permissionString, user, false);
        }
        return new FilePermission(permissionString, user, false); // If something goes wrong, should never be called
    }

    public void setPermissionString(String permissionString){
        permissionString = isDirectory ? "d" + permissionString : "-" + permissionString;
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
