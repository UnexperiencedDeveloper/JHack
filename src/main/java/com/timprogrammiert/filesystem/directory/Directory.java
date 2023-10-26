package com.timprogrammiert.filesystem.directory;

import com.timprogrammiert.filesystem.FileMetaData;
import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tmatz
 */
public class Directory extends FileObject {
    private final Map<String, FileObject> children;

    public static Directory createRootDirectory(String fileName, FileType fileType, User user){
        return new Directory(fileName, fileType, user);
    }

    public static Directory createDirectory(String fileName, FileType fileType, User user, Directory parent){
        return new Directory(fileName, fileType, user, parent);
    }
    private Directory(String fileName, FileType fileType, User user, Directory parent) {
        super(fileName, fileType, user, parent);
        this.children = new HashMap<>();
    }
    private Directory(String fileName, FileType fileType, User user) {
        super(fileName, fileType, user);
        this.children = new HashMap<>();
    }

    public FileObject getChildrenByName(String childrenName) throws NullPointerException{
        if(children.get(childrenName) == null){
            throw new NullPointerException(String.format("cannot access '%s': no such file or directory", childrenName));
        }
        return children.get(childrenName);
    }

    public Collection<FileObject> getAllChildren(){
        return children.values();
    }

    public boolean addNewChildren(FileObject childrenObject){
        // TODO EXCEPTION IF CHILDREN ALREADY EXISTS
        if(children.containsKey(childrenObject.getName())){
            return false;
        }else {
            children.put(childrenObject.getName(), childrenObject);
            return true;
        }

    }
}
