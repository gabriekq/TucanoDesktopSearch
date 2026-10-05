package com.mendonca.search;

import javafx.scene.control.Button;

public class FoundItem {

    private String foundFile;
    private String foundDirectory;

    private Button buttonExplorer;

    public FoundItem(String foundDirectory, String foundFile) {
        this.foundFile = foundFile;
        this.foundDirectory = foundDirectory;
    }

    public String getFoundFile() {
        return foundFile;
    }

    public void setFoundFile(String foundFile) {
        this.foundFile = foundFile;
    }

    public String getFoundDirectory() {
        return foundDirectory;
    }

    public void setFoundDirectory(String foundDirectory) {
        this.foundDirectory = foundDirectory;
    }

    public Button getButtonExplorer() {
        return buttonExplorer;
    }

    public void setButtonExplorer(Button buttonExplorer) {
        this.buttonExplorer = buttonExplorer;
    }
}
