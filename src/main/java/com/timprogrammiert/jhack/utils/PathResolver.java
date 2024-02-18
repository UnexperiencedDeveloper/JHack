package com.timprogrammiert.jhack.utils;

import com.timprogrammiert.jhack.exceptions.FileNotFoundException;
import com.timprogrammiert.jhack.exceptions.NotADirectoryException;
import com.timprogrammiert.jhack.filesystem.BaseFile;
import com.timprogrammiert.jhack.filesystem.Directory;
import com.timprogrammiert.jhack.filesystem.Filesystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Tim
 * Date: 17.02.2024
 * Version: 1.0
 */
public class PathResolver {
    private static Logger logger = LoggerFactory.getLogger(PathResolver.class);
    Filesystem filesystem;
    public PathResolver(Filesystem filesystem) {
        this.filesystem = filesystem;
    }

    public BaseFile resolvePath(String pathString) throws NotADirectoryException, FileNotFoundException {
        // Check if the path is global or relative and delegate to the appropriate resolver
        if (pathString.startsWith("/")) {
            return resolveGlobalPath(pathString);
        } else {
            return resolveRelativePath(pathString);
        }
    }

    private BaseFile resolveRelativePath(String pathString) throws NotADirectoryException, FileNotFoundException {
        Directory subTargetDirectory = filesystem.getCurrentDirectory();
        // Handle special cases for ".", "..", and empty paths
        if (pathString.isEmpty() || pathString.equals(".")) {
            return subTargetDirectory;
        } else if (pathString.equals("..") && subTargetDirectory.getParent() != null) {
            return subTargetDirectory.getParent();
        }

        // Split the path into individual segments
        String[] subFiles = pathString.split("/");
        //BaseFile targetFile = null;

        for (int i = 0; i < subFiles.length; i++) {
            boolean isLastSegment = (i == subFiles.length - 1);
            String subName = subFiles[i];
            boolean found = false;

            BaseFile subFile = subTargetDirectory.getChildByName(subName);
            if(subFile != null){
                // Found a Match
                if((subFile instanceof Directory)){
                    subTargetDirectory = (Directory) subFile;
                    found = true;
                    continue;
                } else if (isLastSegment) {
                    return subFile;
                }else {
                    // PathResolver can also throw NotADirectoryException
                    // It happens if a subDirectory in the Path is a File
                    // e.g /Folder/FILE/Folder/File
                    throw new NotADirectoryException(subFiles[i]);
                }
            }

            // Handle the case where the file is not found
            if (!found) {
                logger.debug("Cant find " + pathString);
                throw new FileNotFoundException(pathString);
            }
        }
        return subTargetDirectory;
    }

    private BaseFile resolveGlobalPath(String pathString){
        return null;
    }
}
