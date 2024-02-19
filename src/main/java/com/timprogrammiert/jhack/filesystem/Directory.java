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
    }
    public void addChild(BaseFile file){
        children.put(file.name, file);
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
}
