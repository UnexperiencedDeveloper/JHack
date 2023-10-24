package com.timprogrammiert.filesystem.regularFile;

import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class RegularFile extends FileObject {
    public RegularFile(String fileName, FileType fileType, User user) {
        super(fileName, fileType, user);
    }
}
