package com.timprogrammiert.filesystem;

/**
 * @author tmatz
 * @version 1.0
 */
public class FileSize {
    private boolean sizeCalcualated;
    private int size;
    private FileSizeCalculater fileSizeCalculater;
    private FileObject fileObject;

    public FileSize(FileObject fileObject){
        this.fileObject = fileObject;
        fileSizeCalculater = new FileSizeCalculater();
    }

    public int getFileSize(){
        if (!sizeCalcualated) {
            calculateSize();
        }
        return size;
    }

    private void calculateSize(){
        size = fileSizeCalculater.calculateSize(fileObject);
        sizeCalcualated = true;
    }

    public void registerFileChange(){
        sizeCalcualated = false;
    }
}
