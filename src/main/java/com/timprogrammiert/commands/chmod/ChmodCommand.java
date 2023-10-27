package com.timprogrammiert.commands.chmod;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.permission.FilePermission;
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
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        if(argList.isEmpty()) return; // Do nothing
        try {
            int permissionNumber = Integer.parseInt(argList.get(0));
            path = new Path(argList.get(1));
            FileObject targetFile = path.resolvePath(host, FileObject.class);
            FilePermission filePermission = targetFile.getFileMetaData().getFilePermission();
            targetFile.getFileMetaData().setFilePermission(PermissionUtil.changePermission(filePermission, permissionNumber));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (FileObjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
