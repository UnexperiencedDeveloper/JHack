package com.timprogrammiert.jhack.permissions;

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
}
