package com.timprogrammiert.filesystem.executable;

import com.timprogrammiert.commands.ICommand;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileTypeEnum;

/**
 * @author tmatz
 */
public class ExecutableFile extends FileObject {
    private ICommand command;

    private ExecutableFile(String fileName, FileTypeEnum fileTypeEnum, User user, ICommand command, Directory parent) {
        super(fileName, fileTypeEnum, user, parent);
        this.command = command;
    }

    public static ExecutableFile createExecutable(String fileName, FileTypeEnum fileTypeEnum, User user, ICommand command, Directory parent){
        return new ExecutableFile(fileName, fileTypeEnum, user, command, parent);
    }

    public ICommand getCommand(){
        return command;
    }
}
