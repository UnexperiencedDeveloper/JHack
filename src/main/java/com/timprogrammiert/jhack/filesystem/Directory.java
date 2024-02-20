package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.permissions.Permission;

import java.util.*;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class Directory extends BaseFile{
    private final Map<String, BaseFile> children;
    public Directory(String name, Directory parent, Permission permission) {
        super(name, parent, permission);
        children = new HashMap<>();
        this.metaData = new MetaData();
    }
    public void addChild(BaseFile file){
        children.put(file.name, file);
        int childFileSize = file.getMetaData().getFileSizeObject().getFileSize();
        int newSize = getMetaData().getFileSizeObject().getFileSize() + childFileSize;
        getMetaData().getFileSizeObject().setFileSize(newSize);
        updateFileSize();
    }

    public void removeChild(String childToRemove){
        children.remove(childToRemove);
        updateFileSize();
    }
    public BaseFile getChildByName(String name){
        return children.get(name);
    }

    public Set<String> getAllFileNames(){
        return children.keySet();
    }
    public Map<String,BaseFile> getChildMap(){
        return children;
    }

    public Collection<BaseFile> getChildrenRecursive() {
        Collection<BaseFile> recursiveFiles = new ArrayList<>();
        // Add the current directory itself to the collection
        recursiveFiles.add(this);

        for (BaseFile baseFile : children.values()) {
            if (baseFile instanceof Directory directoryObject) {
                // Recursively add child files of the subdirectory
                recursiveFiles.addAll(directoryObject.getChildrenRecursive());
            } else {
                // Add non-directory files to the collection
                recursiveFiles.add(baseFile);
            }
        }
        return recursiveFiles;
    }

    public boolean hasChild(String childName){
        return getChildMap().containsKey(childName);
    }

    /**
     * Recursively updates the file size of the current folder and its parent folders
     */
    private void updateFileSize(){
        int newFileSize = 0;
        getMetaData().updateModified();
        // Calculate the total file size by summing up the sizes of all child objects
        for (BaseFile child : getChildMap().values()){
            newFileSize += child.getMetaData().getFileSizeObject().getFileSize();
        }
        getMetaData().getFileSizeObject().setFileSize(newFileSize);

        // Propagate the file size update to the parent folder, if applicable
        if(getParent() != null){
            getParent().updateFileSize();
        }
    }
}
