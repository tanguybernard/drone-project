package fr.istic.sit.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.tomcat.util.file.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author FireDroneTeam
 */

@Component
public class ExecuteShellComand {
    private final Logger log = LoggerFactory.getLogger(ExecuteShellComand.class);


    private static final String IPADDRESS_PATTERN = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
            + "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
            + "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
            + "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])";

    private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
    private static Matcher matcher;

    public String executeCommand(String command) {

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

        return output.toString();

    }

    public String executeCommand2(String command){
       // String line = "AcroRd32.exe /p /h " + file.getAbsolutePath();
        try {
            log.info("executeCommand2.....");
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            log.info("AVANT ------executeCommand2.....");

            int exitValue = executor.execute(cmdLine);
            return "Response "+exitValue;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "XXXX";
    }

    public String executeCommandStart(String command, String id, String lat, String lon){
        try {
            log.info("Starting executeCommandStart..."+command);
            ProcessBuilder pb = new ProcessBuilder(command, id, lat, lon);
            Process p = pb.start();
            log.info("End executeCommandStart...");
        }
        catch(IOException e) {
            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Exception while executing :  " + command);
            e.printStackTrace();
        }
        return "OK executeCommandStart";
    }

    public String executeCommandStop(String command, String ip){
        try {
            log.info("Starting executeCommandStop..."+command);
            ProcessBuilder pb = new ProcessBuilder(command, ip);
            Process p = pb.start();
            log.info("End executeCommandStop...");
        }
        catch(IOException e) {
            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Exception while executing :  " + command);
            e.printStackTrace();
        }
        return "OK executeCommandStop";

    }

    public String executeCommandMove( String command, String cmdArgs_connect, String cmdArgs_mission, String cmdArgs_idDrone, String cmdArgs_idIntervention ){
        try {
            log.info("Starting executeCommandMove..."+command);
            ProcessBuilder pb = new ProcessBuilder(command, cmdArgs_connect, cmdArgs_mission, cmdArgs_idDrone, cmdArgs_idIntervention);
            Process p = pb.start();
            log.info("End executeCommandMove...");
        }
        catch(IOException e) {
            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Exception while executing :  " + command);
            e.printStackTrace();
        }
        return "OK executeCommandMove";

    }
/*
    public String executeCommandVar(String ... param){
        try {
            log.info("Starting executeCommandVar...");
            ProcessBuilder pb = new ProcessBuilder(param);
            Process p = pb.start();
            log.info("End executeCommandStart...");
        }
        catch(IOException e) {
            log.info("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.info("Exception while executing :  " + param);
            e.printStackTrace();
        }
        return "OK executeCommandStart";

    }*/


}

