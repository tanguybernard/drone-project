package fr.istic.sit.util;

import org.apache.tomcat.util.file.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            log.error("* * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            log.error("Exception while executing :  " + command);
            e.printStackTrace();
        }

        return output.toString();

    }
}