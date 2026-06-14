package com.mendonca.gui;

import com.mendonca.Index.Index;
import com.mendonca.Index.IndexHandler;
import com.mendonca.file.FileHandler;
import com.mendonca.search.SearchHandler;
import com.mendonca.utils.GuiUtils;
import com.mendonca.utils.ThreadUtils;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import static com.mendonca.utils.GuiUtils.blockStopButton;
import static com.mendonca.utils.GuiUtils.unblockFieldsOnStop;


public class IndexSearchHandler {

private IndexHandler indexHandler;

private FileHandler fileHandler;

private SearchHandler searchHandler;

private LinkedList<RadioButton> radioButtons;

private Label statusLabel;

private HashMap<String, ? super Parent> guiElements;

private LinkedList<Thread> threadsList;

private Thread thread;

    public IndexSearchHandler(HashMap<String, ? super Parent> guiElements) {
        this.indexHandler = new IndexHandler(guiElements);
        this.fileHandler = new FileHandler(guiElements);
        this.searchHandler = new SearchHandler(guiElements);
        this.radioButtons = GuiUtils.parseMapValues("groupRadio",guiElements);
        this.statusLabel= GuiUtils.parseMapValues("statusLabel",guiElements);
        this.guiElements=guiElements;
        this.threadsList= new LinkedList<>();
    }

    public int execute(){

        String radioSelected = Objects.requireNonNull(this.radioButtons)
                               .stream()
                               .filter(radio -> radio.isSelected())
                               .findFirst()
                               .get()
                               .getId();

        switch (radioSelected) {
            case "radioCreateIndex" -> {
                Runnable indexRunnable = () -> {
                    if(!this.fileHandler.isIndexExists()) {
                        Index index = indexHandler.createIndex();
                        this.fileHandler.saveIndexFile(index);
                    }

                    // desbloqueia a tela
                    unblockFieldsOnStop(this.guiElements);
                    blockStopButton(this.guiElements);
                    // bloqueia o stop
                };
                this.thread = ThreadUtils.createThread("index", indexRunnable);
                this.threadsList.add(thread);
            }
            case "radioSearch" -> {
                // Runnable sera criado aqui para que tela nao trave
                Runnable search = () -> {
                    Optional<Index> indexOptional = this.fileHandler.loadIndexFile();
                    this.searchHandler.clearSearchResults();
                    if (indexOptional.isPresent()) {
                        Index indexLoad = indexOptional.get();
                        this.searchHandler.search(indexLoad);
                    }else{
                        Index index = indexHandler.createIndex();
                        this.searchHandler.search(index);
                    }

                    unblockFieldsOnStop(this.guiElements);
                    blockStopButton(this.guiElements);
                };
                this.thread = ThreadUtils.createThread("search", search);
                this.threadsList.add(this.thread);
            }

           case "radioRebuildIndex"->{
             // manter stop bloqueado aqui porque o usuario nao pode interromper a operacao senao ferra tudo

             Runnable radioRebuildIndex = () ->{ // passar todos os Runnable para dentro de metodos nessa classe mesmo e chamar o metodo aqui
               ArrayList<String> filesNamesList= this.fileHandler.getIndexFilesNames();
                this.fileHandler.recreateFileTracker();
                this.fileHandler.removeAllIndexFiles();

                for(int ind=0;ind<filesNamesList.size();ind=ind+1){

                    String indexFileName = filesNamesList.get(ind);
                    Index index= this.indexHandler.createIndex(indexFileName); // criar um runnable aqui e adicionar na lista
                    this.fileHandler.saveIndexFile(index);

                }

                 unblockFieldsOnStop(this.guiElements);
                 blockStopButton(this.guiElements);
             };

             this.thread = ThreadUtils.createThread("radioRebuildIndex",radioRebuildIndex);
             this.threadsList.add(this.thread);


            }


        }

    ThreadUtils.startThreadsList(this.threadsList);
    return 0;
}


public void stopThreads(){
        // enviar stop para a tela
        this.searchHandler.stopSubThreads();
        ThreadUtils.stopThreadsList(this.threadsList);
}


}
