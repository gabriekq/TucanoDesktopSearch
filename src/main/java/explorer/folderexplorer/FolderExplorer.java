package explorer.folderexplorer;

import com.mendonca.search.FoundItem;
import com.mendonca.utils.Constants;
import com.mendonca.utils.GuiUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.TreeMap;

public class FolderExplorer {

    private TreeMap<String,? super Parent> guiElementsFolderExplorer;

    private TreeMap<String, EventHandler<? extends Event>> guiEvents;

    private MenuButton menuButtonSelectDriver;

    private Button buttonUpFolder;

    private TextField textFieldCurrentPath;



    public FolderExplorer(TreeMap<String,? super Parent> guiElementsFolderExplorer, TreeMap<String, EventHandler<? extends Event>> guiEvents) {
        this.guiElementsFolderExplorer = guiElementsFolderExplorer;
        this.guiEvents=guiEvents;
        this.menuButtonSelectDriver = GuiUtils.parseMapValues("menuButtonSelectDriver",this.guiElementsFolderExplorer);
        this.buttonUpFolder =  GuiUtils.parseMapValues("buttonUpFolder",this.guiElementsFolderExplorer);
        this.textFieldCurrentPath=GuiUtils.parseMapValues("textFieldCurrentPath",this.guiElementsFolderExplorer);
    }

    public void initMenuButton(){

        EventHandler<ActionEvent> eventHandler = (EventHandler<ActionEvent>) this.guiEvents.get("driverMenuSelected");

        File[] driveLetters = File.listRoots();

        for(int index=0;index< driveLetters.length;index=index+1){
          String driverLetter =driveLetters[index].getPath();

            MenuItem menuItem = new MenuItem(driverLetter);
            menuItem.addEventHandler(ActionEvent.ACTION,eventHandler);
            menuButtonSelectDriver.getItems().add(menuItem);
        }

    }

    public void loadTableView(String path,String tableViewKey){

        LinkedList<FoundItem> foldersList = new LinkedList<>();
        TableView<FoundItem> tableView = GuiUtils.parseMapValues(tableViewKey,this.guiElementsFolderExplorer);
        File  file =  new File(path);

        if(file.listFiles()!=null){
            File[] files = file.listFiles();
            for(int index=0;index<files.length;index=index+1){

                if(isFolderValid(files[index])){
                    String folderName = files[index].getName();
                    String folderFullPath = files[index].getAbsolutePath();
                    FoundItem rowValue = new FoundItem(folderFullPath,folderName);
                    foldersList.add(rowValue);
                    tableView.setRowFactory(tableViewFac -> {
                        TableRow<FoundItem> row = new TableRow<FoundItem>();
                        EventHandler<MouseEvent>  tableView1RowClicked = (EventHandler<MouseEvent>) this.guiEvents.get(tableViewKey);
                        row.setOnMouseClicked(tableView1RowClicked);
                        return row;
                    });
                }

            }

            Platform.runLater(()->{
                tableView.setItems(FXCollections.observableArrayList(foldersList));
            });
        }
    }

    public void writeCurrentPath(String path){
        TextField textFieldCurrentPath = (TextField) this.guiElementsFolderExplorer.get("textFieldCurrentPath");
            textFieldCurrentPath.setText(path);
    }


    private boolean isFolderValid(File file){

        if(file.isDirectory() && !file.getName().contains(Constants.EXCLUDED_FOLDER_NAME_1) && !file.getName().startsWith(Constants.EXCLUDED_FOLDER_NAME_BEGIN) ){
            return true;
        }else{
            return false;
        }
    }

    public void clearTable(String tableViewKey){
        TableView<FoundItem> tableView = GuiUtils.parseMapValues(tableViewKey,this.guiElementsFolderExplorer);

            LinkedList<FoundItem> foldersList = new LinkedList<>();
            Platform.runLater(()->{
                tableView.setItems(FXCollections.observableArrayList(foldersList));
            });

    }

    public boolean hasFolders(String directoryPath){
        File file = new File(directoryPath);

        if(file.listFiles()!= null){
            File files[] = file.listFiles();

            for(int index=0;index<files.length;index++){

               File filePosition = files[index];
               if(isFolderValid(filePosition)){
                  return true;
               }
            }
        }
        return false;
    }

    public void upFolderPath(){
       String currentPath = this.textFieldCurrentPath.getText();

       if(currentPath.split("[\\\\]").length>2){

           int lastSlash= currentPath.lastIndexOf("\\");
           currentPath=currentPath.substring(0,lastSlash);
           textFieldCurrentPath.setText(currentPath);
       }else{
               int lastSlash= currentPath.lastIndexOf("\\");
               currentPath = currentPath.substring(0,lastSlash+1);
               textFieldCurrentPath.setText(currentPath);
       }
    }

    public void blockUpFolderButton(){
        this.buttonUpFolder.setDisable(true);
    }

    public void unBlockUpFolderButton(){
        this.buttonUpFolder.setDisable(false);
    }

    public boolean canUpFolderLevel(){

        if(this.textFieldCurrentPath.getText().length()<=3){
            return false;
        }else{
            return true;
        }

    }


}
