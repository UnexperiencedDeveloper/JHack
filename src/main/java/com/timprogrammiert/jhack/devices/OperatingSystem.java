package com.timprogrammiert.jhack.devices;

import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.users.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class OperatingSystem {
    Filesystem filesystem;
    Map<String, User> userMap;
    User currentUser;

    public OperatingSystem() {
        userMap = new HashMap<>();
        setUpRootUser();
        filesystem = new Filesystem(this);
        currentUser = new User("root");

    }
    public Filesystem getFilesystem(){return filesystem;}

    public User getCurrentUser() {
        return currentUser;
    }
    public User getRootUser(){
        return userMap.get("root");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void setUpRootUser(){
        User rootUser = new User("root");
        currentUser = rootUser;
        userMap.put(rootUser.getUserName(), rootUser);
    }
}
