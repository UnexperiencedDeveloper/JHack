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
    MetaData metaData;
    protected BaseFile(String name, Directory parent, Permission permission){
        this.parent = parent;
        this.name = name;
        this.permission = permission;
        this.metaData = new MetaData();
        if(this.parent != null){
            parent.addChild(this);
            logger.debug(String.format("%s added to %s", name, parent.name));
        }
    }

    public Directory getParent(){
        return parent;
    }
    public void setParentFolder(Directory parent){this.parent = parent;}

    public String getName(){
        return name;
    }
    public void setName(String newName){
        // .remove returns the removed Object, which is in this case the Object we want to set a new Key
        parent.getChildMap().put(newName, parent.getChildMap().remove(name));
        name = newName;
    }
    public Permission getPermission(){
        return permission;
    }

    public MetaData getMetaData(){
        return metaData;
    }

}
