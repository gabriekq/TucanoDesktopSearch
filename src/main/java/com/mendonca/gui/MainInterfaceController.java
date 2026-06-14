package com.mendonca.gui;

import com.mendonca.search.FoundItem;
import com.mendonca.utils.Constants;
import com.mendonca.utils.GuiUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.LinkedList;

import static com.mendonca.utils.GuiUtils.*;


public class MainInterfaceController {



    private HashMap<String, ? super Parent> guiElements;

    private  IndexSearchHandler indexSearchHandler;

    @FXML
    private Button buttonExecute;

    @FXML
    private Button buttonStop;

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
     this.guiElements   = new HashMap<>();

    }

    public void initScreamComponets(){

        this.guiElements.put("folderField",folderField);
        this.guiElements.put("fileNameField",fileNameField);
        this.guiElements.put("groupRadio",groupRadio);
        this.guiElements.put("statusLabel",statusLabel);
        this.guiElements.put("buttonExecute",buttonExecute);
        this.guiElements.put("buttonStop",buttonStop);
        blockStopButton(this.guiElements);

        this.directoryCol.setCellValueFactory(new PropertyValueFactory<>("foundDirectory"));
        this.foundFileCol.setCellValueFactory(new PropertyValueFactory<>("foundFile"));

        this.guiElements.put("tableView",tableView);

        LinkedList<RadioButton> radios = GuiUtils.parseMapValues("groupRadio",guiElements);
        radios.get(0).setSelected(true);

        this.indexSearchHandler = new IndexSearchHandler(this.guiElements);

    }

    public void radioSelected(ActionEvent event ){
         RadioButton radioButton=  ((RadioButton)event.getTarget());
         String idValue=  radioButton.getId();

         if(!radioButton.isSelected()){
             radioButton.setSelected(true);
         }
         unSelectDifferentRadioButton(idValue,groupRadio);
     } // keeps the Radio buttons select in consistent way

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
            // bloqueia tela e desbloqueia stop

            blockFieldsOnExecution(this.guiElements);
             this.indexSearchHandler.stopThreads();
             this.indexSearchHandler.execute();
            unblockStopButton(this.guiElements);
        }

      }

    public void stopTask() {
       // ActionEvent event
          // bloqueia stop e desbloqueia tela;
        blockStopButton(this.guiElements);
        this.indexSearchHandler.stopThreads();
        unblockFieldsOnStop(this.guiElements);


    }
}
