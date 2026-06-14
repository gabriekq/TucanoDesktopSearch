package com.mendonca.utils;

import java.util.LinkedList;
import java.util.List;

public class ThreadUtils {

    public static void cleanUpSubThreadsDone(LinkedList<Thread> threadsListSearch){

        do{
            try {
                threadsListSearch.removeIf(thread -> thread.getState().name().contains(Thread.State.TERMINATED.name()));
                Thread.sleep(500);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while (threadsListSearch.size()!=0);

    }

    public static void startThreadsList(List<Thread> threadsList){

        for(int indexTreads=0;indexTreads< threadsList.size();indexTreads=indexTreads+1){

            if(threadsList.get(indexTreads).getState().name().equals(Thread.State.NEW.name())){
                threadsList.get(indexTreads).start();
            }
        }

    }

    public static void stopThreadsList(List<Thread> threadsList){

        if(threadsList.size()>0) {
            for (Thread thread : threadsList) {
                thread.stop();
            }
            threadsList.clear();
        }
    }


    public static  Thread createThread(String nameThread, Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.setName(nameThread);
        thread.setDaemon(true);
        return thread;
    }

}
