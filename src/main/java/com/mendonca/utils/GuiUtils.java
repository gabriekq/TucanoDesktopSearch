package com.mendonca.utils;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;


public class GuiUtils {



    public static <T> T parseMapValues(String key, HashMap<String, ? super Parent> guiElements ){

        Class<?> cls  = guiElements.get(key).getClass();

        if(cls==Group.class){
            LinkedList<RadioButton> radioButtons = new LinkedList<>();
            Group groupRadio = (Group) guiElements.get(key);

            for(Node node :groupRadio.getChildren()){
                 RadioButton radioButton = (RadioButton) node;
                 radioButtons.add(radioButton);
            }
            return (T)  radioButtons;
        }

        if(guiElements.containsKey(key)){
             return (T) guiElements.get(key);
        }else{
            return null;
        }

    }


     public static boolean isValidInputUser(HashMap<String, ? super Parent> guiElements){

          TextField folderField=parseMapValues("folderField",guiElements);
          TextField fileNameField=parseMapValues("fileNameField",guiElements);
          LinkedList<RadioButton> radioButtons = parseMapValues("groupRadio",guiElements);

         Label statusLabel = parseMapValues("statusLabel",guiElements);

          boolean folderFieldIsBlank = folderField.getText().isBlank();
          boolean fileNameFieldIsBlank = fileNameField.getText().isBlank();

          boolean folderFieldIsEmpty  = folderField.getText().isEmpty();
          boolean fileNameFieldIsEmpty  = fileNameField.getText().isEmpty();


        String radioButtonId = Objects.requireNonNull(radioButtons)
                .stream()
                .filter(radio-> radio.isSelected())
                .findFirst()
                .get()
                .getId();

        if(radioButtonId.equals("radioCreateIndex") && (folderFieldIsBlank || folderFieldIsEmpty)  ){
            statusLabel.setText(Constants.STATUS+Constants.FOLDER_PATH_ERROR);
            return false;
        }

        if(radioButtonId.equals("radioSearch") && ((folderFieldIsBlank || folderFieldIsEmpty) || (fileNameFieldIsBlank || fileNameFieldIsEmpty ))  ){
            statusLabel.setText(Constants.STATUS + Constants.FOLDER_FILE_ERROR);
            return false;
        }

         statusLabel.setText(Constants.STATUS);
         return true;
     }


     public static void  blockFieldsOnExecution(HashMap<String, ? super Parent> guiElements){
         TextField folderField=parseMapValues("folderField",guiElements);
         TextField fileNameField=parseMapValues("fileNameField",guiElements);
         LinkedList<RadioButton> radios = GuiUtils.parseMapValues("groupRadio",guiElements);
         Button buttonExecute = parseMapValues("buttonExecute",guiElements);

         folderField.setEditable(false);
         fileNameField.setEditable(false);
         radios.forEach(radio->radio.setDisable(true));
         buttonExecute.setDisable(true);
     }

     public static void unblockFieldsOnStop(HashMap<String, ? super Parent> guiElements){

         TextField folderField=parseMapValues("folderField",guiElements);
         TextField fileNameField=parseMapValues("fileNameField",guiElements);
         LinkedList<RadioButton> radios = GuiUtils.parseMapValues("groupRadio",guiElements);
         Button buttonExecute = parseMapValues("buttonExecute",guiElements);

         folderField.setEditable(true);
         fileNameField.setEditable(true);
         radios.forEach(radio->radio.setDisable(false));
         buttonExecute.setDisable(false);
     }

     public static void blockStopButton(HashMap<String, ? super Parent> guiElements){
         Button buttonStop =  parseMapValues("buttonStop",guiElements);
         buttonStop.setDisable(true);
     }

     public static void unblockStopButton(HashMap<String, ? super Parent> guiElements){
         Button buttonStop =  parseMapValues("buttonStop",guiElements);
         buttonStop.setDisable(false);
     }


}
