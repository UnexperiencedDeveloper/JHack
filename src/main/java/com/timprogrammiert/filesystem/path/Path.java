package com.timprogrammiert.filesystem.path;

import com.timprogrammiert.filesystem.FileObject;
import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.exceptions.FileObjectNotFoundException;
import com.timprogrammiert.host.Host;
import com.timprogrammiert.util.FileTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The {@code Path} class represents a file system path and provides utility methods for working with paths.
 * It can convert paths to arrays, resolve paths in a simulated file system, and obtain absolute paths based
 * on a FileObject's parent hierarchy.
 *
 * @author tmatz
 * @version 1.0
 */
public class Path {
    public static String EnvironmentVariable = "/bin:/usr/bin";
    private String pathString;

    /**
     * Constructs a Path object with the specified path string.
     *
     * @param pathString The path string to represent.
     */
    public Path(String pathString) {
        this.pathString = pathString;
    }

    /**
     * Get the absolute path of a FileObject based on its parent hierarchy.
     *
     * @param fileObject The FileObject for which to determine the absolute path.
     * @return The absolute path as a string.
     */
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

    /**
     * Resolve a path in a simulated file system, returning a FileObject of a specified child type.
     *
     * @param host      The host environment containing the file system.
     * @param childType The class type of the desired child object.
     * @param <T>       A type parameter representing the child type.
     * @return A FileObject of the specified child type representing the resolved path.
     */
    public <T extends FileObject> T resolvePath(Host host, Class<T> childType) throws FileObjectNotFoundException {

        try {
            // Absolute Path will start with "/", relative Path not
            // If its absolute, then start Object is rootDirectory
            FileObject recursiveObject = (getPathToArray().get(0).equals("/")) ? host.getRootDirectory() : host.getCurrentDirectory();
            for (String fileObjectName : getPathToArray()) {
                if (fileObjectName.equals("/")) continue;
                if(recursiveObject.getFileType().equals(FileTypeEnum.Directory)){
                    recursiveObject = ((Directory) recursiveObject).getChildrenByName(fileObjectName);
                }
            }
            return childType.cast(recursiveObject);
        }catch (NullPointerException e){
            throw new FileObjectNotFoundException(e.getMessage());
        }catch (ClassCastException e){
            throw new FileObjectNotFoundException(e.getMessage() + " ClassCastException occured - Path.java");
        }

    }

    /**
     * Convert the path string to an array of path components.
     *
     * @return A list of path components as strings.
     */
    private List<String> getPathToArray(){
        List<String> pathList;
        if(pathString.equals("/")){
            pathList = new ArrayList<>();
            pathList.add("/");
        }else {
            String[] pathAsArray = pathString.split("/");
            pathList = new ArrayList<>(Arrays.asList(pathAsArray));
            // When calling absolute Path like "/etc" theres an empty element at index 0
            if(pathList.get(0).isEmpty()) pathList.set(0, "/");
        }
        return pathList;
    }
}
