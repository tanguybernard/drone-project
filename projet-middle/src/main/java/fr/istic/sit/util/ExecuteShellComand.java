package fr.istic.sit.util;

import org.apache.tomcat.util.file.Matcher;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * @author FireDroneTeam
 */

@Component
public class ExecuteShellComand {
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
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}
