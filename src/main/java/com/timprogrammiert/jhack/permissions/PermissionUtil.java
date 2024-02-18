package com.timprogrammiert.jhack.permissions;

import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class PermissionUtil {
    private static final String DIRECTORY_PEM_STRING = "drwxrwx---";
    private static final String FILE_PEM_STRING = "rwxrwx---";
    public static Permission createDefaultDirectoryPermission(User user, Group group){
        return new Permission(DIRECTORY_PEM_STRING, user, group);
    }
    public static Permission createDefaultFilePermission(User user, Group group){
        return new Permission(FILE_PEM_STRING, user, group);
    }

    public static String permissionCodeToString(String pemCode, BaseFile file) throws InvalidArgumentsException {
        StringBuilder pemStringBuilder = new StringBuilder();
        if(file instanceof Directory){pemStringBuilder.append("d");}
        for (int i = 0; i < 3; i++) {
            switch (pemCode.charAt(i)){
                case '0' -> pemStringBuilder.append("---");
                case '1' -> pemStringBuilder.append("--x");
                case '2' -> pemStringBuilder.append("-w-");
                case '3' -> pemStringBuilder.append("-wx");
                case '4' -> pemStringBuilder.append("r--");
                case '5' -> pemStringBuilder.append("r-x");
                case '6' -> pemStringBuilder.append("rw-");
                case '7' -> pemStringBuilder.append("rwx");
                default -> throw new InvalidArgumentsException("Invalid mode " + pemCode);
            }
        }
        return pemStringBuilder.toString();
    }


}
