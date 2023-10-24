package com.timprogrammiert.commands.ls;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.filesystem.path.Path;
import com.timprogrammiert.host.Host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author tmatz
 */
public class LsCommand implements ICommand {
    private Host host;
    private boolean detailedList = false;
    private Path path;

    @Override
    public void execute(String[] args, Host host) {
        this.host = host;
        List<String> argList = parseArgumentsForTags(new ArrayList<>(Arrays.asList(args)));

        if(argList.isEmpty()){
            listCurrentDirectory();
        }else{
            path = new Path(argList.get(0));
            listAllChildren(path.resolvePath(host, Directory.class));
        }
    }

    private void listCurrentDirectory() {
        listAllChildren(host.getCurrentDirectory());
    }

    private void listAllChildren(FileObject baseItem){
        StringBuilder stringBuilder = new StringBuilder();
        if(baseItem instanceof Directory directoryObject){
            Collection<FileObject> children = directoryObject.getAllChildren();
            for (FileObject object: children) {
                stringBuilder.append(object.getName()).append("\n");
            }
            System.out.println(stringBuilder.toString().strip());
        }
    }

    private List<String> parseArgumentsForTags(List<String> argList){
        if(argList.contains("-al")){
            detailedList = true;
            argList.remove("-al");
        }
        return argList;
    }
}
