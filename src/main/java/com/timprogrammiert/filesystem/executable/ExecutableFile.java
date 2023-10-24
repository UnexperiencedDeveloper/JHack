package com.timprogrammiert.filesystem.executable;

import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class ExecutableFile extends FileObject {

    public ExecutableFile(String fileName, FileType fileType, User user) {
        super(fileName, fileType, user);
    }
}
