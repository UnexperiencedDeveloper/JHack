package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileTypeEnum;

/**
 * The {@code FileObject} class represents a generic file or directory in a simulated file system.
 * It encapsulates essential properties such as the file name, file type, and metadata.
 *
 * @author tmatz
 * @version 1.0
 */
public class FileObject {
    private final String fileName;
    private final FileTypeEnum fileTypeEnum;
    private final FileMetaData fileMetaData;
    private Directory parent;

    /**
     * Constructs a FileObject with the specified file name, file type, user, and parent directory.
     *
     * @param fileName The name of the file or directory.
     * @param fileTypeEnum The type of the file (regular file or directory).
     * @param user     The user associated with the file.
     * @param parent   The parent directory of the file (null if the file is in the root directory).
     */
    protected FileObject(String fileName, FileTypeEnum fileTypeEnum, User user, Directory parent) {
        this.fileName = fileName;
        this.fileTypeEnum = fileTypeEnum;
        this.fileMetaData = FileMetaData.createMetaData(user, fileTypeEnum);
        this.parent = parent;
    }
    /**
     * Constructs a FileObject with the specified file name, file type, and user.
     *
     * @param fileName The name of the file or directory.
     * @param fileTypeEnum The type of the file (regular file or directory).
     * @param user     The user associated with the file.
     */
    protected FileObject(String fileName, FileTypeEnum fileTypeEnum, User user) {
        this.fileName = fileName;
        this.fileTypeEnum = fileTypeEnum;
        this.fileMetaData = FileMetaData.createMetaData(user, fileTypeEnum);
    }

    /**
     * Gets the name of the file or directory.
     *
     * @return The name of the file or directory.
     */
    public String getName(){
        return fileName;
    }

    /**
     * Gets the type of the file (regular file or directory).
     *
     * @return The type of the file.
     */
    public FileTypeEnum getFileType(){
        return fileTypeEnum;
    }

    /**
     * Gets the metadata associated with the file.
     *
     * @return The metadata of the file.
     */
    public FileMetaData getFileMetaData(){
        return fileMetaData;
    }

    /**
     * Gets the parent directory of the file (null if the file is in the root directory).
     *
     * @return The parent directory of the file.
     */
    public Directory getParent(){
        // TODO HANDLE IF NO PARENT EXISTS, LIKE FOR ROOT DIRECTORY
        return parent;
    }

}
