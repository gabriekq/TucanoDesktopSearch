package com.mendonca.Index;

import com.mendonca.utils.Constants;
import com.mendonca.utils.GuiUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class IndexHandler {

    private HashMap<String, LinkedList<String>> indexAllSubFolders;

    private TextField folderField;

    private Label statusLabel;


    public IndexHandler(HashMap<String, ? super Parent> guiElements) {
        indexAllSubFolders = new HashMap<>();
        this.folderField = GuiUtils.parseMapValues("folderField",guiElements);
        this.statusLabel = GuiUtils.parseMapValues("statusLabel",guiElements);
    }

    private void addFolderMap(File file){
     LinkedList<String> listPaths;
     String filePath = file.getPath();
     String fileNameFistLetter = String.valueOf(file.getName().toUpperCase().charAt(0));

     if(indexAllSubFolders.containsKey(fileNameFistLetter)){
        listPaths  = indexAllSubFolders.get(fileNameFistLetter);
        listPaths.add(filePath);
      }else{
        listPaths = new LinkedList<>();
        listPaths.add(filePath);
        indexAllSubFolders.put(fileNameFistLetter,listPaths);
     }

    }


    public Index createIndex(){
    String dirName =this.folderField.getText();
    retrieveAllSubDirectories(dirName);
    Index index = new Index(this.indexAllSubFolders,dirName);
    this.indexAllSubFolders.clear();
    Platform.runLater(() -> {this.statusLabel.setText(Constants.STATUS+Constants.OPERATION_INDEXING+"Done");});

    return index;
}

    public Index createIndex(String dirName){
        retrieveAllSubDirectories(dirName);
        Index index = new Index(this.indexAllSubFolders,dirName);
        this.indexAllSubFolders.clear();
        Platform.runLater(() -> {this.statusLabel.setText(Constants.STATUS+Constants.OPERATION_INDEXING+"Done");});

        return index;
    }

private void retrieveAllSubDirectories(String rootDirectory) {

    File fileRoot = new File(rootDirectory);
    File[] files = fileRoot.listFiles();

    if ( (files != null)   &&  (files.length != 0)) {

        for(int indexPosition=0;indexPosition<files.length;indexPosition=indexPosition+1) {

            File file = files[indexPosition];

            if (file.isDirectory()) {

                String directoryPath= file.getPath();
                addFolderMap(file);

                Platform.runLater(() -> {
                    this.statusLabel.setText(Constants.STATUS+Constants.OPERATION_INDEXING+file.getName());
                });
                retrieveAllSubDirectories(directoryPath);
            }
        }

    }

 }

    public static void narrowIndex(Index index, String rootPath) {

        ArrayList<String> keysSubPaths = new ArrayList<>(index.getAllSubFolders().keySet());

        for(int indexPosition=0;indexPosition<keysSubPaths.size();indexPosition=indexPosition+1){

            String key = keysSubPaths.get(indexPosition);

            boolean containsPath = index.getAllSubFolders().get(key).stream().anyMatch(path -> path.contains(rootPath));

            if(containsPath){
                index.getAllSubFolders().get(key).removeIf(path-> !path.contains(rootPath));
            }else{
                index.getAllSubFolders().remove(key);
            }
        }
        index.setRootPath(rootPath);
        keysSubPaths.clear();
    }


}
