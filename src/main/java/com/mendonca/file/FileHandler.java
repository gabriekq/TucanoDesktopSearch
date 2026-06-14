package com.mendonca.file;

import com.mendonca.Index.Index;
import com.mendonca.Index.IndexHandler;
import com.mendonca.utils.Constants;
import com.mendonca.utils.GuiUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public class FileHandler {

    private ObjectOutputStream output;

    private ObjectInputStream input;

    private Label statusLabel;

    private TextField folderField;

    private FileTracker fileTracker;

    private Index index;



    public FileHandler(HashMap<String, ? super Parent> guiElements) {

        this.statusLabel = GuiUtils.parseMapValues("statusLabel",guiElements);
        this.folderField = GuiUtils.parseMapValues("folderField",guiElements);
        this.initializeFilesNamesSaved();

    }

    private void initializeFilesNamesSaved() {

        File trackerDir = new File(Constants.PATH_TRACKER_FILE);
        File[] trackers = trackerDir.listFiles();

        boolean fileExists = Arrays.stream(Objects.requireNonNull(trackers))
                .filter(File::isFile)
                .anyMatch(tracker -> tracker.getName().contains(Constants.FILE_NAME_TRACKER));

        if(!fileExists){

         this.removeAllIndexFiles();
         TreeMap<String,String> filesNamesSaved = new TreeMap<>();
         FileTracker fileTracker =  new FileTracker(filesNamesSaved);
         this.openFile(Constants.FILE_NAME_TRACKER,Constants.PATH_TRACKER_FILE);
         this.saveFile(fileTracker);
         this.fileTracker =fileTracker;

        }else{

         this.loadFile(Constants.FILE_NAME_TRACKER,Constants.PATH_TRACKER_FILE);
         this.fileTracker = this.readFile();

        }
        this.closeFile();
    }

    public void removeAllIndexFiles(){
        // implement remove
        File filesRemove = new File(Constants.PATH_INDEX_FILE);

        if(filesRemove.listFiles()!=null && Objects.requireNonNull(filesRemove.listFiles()).length>0 ){
            File[] listFilesRemove =  filesRemove.listFiles();
            Arrays.stream(listFilesRemove).filter(File::isFile).forEach(File::delete);
        }
        Platform.runLater(() -> {statusLabel.setText(Constants.STATUS + "removeAllIndexFiles Done!!");});
    }

    public ArrayList<String> getIndexFilesNames(){

        ArrayList<String> indexFilesNames =  new ArrayList<>(this.fileTracker.getFilesNamesSaved().keySet());
        Platform.runLater(() -> {statusLabel.setText(Constants.STATUS + "Retrieve IndexFilesNames Done!!");});
        return indexFilesNames;
    }

    public void recreateFileTracker(){

        this.openFile(Constants.FILE_NAME_TRACKER,Constants.PATH_TRACKER_FILE);
        this.fileTracker.getFilesNamesSaved().clear();
        this.saveFile(fileTracker);
        this.closeFile();
        Platform.runLater(() -> {statusLabel.setText(Constants.STATUS + "recreateFileTracker Done!!");});
    }

    private <T> T readFile() {

        T objectRead=null;

        try {
            objectRead = (T) input.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println(ioException.getMessage());
        }

        return objectRead;
    }

    private void loadFile(String fileName,String pathFolder) {

        String file = pathFolder+fileName;
        try {

            this.input = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    public boolean isIndexExists(){

        String folderFieldText =this.folderField.getText();
       if(this.fileTracker.getFilesNamesSaved().containsKey(folderFieldText)){
           Platform.runLater(() -> {statusLabel.setText(Constants.STATUS + Constants.OPERATION_FILE_EXISTS);});
           return true;
       }else{
           return false;
       }
    }

    public void saveIndexFile(Index index){

        String indexFileNameHash =index.getRootPath().hashCode()+".ser" ;
        Platform.runLater(() -> {statusLabel.setText(Constants.STATUS + Constants.OPERATION_FILE_SAVE + indexFileNameHash);});
        this.openFile(indexFileNameHash,Constants.PATH_INDEX_FILE);
        this.saveFile(index);
        setIndex(index);
        this.closeFile();

        fileTracker.getFilesNamesSaved().put(index.getRootPath(),indexFileNameHash);
        this.openFile(Constants.FILE_NAME_TRACKER,Constants.PATH_TRACKER_FILE);
        this.saveFile(fileTracker);
        this.closeFile();

        Platform.runLater(() -> {statusLabel.setText(Constants.STATUS +"File Saved !!! "+indexFileNameHash);});
    }

    public Optional<Index> loadIndexFile(){
        String rootPath = this.folderField.getText();
        Index index;

        if( getIndex() != null && getIndex().getRootPath().equals(rootPath)  ){
            return Optional.of(getIndex());

        }else {

            String actualOrClosestFileName = mappingIndexFileName(rootPath);
            if (actualOrClosestFileName != null) {
                this.loadFile(actualOrClosestFileName, Constants.PATH_INDEX_FILE);
                index = this.readFile();
                this.closeFile();
                setIndex(index);

                if (!index.getRootPath().equals(rootPath)) {
                    // set index variable
                    IndexHandler.narrowIndex(index, rootPath);
                    return  Optional.of(index);
                } else {
                    // set index variable
                    return Optional.of(index);
                }
            }

            return Optional.empty();
        }
    }

    private  String mappingIndexFileName(String rootPath) {  // return the closest index if path aaa/abc does not exist so return de add/

        int numberElement=0;
        String regexSplitValue="[\\\\]";
        String[] rootPathSplit = rootPath.split(regexSplitValue);

        for(int index=rootPathSplit.length-1;index>0;index--) {

            if(fileTracker.getFilesNamesSaved().containsKey(rootPath)){
                return fileTracker.getFilesNamesSaved().get(rootPath);
            }

            numberElement = rootPathSplit[index].length()+1;
            rootPath = rootPath.substring(0,rootPath.length()-numberElement);
        }
         return null;
    }


    private void openFile(String fileName,String pathFolder){

        String fileNamePath = pathFolder+fileName;
        try {
           this.output = new ObjectOutputStream(new FileOutputStream(fileNamePath));
        } catch (IOException ioException) {
           System.out.println(ioException.getMessage());
        }
    }

    private <T> void saveFile(T SerializableObject){
        try {
            output.writeObject(SerializableObject);
            output.flush();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

   public void closeFile(){

        try {
            if (this.output != null) {
                this.output.close();
            }

            if(this.input != null){
                this.input.close();
            }


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }

   }

    private Index getIndex() {
        return index;
    }

    private void setIndex(Index index) {
        this.index = index;
    }
}
