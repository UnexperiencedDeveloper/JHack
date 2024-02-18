package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.devices.OperatingSystem;
import com.timprogrammiert.jhack.permissions.PermissionUtil;
import com.timprogrammiert.jhack.users.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class Filesystem {

    Directory rootFolder;
    Directory currentDirectory;
    OperatingSystem operatingSystem;

    public Filesystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
        setupFilesystem();
    }
    public Directory getRootFolder(){
        return rootFolder;
    }

    public void setCurrentDirectory(Directory currentDirectory){
        this.currentDirectory = currentDirectory;
    }

    public Directory getCurrentDirectory(){
        return currentDirectory;
    }

    private void setupFilesystem(){
        User rootUser = operatingSystem.getCurrentUser();
        Directory root = new Directory("/", null, PermissionUtil.createDefaultDirectoryPermission(rootUser, rootUser.getPrimaryGroup()));
        rootFolder = root;
        Directory var = new Directory("var", root, PermissionUtil.createDefaultDirectoryPermission(rootUser, rootUser.getPrimaryGroup()));
        Directory etc = new Directory("etc", root, PermissionUtil.createDefaultDirectoryPermission(rootUser, rootUser.getPrimaryGroup()));
        setCurrentDirectory(root);
    }
}
