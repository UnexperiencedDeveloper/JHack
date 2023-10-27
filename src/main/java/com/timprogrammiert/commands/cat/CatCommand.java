package com.timprogrammiert.commands.cat;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.commands.exceptions.CommandExecutionException;
import com.timprogrammiert.filesystem.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.filesystem.regularFile.RegularFile;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tmatz
 * @version 1.0
 */
public class CatCommand implements ICommand {
    private Host host;
    private Path path;

    @Override
    public void execute(String[] args, Host host) throws CommandExecutionException {
        try {
            List<String> argList = new ArrayList<>(Arrays.asList(args));
            if(argList.isEmpty()) return; // Do nothing
            else  {
                path = new Path(argList.get(0));
                printContent(path.resolvePath(host, RegularFile.class));
            }
        } catch (FileObjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printContent(RegularFile regularFile){
        System.out.println(regularFile.getContent());
    }
}
