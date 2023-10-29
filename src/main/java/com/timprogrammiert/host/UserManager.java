package com.timprogrammiert.host;

import com.timprogrammiert.filesystem.VirtualFileSystem;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.manager.EtcPasswdManager;
import com.timprogrammiert.user.User;
import com.timprogrammiert.user.UserGroup;
import com.timprogrammiert.util.FileTypeEnum;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The `UserManager` class manages user-related operations and user data in the system.
 * It allows creating new users, setting the current user, and retrieving user information.
 * Additionally, it handles user home directory creation and interacts with the EtcPasswdManager
 * for managing user data in the `/etc/passwd` file.
 *
 * @author tmatz
 * @version 1.0
 */
public class UserManager {
    private Set<User> registeredUsers;
    private Set<UserGroup> registeredGroups;
    private VirtualFileSystem fileSystem;
    private EtcPasswdManager etcPasswdManager;

    /**
     * Constructs a new `UserManager` instance with the specified virtual file system.
     *
     * @param fileSystem The virtual file system to manage user directories and files.
     */
    public UserManager(VirtualFileSystem fileSystem) {
        registeredUsers = new LinkedHashSet<>();
        registeredGroups = new LinkedHashSet<>();
        this.fileSystem = fileSystem;
        etcPasswdManager = new EtcPasswdManager(fileSystem.getHost());
    }
    public void setCurrentUser(){

    }
    /**
     * Creates a new user with the given username. This function creates a new user group for the user,
     * assigns a unique UID and GID, creates a home directory, writes the user data to the `/etc/passwd` file,
     * and adds the new user and its primary group to the registered users and groups.
     *
     * @param userName The username of the new user.
     * @return The newly created User object.
     */
    public User createNewUser(String userName){
        UserGroup primaryGroupForUser = new UserGroup(userName);
        User userToCreate = new User(userName, primaryGroupForUser);

        // Assign unique UID and GID
        int uidCounter = registeredUsers.size() + 1000;
        userToCreate.setUid(uidCounter);
        primaryGroupForUser.setGid(uidCounter);

        userToCreate.setHomeDirectory(createHomeDirectory(userToCreate));

        etcPasswdManager.writeNewUser(userToCreate);

        // Add the new user and its primary group to the registered users and groups
        registeredUsers.add(userToCreate);
        registeredGroups.add(primaryGroupForUser);
        return userToCreate;
    }

    /**
     * Creates a home directory for the given user under the base home directory of the file system.
     *
     * @param user The user for whom the home directory is created.
     */
    private Directory createHomeDirectory(User user){
        Directory baseHomeDirectory = fileSystem.getHomeDirectory();
        Directory newHomeDirectory = Directory.createDirectory(user.getUserName(), FileTypeEnum.Directory, user, baseHomeDirectory);
        baseHomeDirectory.addNewChildren(newHomeDirectory);
        return newHomeDirectory;
    }
}
