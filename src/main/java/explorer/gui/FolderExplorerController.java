package explorer.gui;

import com.mendonca.search.FoundItem;
import com.mendonca.tucanodesktopsearch.ControllerInitializableGUI;
import explorer.folderexplorer.FolderExplorerHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.util.TreeMap;

import static com.mendonca.tucanodesktopsearch.HelloApplication.sendResultMainController;
import static com.mendonca.tucanodesktopsearch.HelloApplication.startScene;

public class FolderExplorerController  implements ControllerInitializableGUI {

    @FXML
    private TextField textFieldCurrentPath;

    @FXML
    private MenuButton menuButtonSelectDriver;

    @FXML
    private Button buttonApplyPath;

    @FXML
    private Button buttonUpFolder;

    @FXML
    private TableView<FoundItem> tableView1;
    @FXML
    private TableView<FoundItem> tableView2;

    @FXML
    private TableColumn<FoundItem,String> tableView1ColumnFolder;

    @FXML
    private TableColumn<FoundItem,String> tableView2ColumnFolder;


    private TreeMap<String,? super Parent> guiElementsFolderExplorer;

    private TreeMap<String,EventHandler<? extends Event>> guiEvents;

    private FolderExplorerHandler folderExplorerHandler;


    @Override
    public void initScreenComponents() {

        this.guiElementsFolderExplorer = new TreeMap<>();

        this.guiElementsFolderExplorer.put("textFieldCurrentPath",this.textFieldCurrentPath);
        this.guiElementsFolderExplorer.put("menuButtonSelectDriver",this.menuButtonSelectDriver);
        this.guiElementsFolderExplorer.put("buttonApplyPath",this.buttonApplyPath);
        this.guiElementsFolderExplorer.put("buttonUpFolder",this.buttonUpFolder);

        this.tableView1ColumnFolder.setCellValueFactory(new PropertyValueFactory<>("foundFile"));
        this.tableView2ColumnFolder.setCellValueFactory(new PropertyValueFactory<>("foundFile"));

        this.guiElementsFolderExplorer.put("tableView1",this.tableView1);
        this.guiElementsFolderExplorer.put("tableView2",this.tableView2);

        this.guiEvents = new TreeMap<>();
        EventHandler<ActionEvent> driverMenuSelected = this::driverMenuSelected;
        EventHandler<MouseEvent> tableView1RowClicked = this::tableView1RowClicked;
        EventHandler<MouseEvent> tableView2RowClicked = this::tableView2RowClicked;

        this.guiEvents.put("driverMenuSelected",driverMenuSelected);
        this.guiEvents.put("tableView1",tableView1RowClicked);
        this.guiEvents.put("tableView2",tableView2RowClicked);

        this.folderExplorerHandler = new FolderExplorerHandler(this.guiElementsFolderExplorer,this.guiEvents);
        this.folderExplorerHandler.loadMenuButton();
        this.folderExplorerHandler.blockUpFolderLevelButton();

    }

   public void  driverMenuSelected(ActionEvent actionEvent){

       MenuItem menuItem = (MenuItem)  actionEvent.getTarget();
       String menuItemText = menuItem.getText();
       menuButtonSelectDriver.setText(menuItemText);
       this.folderExplorerHandler.clearTableView("tableView2");
       this.folderExplorerHandler.fetchListFolderName(menuItemText,"tableView1");
       this.folderExplorerHandler.setCurrentPathDisplay(menuItemText);
       this.folderExplorerHandler.blockUpFolderLevelButton();
   }

   public void tableView1RowClicked(MouseEvent mouseEvent){
       TableRow<FoundItem> row  = (TableRow<FoundItem>) mouseEvent.getSource();
       FoundItem foundItem = this.tableView1.getItems().get(row.getIndex());
       this.folderExplorerHandler.fetchListFolderName(foundItem.getFoundDirectory(),"tableView2");
       this.folderExplorerHandler.setCurrentPathDisplay(foundItem.getFoundDirectory());
       this.folderExplorerHandler.unblockUpFolderLevelButton();
   }

   public void tableView2RowClicked(MouseEvent mouseEvent){
       TableRow<FoundItem> row  = (TableRow<FoundItem>) mouseEvent.getSource();
       FoundItem foundItem = this.tableView2.getItems().get(row.getIndex());
       this.folderExplorerHandler.browserFolder(foundItem.getFoundDirectory());
       this.folderExplorerHandler.setCurrentPathDisplay(foundItem.getFoundDirectory());
       this.folderExplorerHandler.unblockUpFolderLevelButton();
   }

    public void goMainMenuApplyPath(){
        try {
            String pathSelected= textFieldCurrentPath.getText();
            sendResultMainController(pathSelected);
            startScene("mainSearch.fxml");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void goMainMenu(){
        try {
            startScene("mainSearch.fxml");
        }catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void upFolderLevel(){

        this.folderExplorerHandler.upFolderLevel();
        this.folderExplorerHandler.blockUpFolderLevelButton();
    }

}
