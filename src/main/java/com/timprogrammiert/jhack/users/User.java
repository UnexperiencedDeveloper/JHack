package com.timprogrammiert.jhack.users;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class User {
    String userName;
    Map<String, Group> memberOfGroups;

    public User(String userName) {
        this.userName = userName;
        memberOfGroups = new HashMap<>();
        Group primaryGroup = new Group(userName);
        memberOfGroups.put(primaryGroup.getGroupName(), primaryGroup);
        primaryGroup.addUser(this);
    }

    public String getUserName() {
        return userName;
    }

    public Group getPrimaryGroup(){
        return memberOfGroups.get(userName);
    }
}
