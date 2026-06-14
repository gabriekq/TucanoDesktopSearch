package com.mendonca.file;

import java.io.Serializable;
import java.util.TreeMap;

public class FileTracker implements Serializable {

    private TreeMap<String,String> filesNamesSaved;

    public FileTracker(TreeMap<String,String> filesNamesSaved) {
        this.filesNamesSaved=filesNamesSaved;
    }

    public TreeMap<String, String> getFilesNamesSaved() {
        return filesNamesSaved;
    }

    public void setFilesNamesSaved(TreeMap<String, String> filesNamesSaved) {
        this.filesNamesSaved = filesNamesSaved;
    }
}
