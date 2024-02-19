package com.timprogrammiert.jhack.terminal.commands;

import com.timprogrammiert.jhack.devices.Computer;
import com.timprogrammiert.jhack.devices.DeviceManager;
import com.timprogrammiert.jhack.exceptions.GroupDoesNotExistException;
import com.timprogrammiert.jhack.exceptions.InvalidArgumentsException;
import com.timprogrammiert.jhack.exceptions.UserDoesNotExistException;
import com.timprogrammiert.jhack.users.Group;
import com.timprogrammiert.jhack.users.User;
import com.timprogrammiert.jhack.utils.CommandRessources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class IdCommand implements ICommand{
    private static final Logger logger = LoggerFactory.getLogger(IdCommand.class);
    private static final String USER_FLAG = "-u";
    private static final String GROUP_FLAG = "-g";
    private static final String ALL_GROUPS_FLAG = "-G";
    Computer computer;
    @Override
    public String run(String[] args) {
        computer = DeviceManager.getCurrentDevice();
        return handleArguments(new ArrayList<>(Arrays.asList(args)));
    }
    private String handleArguments(List<String> args) {
        // If no specific username is provided, add the current user
        if (args.size() == 1) {
            args.add(computer.getOperatingSystem().getCurrentUser().getUserName());
        }
        try {
            if(args.contains("-h")){
                throw new InvalidArgumentsException(CommandRessources.ID_USAGE);
            }
            // Check command options
            if (args.contains(USER_FLAG)) {
                return userId(args.get(1));
            } else if (args.contains(GROUP_FLAG)) {
                return getGroupId(args.get(1));
            } else if (args.contains(ALL_GROUPS_FLAG)) {
                return getAllGroupsByUser(args.get(1));
            } else {
                return withoutArguments();
            }
        } catch (UserDoesNotExistException | GroupDoesNotExistException | InvalidArgumentsException e) {
            return e.getMessage();
        }
    }



    private String withoutArguments(){
        User currentUser = computer.getOperatingSystem().getCurrentUser();
        String result = String.format("uid=%s(%s) gid=%s(%s)",
                currentUser.getAccountInfo().getUid(),
                currentUser.getUserName(),
                currentUser.getPrimaryGroup().getGroupInfo().getGuid(),
                currentUser.getPrimaryGroup().getGroupName()
        );
        Collection<Group> userGroups = currentUser.getAllGroups();
        StringBuilder otherGroups = new StringBuilder();

        // Append additional groups
        for (Group group : userGroups) {
            if (group.getGroupName().equals(currentUser.getUserName())) {
                continue;
            }
            otherGroups.append(" groups=");
            otherGroups.append(String.format("%s(%s),",
                    group.getGroupName(),
                    group.getGroupInfo().getGuid()));
        }
        return result + otherGroups.toString();
    }

    /**
     * Returns user ID based on the provided username.
     *
     * @param userName Username for which to retrieve the user ID.
     * @return String representation of the user ID.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    private String userId(String userName) throws UserDoesNotExistException {
        if(!computer.getOperatingSystem().isValidUser(userName)){
            throw new UserDoesNotExistException(userName);
        }
        User user = computer.getOperatingSystem().getSpecificUser(userName);
        return String.valueOf(user.getAccountInfo().getUid());
    }

    /**
     * Returns Group ID based on the provided Groupname.
     *
     * @param groupName Groupname for which to retrieve the Group ID
     * @return String representation of the Group ID
     * @throws GroupDoesNotExistException if the Group does not exist
     * @throws InvalidArgumentsException if now GroupName is provided
     */
    private String getGroupId(String groupName) throws GroupDoesNotExistException, InvalidArgumentsException {
        if(groupName.isEmpty()){throw new InvalidArgumentsException("Groupname Empty");}
        if(!computer.getOperatingSystem().isValidGroup(groupName)){
            throw new GroupDoesNotExistException(groupName);
        }
        return "" + computer.getOperatingSystem().getSpecificGroup(groupName).getGroupInfo().getGuid();

    }

    /**
     * Retrieves all group memberships of a user based on the provided username.
     *
     * @param userName Username for which to retrieve group memberships.
     * @return String representation of all group memberships.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    private String getAllGroupsByUser(String userName) throws UserDoesNotExistException {
        if(!computer.getOperatingSystem().isValidUser(userName)){
            throw new UserDoesNotExistException(userName);
        }
        User user = computer.getOperatingSystem().getSpecificUser(userName);
        StringBuilder result = new StringBuilder();
        for (Group group : user.getAllGroups()) {
            result.append(group.getGroupName()).append(" ");
        }
        return result.toString().strip();
    }

}
