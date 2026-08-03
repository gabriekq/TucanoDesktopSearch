package explorer.folderexplorer;

import com.mendonca.utils.GuiUtils;
import com.mendonca.utils.ThreadUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.TreeMap;

public class FolderExplorerHandler  {


    private FolderExplorer folderExplorer;

    private TreeMap<String,? super Parent> guiElementsFolderExplorer;

    TreeMap<String, EventHandler<? extends Event>> guiEvents;

    private LinkedList<Thread> threadListFolderExplorer;

    public FolderExplorerHandler(TreeMap<String,? super Parent> guiElementsFolderExplorer, TreeMap<String, EventHandler<? extends Event>> guiEvents) {
        this.guiElementsFolderExplorer = guiElementsFolderExplorer;
        this.guiEvents = guiEvents;
        this.folderExplorer= new FolderExplorer(this.guiElementsFolderExplorer,this.guiEvents );
        this.threadListFolderExplorer = new LinkedList<>();
    }

    public void loadMenuButton(){
        this.folderExplorer.initMenuButton();
    }

    public void fetchListFolderName(String pathLoad, String tableName){


      ThreadUtils.stopThreadsList(this.threadListFolderExplorer);

      Runnable loadTableView = () -> {
        this.folderExplorer.clearTable(tableName);
        this.folderExplorer.loadTableView(pathLoad,tableName);

      };
      Thread threadLoadTableView = ThreadUtils.createThread("fetchListFolderName",loadTableView);
      this.threadListFolderExplorer.add(threadLoadTableView);
      ThreadUtils.startThreadsList(this.threadListFolderExplorer);

    }

   public void clearTableView(String tableName){
        this.folderExplorer.clearTable(tableName);
   }

public void browserFolder(String pathLoad){

       boolean hasFolders= this.folderExplorer.hasFolders(pathLoad);
       if(hasFolders){
           this.clearTableView("tableView2");
           this.folderExplorer.loadTableView(pathLoad,"tableView1");
       }
}

public void setCurrentPathDisplay(String path){
    this.folderExplorer.writeCurrentPath(path);
}

 public void upFolderLevel(){

        if(this.folderExplorer.canUpFolderLevel()){
            this.folderExplorer.upFolderPath();
            this.clearTableView("tableView2");
            TextField textFieldCurrentPath = GuiUtils.parseMapValues("textFieldCurrentPath", this.guiElementsFolderExplorer);
            String text = textFieldCurrentPath.getText();
            this.clearTableView("tableView2");
            this.folderExplorer.loadTableView(text, "tableView1");
        }

 }


 public void blockUpFolderLevelButton(){
        if(!this.folderExplorer.canUpFolderLevel()){
            this.folderExplorer.blockUpFolderButton();
        }
 }

 public void unblockUpFolderLevelButton(){
     if(this.folderExplorer.canUpFolderLevel()){
         this.folderExplorer.unBlockUpFolderButton();
     }
 }



}
