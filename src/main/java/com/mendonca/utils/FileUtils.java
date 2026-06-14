package com.mendonca.utils;

import java.io.File;
import java.util.Arrays;

public class FileUtils {

    public static boolean directoryExists(String path){
        File file = new File(path);
        return  file.exists();
    }

    public static boolean isEmptyFolder(String path){

        File file = new File(path);
        File[] files=  file.listFiles();
        boolean emptyFolder=true;

       if(files != null) {
           emptyFolder = Arrays.stream(files).noneMatch(File::isFile);
       }
        return emptyFolder;
    }



}
