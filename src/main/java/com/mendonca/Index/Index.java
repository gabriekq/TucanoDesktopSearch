package com.mendonca.Index;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class Index implements Serializable {

    private String rootPath;

    private HashMap<String, LinkedList<String>> allSubFolders;

    public Index(HashMap<String, LinkedList<String>> allSubFolders,String rootPath ) {

        this.allSubFolders = new HashMap<>(allSubFolders);
        this.rootPath= rootPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public HashMap<String, LinkedList<String>> getAllSubFolders() {
        return allSubFolders;
    }

    public void setAllSubFolders(HashMap<String, LinkedList<String>> allSubFolders) {
        this.allSubFolders = allSubFolders;
    }
}
