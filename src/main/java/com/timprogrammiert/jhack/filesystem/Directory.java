package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.permissions.Permission;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
}
