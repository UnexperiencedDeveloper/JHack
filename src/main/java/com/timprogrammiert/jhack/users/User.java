package com.timprogrammiert.jhack.users;

import java.util.Collection;
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
    AccountInfo accountInfo;

    public User(String userName, Group primaryGroup) {
        accountInfo = new AccountInfo(UidManager.generateUid());
        UidManager.addUserToList(this);

        this.userName = userName;
        memberOfGroups = new HashMap<>();
        memberOfGroups.put(primaryGroup.getGroupName(), primaryGroup);
        primaryGroup.addUser(this);
    }



    public String getUserName() {
        return userName;
    }

    public Group getPrimaryGroup(){
        return memberOfGroups.get(userName);
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public Collection<Group> getAllGroups(){
        return memberOfGroups.values();
    }
    public void addToGroup(Group group){
        memberOfGroups.put(group.getGroupName(), group);
    }
}
