package com.mendonca.gui;

import com.mendonca.tucanodesktopsearch.ControllerInitializableGUI;
import com.mendonca.search.FoundItem;
import com.mendonca.utils.GuiUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

import static com.mendonca.tucanodesktopsearch.HelloApplication.startScene;
import static com.mendonca.utils.GuiUtils.*;


public class MainInterfaceController implements ControllerInitializableGUI {



    private TreeMap<String, ? super Parent> guiElements;

    private IndexSearchFileHandler indexSearchFileHandler;

    @FXML
    private Button buttonExecute;

    @FXML
    private Button buttonStop;

    @FXML
    private Button buttonFolderSelector;

    @FXML
    private Label statusLabel;

    @FXML
    private Group groupRadio;

    @FXML
    private TextField fileNameField;

    @FXML
    private TextField folderField;

    @FXML
    private TableView<FoundItem>  tableView;

    @FXML
    public TableColumn<FoundItem,String> directoryCol;
    @FXML
    public TableColumn<FoundItem,String> foundFileCol;

    public MainInterfaceController() {
     this.guiElements   = new TreeMap<>();

    }
    @Override
    public void initScreenComponents(){

        this.guiElements.put("folderField",folderField);
        this.guiElements.put("fileNameField",fileNameField);
        this.guiElements.put("groupRadio",groupRadio);
        this.guiElements.put("statusLabel",statusLabel);
        this.guiElements.put("buttonExecute",buttonExecute);
        this.guiElements.put("buttonStop",buttonStop);
        this.guiElements.put("buttonFolderSelector",buttonFolderSelector);
        blockStopButton(this.guiElements);

        this.directoryCol.setCellValueFactory(new PropertyValueFactory<>("foundDirectory"));
        this.foundFileCol.setCellValueFactory(new PropertyValueFactory<>("foundFile"));

        this.guiElements.put("tableView",tableView);

        LinkedList<RadioButton> radios = GuiUtils.parseMapValues("groupRadio",guiElements);
        radios.get(0).setSelected(true);

        this.indexSearchFileHandler = new IndexSearchFileHandler(this.guiElements);

    }

    public void radioSelected(ActionEvent event ){
         RadioButton radioButton=  ((RadioButton)event.getTarget());
         String idValue=  radioButton.getId();

         if(!radioButton.isSelected()){
             radioButton.setSelected(true);
         }
         unSelectDifferentRadioButton(idValue,groupRadio);
     }

      public void unSelectDifferentRadioButton(String buttonKeep,Group groupRadio){

          for( Node radio : groupRadio.getChildren()){

              if(!buttonKeep.equals(radio.getId())){
                  RadioButton radioButton = (RadioButton)radio;
                  radioButton.setSelected(false);
              }
          }
      }

      public void executeTask(){

        if(GuiUtils.isValidInputUser(this.guiElements)){
            blockFieldsOnExecution(this.guiElements);
             this.indexSearchFileHandler.stopThreads();
             this.indexSearchFileHandler.execute();
            unblockStopButton(this.guiElements);
        }

      }

    public void stopTask() {
        blockStopButton(this.guiElements);
        this.indexSearchFileHandler.stopThreads();
        unblockFieldsOnStop(this.guiElements);
    }


    public void openFolderSelectorMenu(){

        try {
            startScene("tucanoFolderExplorer.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public  void setSearchPathFolderExplorer(String folderPath){
        this.folderField.setText(folderPath);
    }
}
