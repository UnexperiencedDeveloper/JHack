package com.timprogrammiert.user;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a user in the system with a username, primary group, and secondary groups.
 *
 * @version 1.0
 * @author tmatz
 */
public class User {
    private String userName;                // The username of the user
    private UserGroup primaryGroup;          // The user's primary group
    private Set<UserGroup> secondaryGroups;  // Set of secondary groups the user belongs to
    private int uid;                         // User ID (unique identifier)

    /**
     * Constructor to initialize a User object with a username and primary group.
     *
     * @param userName The username of the user.
     * @param primaryGroup The user's primary group.
     */
    public User(String userName, UserGroup primaryGroup) {
        this.userName = userName;
        secondaryGroups = new LinkedHashSet<>(); // Initialize the set of secondary groups
        this.primaryGroup = primaryGroup;        // Set the primary group for the user
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the secondary groups the user belongs to.
     *
     * @return Set of secondary groups.
     */
    public Set<UserGroup> getSecondaryGroups() {
        return secondaryGroups;
    }

    /**
     * Adds the user to a secondary group.
     *
     * @param group The secondary group to add the user to.
     */
    public void addToSecondaryGroup(UserGroup group){
        secondaryGroups.add(group);
    }

    /**
     * Gets the user's primary group.
     *
     * @return The user's primary group.
     */
    public UserGroup getPrimaryGroup() {
        return primaryGroup;
    }
}
