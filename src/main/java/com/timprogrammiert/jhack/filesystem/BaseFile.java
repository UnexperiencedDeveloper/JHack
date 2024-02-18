package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.permissions.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class BaseFile {
    private static Logger logger = LoggerFactory.getLogger(BaseFile.class);
    Directory parent;
    String name;
    Permission permission;
    protected BaseFile(String name, Directory parent, Permission permission){
        this.parent = parent;
        this.name = name;
        this.permission = permission;
        if(this.parent != null){
            parent.addChild(this);
            logger.debug(String.format("%s added to %s", name, parent.name));
        }
    }

    public Directory getParent(){
        return parent;
    }
    public String getName(){
        return name;
    }
    public Permission getPermission(){
        return permission;
    }

}
