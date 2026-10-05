package com.mendonca.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandPowerShell {

    private String command = "powershell.exe Invoke-Item -LiteralPath ";

    private Process powerShellProcess ;


    public void openExplorer(String directory)  {

        String fullCommand = command+"'"+directory+"'";
        try {

            this.powerShellProcess = Runtime.getRuntime().exec(fullCommand);
            this.powerShellProcess.getOutputStream().close();

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.powerShellProcess.getInputStream()));
            do{
                line =reader.readLine();
                if(line!=null) {
                System.out.println(line);
                }
            }while (line !=null);

            reader.close();
        }catch (IOException exception) {
            System.out.println(exception.getCause().getMessage());
        }

    }



}
