package com.timprogrammiert.user;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author tmatz
 */
public class UserGroup {
    private int gid;
    private String groupName;
    private Set<User> registeredUsers;

    public UserGroup(String groupName){
        this.groupName = groupName;
        registeredUsers = new LinkedHashSet<>();
    }

    public void addUser(User userToAdd){
        registeredUsers.add(userToAdd);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGid(int gid){
        this.gid = gid;
    }

    public int getGid() {
        return gid;
    }
}
