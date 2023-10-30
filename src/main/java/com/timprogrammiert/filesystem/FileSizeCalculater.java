package com.timprogrammiert.filesystem;

import com.timprogrammiert.filesystem.directory.Directory;
import com.timprogrammiert.util.FileTypeEnum;

import java.util.Random;

/**
 * The `FileSizeCalculater` class calculates and returns a simulated size for a
 *  given `FileObject`. It uses a base size for files and directories and adds
 *  random variation to simulate real-world size differences. The `calculateSize`
 *  method calculates the size for a given `FileObject` by considering its type
 *  (file or directory) and, in the case of directories, summing the sizes of
 *  its children.
 *
 * @author tmatz
 * @version 1.0
 */
public class FileSizeCalculater {
    private int getRandomSize(int baseSize){
        Random random = new Random();
        double variation = random.nextDouble() * 0.5 + 0.5;
        return (int) (baseSize * variation);
    }

    /**
     * Calculates the simulated size for the given `FileObject`.
     *
     * @param fileObject The `FileObject` for which the size is to be calculated.
     * @return The calculated size of the `FileObject`.
     */
    public int calculateSize(FileObject fileObject){
        int baseSizeFile = 50;
        int baseSizeDirectory = 4096;

        boolean isDirectory = fileObject.getFileType().equals(FileTypeEnum.Directory);
        int baseSize = isDirectory ? baseSizeDirectory : baseSizeFile;
        int  objectSize = getRandomSize(baseSize);
        if (isDirectory) {
            int totalSize = objectSize;
            Directory directoryObject = (Directory) fileObject;

            for (FileObject children : directoryObject.getAllChildren()) {
                totalSize += children.getFileMetaData().getFileSize().getFileSize();
            }
            return totalSize;
        } else {
            // Object is File or Exectuable
            return objectSize;
        }
    }

}
