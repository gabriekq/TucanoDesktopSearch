package com.mendonca.tucanodesktopsearch;

import com.mendonca.gui.MainInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
        stage.setTitle("Tucano-Search!");
        stage.setResizable(false);
        MainInterfaceController mainInterface = (MainInterfaceController) fxmlLoader.getController();
        mainInterface.initScreamComponets();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}