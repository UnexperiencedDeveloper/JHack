package com.timprogrammiert.jhack.devices;

import com.timprogrammiert.jhack.exceptions.GroupAlreadyExistsException;
import com.timprogrammiert.jhack.exceptions.UserAlreadyExistsException;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class OperatingSystem {
    private static Logger logger = LoggerFactory.getLogger(OperatingSystem.class);
    Filesystem filesystem;
    Map<String, User> userMap;
    Map<String, Group> groupMap;
    User currentUser;

    public OperatingSystem() {
        userMap = new HashMap<>();
        groupMap = new HashMap<>();
        setUpRootUser();
        filesystem = new Filesystem(this);

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
    public User createNewUser(String userName) throws UserAlreadyExistsException, GroupAlreadyExistsException {
        if(userMap.containsKey(userName)){
            throw new UserAlreadyExistsException(userName);
        }
        Group primaryGroup = createNewGroup(userName);
        User createdUser = new User(userName, primaryGroup);
        userMap.put(userName, createdUser);
        return createdUser;
    }

    public User getSpecificUser(String userName){
        return userMap.get(userName);
    }

    private void setUpRootUser(){
        try {
            User rootUser =  createNewUser("root");
            currentUser = rootUser;
            userMap.put(rootUser.getUserName(), rootUser);
        } catch (UserAlreadyExistsException | GroupAlreadyExistsException e) {
            logger.debug("Cant craete Root User - already exists");
        }

    }

    public boolean isValidUser(String userName){
        return userMap.containsKey(userName);
    }
    public Group createNewGroup(String groupName) throws GroupAlreadyExistsException {
        if(groupMap.containsKey(groupName)){
            throw new GroupAlreadyExistsException(groupName);
        }
        Group createdGroup = new Group(groupName);
        groupMap.put(groupName, createdGroup);
        return createdGroup;
    }
    public boolean isValidGroup(String groupName){
        return groupMap.containsKey(groupName);
    }

    public Group getSpecificGroup(String groupName){
        return groupMap.get(groupName);
    }
}
