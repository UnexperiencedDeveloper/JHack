package com.timprogrammiert.jhack.permissions;

import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class Permission {
    String permissionString;
    User user;
    Group group;

    public Permission(String permissionString, User user, Group group) {
        this.permissionString = permissionString;
        this.user = user;
        this.group = group;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
