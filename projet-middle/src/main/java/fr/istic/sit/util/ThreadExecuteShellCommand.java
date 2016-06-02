package fr.istic.sit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fracma on 6/2/16.
 */
public class ThreadExecuteShellCommand extends Thread {
    private String[] command;

    private final Logger log = LoggerFactory.getLogger(ThreadExecuteShellCommand.class);


    public ThreadExecuteShellCommand(String str, String... command) {
        super(str);
        this.command = command;
    }

    public void run() {
        log.info("Start..... "+command);
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}