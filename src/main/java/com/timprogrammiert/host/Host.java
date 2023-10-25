package com.timprogrammiert.host;

import com.timprogrammiert.filesystem.VirtualFileSystem;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tmatz
 */
public class Host {
    private final VirtualFileSystem fileSystem;
    private final Set<User> registeredUsers;
    private final Set<UserGroup> registeredGroups;
    private final String hostName;
    private Directory currentDirectory;
    private User currentUser;

    public Host(String hostName) {
        this.hostName = hostName;
        fileSystem = new VirtualFileSystem(this);
        registeredGroups = new HashSet<>();
        registeredUsers = new HashSet<>();
        currentDirectory = fileSystem.getRootDirectory();
    }

    public String getHostName(){
        return hostName;
    }

    public Directory getRootDirectory(){
        return fileSystem.getRootDirectory();
    }

    public VirtualFileSystem getFileSystem(){
        return fileSystem;
    }

    public Directory getCurrentDirectory(){
        return currentDirectory;
    }

    public User getCurrentUser(){
        return currentUser;
    }


}
