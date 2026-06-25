package com.mendonca.search;


import com.mendonca.Index.Index;
import com.mendonca.utils.Constants;
import com.mendonca.utils.FileUtils;
import com.mendonca.utils.GuiUtils;
import com.mendonca.utils.ThreadUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;


public class SearchHandler {

    private TableView<FoundItem> tableView;

    private TextField fileNameField;

    private Label statusLabel;

    private LinkedBlockingQueue<FoundItem> foundItems;

    private LinkedList<Thread> threadsListSearch;

    public SearchHandler(HashMap<String, ? super Parent> guiElements) {
      this.tableView = GuiUtils.parseMapValues("tableView",guiElements);
      this.fileNameField = GuiUtils.parseMapValues("fileNameField",guiElements);
      this.statusLabel=GuiUtils.parseMapValues("statusLabel",guiElements);
      this.foundItems=new LinkedBlockingQueue<>();
      this.threadsListSearch = new LinkedList<>();

    }

    public void search(Index index){

        // Criar pesquisa para a pasta corrent somente
        if(!FileUtils.isEmptyFolder(index.getRootPath())){
            String fileToSearch = fileNameField.getText().toLowerCase();
            this.searchFile(index.getRootPath(),fileToSearch);
        }


       HashMap<String, LinkedList<String>> allSubDirectories = index.getAllSubFolders(); //

       this.searchAllFolders(allSubDirectories);
        Platform.runLater(()->{
            this.statusLabel.setText(Constants.OPERATION_SEARCHING+"Done !!!");
        });

    }

    private void searchAllFolders(HashMap<String, LinkedList<String>> allSubDirectories){

        for(String key  :allSubDirectories.keySet()){

            Runnable subSearch = () -> {
                LinkedList<String>  listFolders  = allSubDirectories.get(key);
                this.searchAtList(listFolders);
            };
             Thread subThread=ThreadUtils.createThread("subSearch"+key,subSearch);
            this.threadsListSearch.add(subThread);
        }
        ThreadUtils.startThreadsList(this.threadsListSearch);
        ThreadUtils.cleanUpSubThreadsDone(this.threadsListSearch);

    }

    private void searchAtList(LinkedList<String>  listFolders ){

        String fileToSearch = fileNameField.getText().toLowerCase();

        for(int indexPosition=0; indexPosition<listFolders.size();indexPosition=indexPosition+1 ){

            String directory= listFolders.get(indexPosition);

            if(directory==null){
                System.out.println(indexPosition);
            }

          if(FileUtils.directoryExists(directory) && !FileUtils.isEmptyFolder(directory) ){
              this.searchFile(directory,fileToSearch);
          }

        }


    }

    private void searchFile(String directory, String fileToSearch){
        File files = new File(directory);

        String directoryName = directory.split("[\\\\]")[directory.split("[\\\\]").length-1];
        Platform.runLater(()->{
        this.statusLabel.setText(Constants.OPERATION_SEARCHING+directoryName);
        });

      //  if(files.listFiles()!=null) {
            File[] filesSearch = files.listFiles();
            for (int indexPosition = 0; indexPosition < filesSearch.length; indexPosition = indexPosition + 1) {

                File file = filesSearch[indexPosition];

                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.contains(fileToSearch)) {
                        this.addFoundItems(directory, fileName);
                    }
                }
            }
      //  }


    }

    private void addFoundItems(String pathDirectory, String fileFound){

        this.foundItems.add(new FoundItem(pathDirectory,fileFound));

        Platform.runLater(()->
        this.tableView.setItems(FXCollections.observableArrayList(this.foundItems))
        );

    }

    public void clearSearchResults(){

        if(this.foundItems.size() >0){

            this.foundItems.clear();
            Platform.runLater(()->
                    this.tableView.setItems(FXCollections.observableArrayList(this.foundItems))
            );
        }

    }

    public void stopSubThreads() {
        ThreadUtils.stopThreadsList(this.threadsListSearch);
    }
}
