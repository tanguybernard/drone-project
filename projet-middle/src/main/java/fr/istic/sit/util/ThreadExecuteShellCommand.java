package fr.istic.sit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by fracma on 6/2/16.
 */
public class ThreadExecuteShellCommand extends Thread {
    private String command;

    private final Logger log = LoggerFactory.getLogger(ThreadExecuteShellCommand.class);


    public ThreadExecuteShellCommand(String str, String command) {
        super(str);
        this.command = command;
    }

    public void run() {
        log.info("ThreadExecuteShellCommandThreadExecuteShellCommandThreadExecuteShellCommandThreadExecuteShellCommand ");
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            //p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            String line = "";

            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }



            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Here is the standard error of the command (if any):\n");
            while ((line = stdError.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Exception while executing :  " + command);
            e.printStackTrace();
        }
        log.info(output.toString());
    }
}