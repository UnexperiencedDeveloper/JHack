package com.timprogrammiert.host;

import com.timprogrammiert.filesystem.VirtualFileSystem;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;
import com.timprogrammiert.util.FileType;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private final String hostName;
    private Directory currentDirectory;
    private User currentUser; // TO BE REPLACED WITH USERMANAGER
    private User rootUser;
    private UserManager userManager;

    public Host(String hostName) {
        this.hostName = hostName;
        rootUser = createRootUser();
        fileSystem = new VirtualFileSystem(this);
        userManager = new UserManager(fileSystem);
        currentUser = createNewUser("tim"); // TO BE REPLACED WITH USERMANAGER
        currentDirectory = fileSystem.getRootDirectory();
        createNewUser("testUser");
    }
    public void setCurrentDirectory(Directory currentDirectory){
        this.currentDirectory = currentDirectory;
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
        return userManager.createNewUser(userName);
    }

    public User getRootUser() {
        return rootUser;
    }

    private User createRootUser(){
        UserGroup rootGroup = new UserGroup("root"); // Each user has its own Group
        rootGroup.setGid(0);
        User rootUser = new User("root", rootGroup);
        rootUser.setUid(0);
        rootGroup.addUser(rootUser);
        return rootUser;
    }

}
