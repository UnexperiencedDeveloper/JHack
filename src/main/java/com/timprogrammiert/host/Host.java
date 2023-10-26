package com.timprogrammiert.host;

import com.timprogrammiert.filesystem.VirtualFileSystem;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a host system in a virtual environment. It contains information about the host's file system,
 * registered users, registered user groups, hostname, current working directory, and the currently logged-in user.
 * This class is responsible for managing the state of the host system.
 *
 * @author tmatz
 * @version 1.0
 */
public class Host {
    private final VirtualFileSystem fileSystem;
    private final Set<User> registeredUsers;
    private final Set<UserGroup> registeredGroups;
    private final String hostName;
    private Directory currentDirectory;
    private User currentUser;
    private User rootUser;

    public Host(String hostName) {
        this.hostName = hostName;
        rootUser = createNewUser("root");
        currentUser = createNewUser("tim");
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

    public User createNewUser(String userName){
        UserGroup newGroup = new UserGroup(userName); // Each user has its own Group
        User newUser = new User(userName, newGroup);
        newGroup.addUser(newUser);
        return newUser;
    }

    public User getRootUser() {
        return rootUser;
    }
}
