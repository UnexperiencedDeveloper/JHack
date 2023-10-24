package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileType;

/**
 * @author tmatz
 */
public class FileObject {
    private final String fileName;
    private final FileType fileType;
    private final FileMetaData fileMetaData;
    private Directory parent;

    // TODO FileMetaData should be created by the File / Directory itself
    protected FileObject(String fileName, FileType fileType, User user, Directory parent) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileMetaData = FileMetaData.createMetaData(user);
        this.parent = parent;
    }
    protected FileObject(String fileName, FileType fileType, User user) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileMetaData = FileMetaData.createMetaData(user);
    }

    public String getName(){
        return fileName;
    }

    public FileType getFileType(){
        return fileType;
    }

    public FileMetaData getFileMetaData(){
        return fileMetaData;
    }

    public Directory getParent(){
        return parent;
    }

}
