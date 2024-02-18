package com.timprogrammiert.jhack.devices;

import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.users.User;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class OperatingSystem {
    Filesystem filesystem;
    User currentUser;

    public OperatingSystem() {
        filesystem = new Filesystem();
        currentUser = new User("root");
    }
    public Filesystem getFilesystem(){return filesystem;}

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
