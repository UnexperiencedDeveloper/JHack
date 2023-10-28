package com.timprogrammiert.commands.chmod;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.commands.exceptions.PermissionDeniedException;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.FilePermission;
import com.timprogrammiert.filesystem.permission.PermissionChecker;
import com.timprogrammiert.filesystem.permission.PermissionUtil;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tmatz
 * @version 1.0
 */
public class ChmodCommand implements ICommand {
    private Host host;
    private Path path;
    private final String commandName = "chmod";
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        if(argList.isEmpty()) return; // Do nothing
        try {
            // check Write Pem for File + Check Write Pem for Parent
            int permissionNumber = Integer.parseInt(argList.get(0));
            path = new Path(argList.get(1));
            PermissionChecker permissionChecker = new PermissionChecker(path.resolvePath(host, FileObject.class),host);
            PermissionChecker permissionCheckerParent = new PermissionChecker(path.resolvePath(host, FileObject.class).getParent(), host);
            FileObject targetFile = path.resolvePath(host, FileObject.class);
            if (permissionChecker.isCanWrite() && permissionCheckerParent.isCanWrite()) {
                FilePermission filePermission = targetFile.getFileMetaData().getFilePermission();
                targetFile.getFileMetaData().setFilePermission(PermissionUtil.changePermission(filePermission, permissionNumber));

                targetFile.getFileMetaData().setModifiedTimeStamp();
                targetFile.getParent().getFileMetaData().setModifiedTimeStamp();
            } else {
                throw new PermissionDeniedException(String.format("changing permissions of '%s' : Operation not permitted" , targetFile.getName()));
            }

        } catch (NumberFormatException | FileObjectNotFoundException | PermissionDeniedException e) {
            throw new CommandExecutionException(commandName + ": " + e.getMessage());
        }
    }
}
