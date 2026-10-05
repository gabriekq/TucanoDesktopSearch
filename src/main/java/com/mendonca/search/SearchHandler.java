package com.mendonca.search;


import com.mendonca.Index.Index;
import com.mendonca.utils.CommandPowerShell;
import com.mendonca.utils.Constants;
import com.mendonca.utils.FileUtils;
import com.mendonca.utils.GuiUtils;
import com.mendonca.utils.ThreadUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;


public class SearchHandler {

    private TableView<FoundItem> tableView;

    private TextField fileNameField;

    private Label statusLabel;

    private LinkedBlockingQueue<FoundItem> foundItems;

    private LinkedList<Thread> threadsListSearch;

    private CommandPowerShell command;

    public SearchHandler(TreeMap<String, ? super Parent> guiElements) {
      this.tableView = GuiUtils.parseMapValues("tableView",guiElements);
      this.fileNameField = GuiUtils.parseMapValues("fileNameField",guiElements);
      this.statusLabel=GuiUtils.parseMapValues("statusLabel",guiElements);
      this.foundItems=new LinkedBlockingQueue<>();
      this.threadsListSearch = new LinkedList<>();
      this.command = new CommandPowerShell();
    }

    public void search(Index index){

        if(!FileUtils.isEmptyFolder(index.getRootPath())){
            String fileToSearch = fileNameField.getText().toLowerCase();
            this.searchFile(index.getRootPath(),fileToSearch);
        }


       HashMap<String, LinkedList<String>> allSubDirectories = index.getAllSubFolders();

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

            File[] filesSearch = files.listFiles();
            for (int indexPosition = 0; indexPosition < filesSearch.length; indexPosition = indexPosition + 1) {

                File file = filesSearch[indexPosition];

                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.contains(fileToSearch)) {
                        FoundItem  foundItem= new FoundItem(directory,fileName);
                        this.addFoundItems(foundItem);
                    }
                }
            }

    }

    public void clickOpenExplorerEvent(MouseEvent mouseEvent){
       Button  button = (Button) mouseEvent.getSource();
       int idButton = Integer.parseInt(button.getId());
       FoundItem foundItem = this.tableView.getItems().get(idButton);
       String directory = foundItem.getFoundDirectory();
       command.openExplorer(directory);

    }

    private void addFoundItems(FoundItem foundItem){

        Button button = new Button();
        EventHandler<MouseEvent> clickOpenExplorerEvent = this::clickOpenExplorerEvent;
        button.setOnMouseClicked(clickOpenExplorerEvent);
        button.setText("Folder");
        String currentId= String.valueOf(foundItems.size())  ;
        button.setId(currentId);
        foundItem.setButtonExplorer(button);

        this.foundItems.add(foundItem);

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
