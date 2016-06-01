package fr.istic.sit.service;

import fr.istic.sit.domain.Drone;
import fr.istic.sit.model.ActionMissionDrone;
import fr.istic.sit.model.MissionDrone;
import fr.istic.sit.model.ModeMissionDrone;
import fr.istic.sit.util.ExecuteShellComand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FireDroneTeam
 */

@Component
public class DroneService {

    private final Logger log = LoggerFactory.getLogger(DroneService.class);

    @Autowired
    private ExecuteShellComand executeShellComand;

    private final String TCP = "tcp";
    private final String IMAGEFOLDER = "/photo/intervention/";

    /**
     * Create a new Drone
     * @param latitude of the Drone
     * @param longitude of the Drone
     */
    public void createDrone(double latitude, double longitude){
        log.debug("Creating drone....");
        String command = "./create-drone.sh " + latitude + " " + longitude;
        String response = executeShellComand.executeCommand(command);
        log.info("Response "+ response);
    }

    /**
     *
     parser.add_argument('--connect',default='tcp:127.0.0.1:5760',
     help="vehicle connection target string. If not specified, SITL automatically started and used.")
     parser.add_argument('--mission',help='Mission')
     parser.add_argument('--idDrone',help='Identifiant du drone',required=True)
     parser.add_argument('--idIntervention',help='Identifiant de l\'intervention',required=True)
     parser.add_argument('--imageFolder', help='Dossier des images',required=True)

     */
    public void action(ActionMissionDrone actionMissionDrone){
        log.debug("Action on drone....");

        String command = new String();


        if(actionMissionDrone.isStart()) {
            String cmdArgs_connect ;
            String cmdArgs_mission ;
            String cmdArgs_idDrone ;
            String cmdArgs_idIntervention ;
            String cmdArgs_imageFolder ;
            Drone drone = actionMissionDrone.getDrone();
            String response;
            switch (ModeMissionDrone.getModeFromString(actionMissionDrone.getMission().getMode())) {

                case LOOP:
                    command += "python move_drone_boucle.py ";
                    cmdArgs_connect = "--connect " + drone.getIp()+":"+drone.getPort() + " ";
                    cmdArgs_mission = "--mission " + actionMissionDrone.getMission().toString();
                    cmdArgs_idDrone = "--idDrone " + drone.getId() ;
                    cmdArgs_idIntervention = "--idIntervention " + actionMissionDrone.getIdIntervention() ;
                    cmdArgs_imageFolder = "--imageFolder " + IMAGEFOLDER + actionMissionDrone.getIdIntervention() ;

                    command += cmdArgs_connect;
                    command += cmdArgs_mission;
                    command += cmdArgs_idDrone;
                    command += cmdArgs_idIntervention;
                    command += cmdArgs_imageFolder;

                    log.info("- - -> Requete : "+ command);
                    response = executeShellComand.executeCommand(command);
                    log.info("Response "+ response);

                    break;
                case SEGMENT:
                    command += "python move_drone_segment.py ";
                    cmdArgs_connect = "--connect " + drone.getIp()+":"+drone.getPort() + " ";
                    cmdArgs_mission = "--mission " + actionMissionDrone.getMission().toString();
                    cmdArgs_idDrone = "--idDrone " + drone.getId() ;
                    cmdArgs_idIntervention = "--idIntervention " + actionMissionDrone.getIdIntervention() ;

                    command += cmdArgs_connect;
                    command += cmdArgs_mission;
                    command += cmdArgs_idDrone;
                    command += cmdArgs_idIntervention;

                    log.info("- - -> Requete : "+ command);
                    response = executeShellComand.executeCommand(command);
                    log.info("Response "+ response);

                    break;
                case ZONE:
                    break;

            }
        }
        else {

        }


        String response = executeShellComand.executeCommand("./create-drone.sh 1 48.115127 -1.637972");
        log.info("Response "+ response);
    }

    public void stopDrone(){
        log.debug("Stopping drone....");
        String response = executeShellComand.executeCommand("./create-drone.sh 1 48.115127 -1.637972");
        log.info("Response "+ response);
    }
}
