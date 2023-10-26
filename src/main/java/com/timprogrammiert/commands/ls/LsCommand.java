package com.timprogrammiert.commands.ls;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The {@code LsCommand} class implements the ICommand interface to execute the 'ls' command in a simulated
 * Linux file system environment. It can list the contents of the current directory or a specified directory.
 * Supports the '-al' option for detailed listing.
 * <p>
 * Usage:
 * - To list contents of the current directory: LsCommand.execute(new String[]{}, host);
 * - To list contents of a specific directory: LsCommand.execute(new String[]{path}, host);
 *
 * @author tmatz
 * @version 1.0
 * @see ICommand
 */
public class LsCommand implements ICommand {
    private Host host; // The host environment in which the command is executed
    private boolean detailedList = false; // Flag indicating whether detailed listing is requested
    private Path path; // The path of the directory to list (optional) only set if not the current directory to list
    private String commandName = "ls";

    /**
     * Executes the 'ls' command based on the provided arguments and the current host environment.
     *
     * @param args An array of command arguments. If empty, lists the contents of the current directory.
     *             If contains a path, lists the contents of the specified directory.
     * @param host The host environment in which the command is executed.
     */
    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        this.host = host;
        List<String> argList = parseArgumentsForTags(new ArrayList<>(Arrays.asList(args)));

        if(argList.isEmpty()){
            listCurrentDirectory();
        }else{
            path = new Path(argList.get(0));
            try {
                listAllChildren(path.resolvePath(host, Directory.class));
            }catch (FileObjectNotFoundException e){
                throw new CommandExecutionException(commandName + ": " + e.getMessage());
            }
        }
    }

    /**
     * Lists the contents of the current directory in the host environment.
     */
    private void listCurrentDirectory() {
        listAllChildren(host.getCurrentDirectory());
    }

    /**
     * Lists all children (files and directories) of the specified base directory.
     *
     * @param baseItem The base directory whose contents need to be listed.
     */
    private void listAllChildren(FileObject baseItem){
        StringBuilder stringBuilder = new StringBuilder();
        if(baseItem instanceof Directory directoryObject){
            Collection<FileObject> children = directoryObject.getAllChildren();
            for (FileObject object: children) {
                if(detailedList){
                    FileMetaData metaData = object.getFileMetaData();
                    stringBuilder.append(metaData.getFilePermission().getPermissionString()).append(" ")
                            .append(metaData.getFilePermission().getUser().getUserName()).append(" ")
                            .append(metaData.getFilePermission().getUserGroup().getGroupName()).append(" ")
                            .append("FileSize").append(" ")
                            .append(metaData.getModifiedTimeStamp()).append(" ")
                            .append(object.getName()).append("\n");
                }else {
                    stringBuilder.append(object.getName()).append("\n");
                }
            }
            System.out.println(stringBuilder.toString().strip());
        }
        detailedList = false;
    }

    /**
     * Parses the command arguments and extracts options/tags.
     *
     * @param argList A list of command arguments to be parsed.
     * @return A list of parsed arguments without the options/tags.
     */
    private List<String> parseArgumentsForTags(List<String> argList){
        if(argList.contains("-al")){
            detailedList = true;
            argList.remove("-al");
        }
        return argList;
    }
}
