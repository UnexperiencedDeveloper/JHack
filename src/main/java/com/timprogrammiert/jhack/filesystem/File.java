package com.timprogrammiert.jhack.filesystem;

import com.timprogrammiert.jhack.permissions.Permission;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class File extends BaseFile{
    String content;
    public File(String name, Directory parent, Permission permission) {
        super(name, parent, permission);
        this.content = "";
    }
    public void appendString(String stringToAppend){
        content += (stringToAppend + "\n");
    }

    public String getContent(){
        return content;
    }
}
