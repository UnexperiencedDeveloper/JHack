package com.timprogrammiert.filesystem.path;

import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.util.FileType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author tmatz
 */
public class Path {
    private String pathString;
    public Path(String pathString) {
        this.pathString = pathString;
    }

    public static String getAbsolutePathByFileObject(FileObject fileObject){
        List<String> allParents = new ArrayList<>();
        FileObject parent = fileObject;

        while(parent != null){
            allParents.add(parent.getName());
            parent = parent.getParent();
        }
        Collections.reverse(allParents);
        // root Directory is "/", so we got //tmp/... because after Directoryname theres a "/" so we delete first element
        allParents.remove(0);
        String absolutePath = String.join("/", allParents);
        return "/" + absolutePath;
    }

    public List<String> getPathToArray(){
        String[] pathAsArray = pathString.split("/");
        List<String> pathList = new ArrayList<>(Arrays.asList(pathAsArray));

        // When calling absolute Path like "/etc" theres an empty element at index 0
        // Need to set this Element to the root Directory manually
        // Wont happen when calling relative path like "test/testFile"
        if(pathList.get(0).isEmpty()) pathList.set(0, "/");
        return pathList;
    }
    public <T extends FileObject> T resolvePath(Host host, Class<T> childType){
        // Absolute Path will start with "/", relative Path not
        // If its absoute, then start Object is rootDirectory
        FileObject recursiveObject = (getPathToArray().get(0).equals("/")) ? host.getRootDirectory() : host.getCurrentDirectory();
        for (String fileObjectName : getPathToArray()) {
            if (fileObjectName.equals("/")) continue;
            if(recursiveObject.getFileType().equals(FileType.Directory)){
                // TODO add Logic if File does not exist
                recursiveObject = ((Directory) recursiveObject).getChildrenByName(fileObjectName);
            }
        }
        return childType.cast(recursiveObject);
    }
}
