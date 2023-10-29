package com.timprogrammiert.filesystem.regularFile;

import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.user.User;
import com.timprogrammiert.util.FileTypeEnum;

/**
 * @author tmatz
 */
public class RegularFile extends FileObject {
    private String fileContent = "";

    public static RegularFile createRegularFile(String fileName, FileTypeEnum fileTypeEnum, User user, Directory parent){
        return new RegularFile(fileName, fileTypeEnum, user, parent);
    }
    private RegularFile(String fileName, FileTypeEnum fileTypeEnum, User user, Directory parent) {
        super(fileName, fileTypeEnum, user, parent);
    }
    public void setContent(String content){
        fileContent = content;
    }
    public void appendContent(String content){
        fileContent += content;
    }

    public String getContent(){
        return fileContent;
    }
}
