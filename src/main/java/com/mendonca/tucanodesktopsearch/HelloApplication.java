package com.mendonca.tucanodesktopsearch;

import com.mendonca.gui.MainInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.TreeMap;

public class HelloApplication extends Application {

    private static Stage stageAux;

    private static TreeMap<String,Scene> scenes = new TreeMap<>();

    private static MainInterfaceController mainInterfaceController;

    @Override
    public void start(Stage stage) throws IOException {
        stageAux = stage;
        startScene("mainSearch.fxml");

    }

    public static void sendResultMainController(String folderSearchPath){
      mainInterfaceController.setSearchPathFolderExplorer(folderSearchPath);
    }

    public static void startScene(String fxmlFile) throws IOException {
        String windowName = fxmlFile.split("[.]")[0];
        if(scenes.containsKey(windowName)){
            Scene scene = scenes.get(windowName);
            stageAux.setTitle(windowName);
            stageAux.setScene(scene);
        }else {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent parent = fxmlLoader.load();
            double prefWidthSize = ((Pane) parent).getPrefWidth();
            double prefHeightSize = ((Pane) parent).getPrefHeight();

            setMainInterfaceController(fxmlLoader.getController());

            ControllerInitializableGUI controller = fxmlLoader.getController();
            controller.initScreenComponents();

            Scene scene = new Scene(parent, prefWidthSize, prefHeightSize);
            stageAux.setTitle(windowName);
            stageAux.setResizable(false);
            stageAux.setScene(scene);
            stageAux.show();
            scenes.put(windowName, scene);
        }
    }

    public static <T> void setMainInterfaceController(T mainInterfaceController) {

        if(HelloApplication.mainInterfaceController == null  && mainInterfaceController.getClass().equals(MainInterfaceController.class)){
            HelloApplication.mainInterfaceController = (MainInterfaceController) mainInterfaceController;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}