package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.permissions.Permission;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class File extends BaseFile{
    public File(String name, Directory parent, Permission permission) {
        super(name, parent, permission);
    }
}
