package com.timprogrammiert.jhack.users;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class Group {
    String groupName;
    Map<String, User> userMap;
    GroupInfo groupInfo;

    public Group(String groupName) {
        groupInfo = new GroupInfo(GidManager.generateUid());
        GidManager.addGroupToList(this);
        this.groupName = groupName;
        userMap = new HashMap<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void addUser(User user){
        userMap.put(user.getUserName(), user);
    }

    public User getUser(String userName){
        return userMap.get(userName);
    }
    public boolean hasMember(String userName){
        return userMap.containsKey(userName);
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }
}
