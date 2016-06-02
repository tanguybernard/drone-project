package fr.istic.sit.service;

import fr.istic.sit.domain.Drone;
import fr.istic.sit.model.ActionMissionDrone;
import fr.istic.sit.model.MissionDrone;
import fr.istic.sit.model.ModeMissionDrone;
import fr.istic.sit.util.ExecuteShellComand;
import fr.istic.sit.util.ThreadExecuteShellCommand;
import org.apache.xerces.util.SymbolTable;
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


    /**
     *
     * @param id
     * @param latitude
     * @param longitude
     */
    public void createDrone(String id, String latitude, String longitude){
        log.info("Creating drone....");
        //String command = "./create-drone.sh " + id + " " + latitude + " " + longitude;

        executeShellComand.executeCommandStart("ls","-la","","");

        String command = "./create-drone.sh";
        String response = executeShellComand.executeCommandStart(command, id, latitude, longitude);

        //String commandCreateDrone = "python create_drone.py --instance " + id + " --latitude " + latitude + " --longitude " + longitude + " &" ;
        //String response = executeShellComand.executeCommand2(command);

        log.info("Response ----> create-drone.sh  " + response);

        /*
        python create_drone.py --instance $1 --latitude $2 --longitude $3 &
        ./create-mavproxy.sh $1
        */

       /* try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/

        //String commandMavProxy = "./create-mavproxy.sh " + id ;
       // response = executeShellComand.executeCommand(commandMavProxy);
       // log.info("Response ----> create-mavproxy.sh "+ response);
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
    public void actionNO(ActionMissionDrone actionMissionDrone){
        log.info("Action on drone....");

        String command = new String();
        String response = "";

        if(actionMissionDrone.isStart()) {
            String cmdArgs_connect ;
            String cmdArgs_mission ;
            String cmdArgs_idDrone ;
            String cmdArgs_idIntervention ;
            Drone drone = actionMissionDrone.getDrone();

            switch (ModeMissionDrone.getModeFromString(actionMissionDrone.getMission().getMode())) {

                case LOOP:
                    //command += "python move_drone_boucle.py ";
                    command += "./move_drone_boucle.sh  ";
                    break;

                case SEGMENT:
                    //command += "./move_drone_segment.py ";
                    command += "./move_drone_segment.sh  ";
                    break;

                case ZONE:
                    break;

            }
          /*  cmdArgs_connect = "--connect " + drone.getIp()+":"+drone.getPort() + " ";
            cmdArgs_mission = "--mission " + actionMissionDrone.toJson() + " ";
            cmdArgs_idDrone = "--idDrone " + drone.getId() + " ";
            cmdArgs_idIntervention = "--idIntervention " + actionMissionDrone.getIdIntervention() ;*/


            cmdArgs_connect = drone.getIp()+":"+drone.getPort() + " ";
            cmdArgs_mission = actionMissionDrone.stringCommand() + " ";
            cmdArgs_idDrone = drone.getId() + " ";
            cmdArgs_idIntervention = actionMissionDrone.getIdIntervention() ;

            command += cmdArgs_connect;
            command += cmdArgs_mission;
            command += cmdArgs_idDrone;
            command += cmdArgs_idIntervention;

            log.info("- - -> Requete : "+ command);

            new ThreadExecuteShellCommand(actionMissionDrone.getMission().getMode(),command).start();
            /*response = executeShellComand.executeCommand(command);
            log.info("Response "+ response);
            */
            log.info("ThreadExecuteShellCommand APRES LANCER !!!!");
        }
        //** ACTION == STOP **//
        else {

            //command += "python stop_drone.py --connect " + actionMissionDrone.getDrone().getIp()+":"+actionMissionDrone.getDrone().getPort() ;
            //command += "./stop_drone.sh " + actionMissionDrone.getDrone().getIp()+":"+actionMissionDrone.getDrone().getPort() ;
            command = "./stop_drone.sh";
            log.info("- - -> Requete : "+ command);
            response = executeShellComand.executeCommandStop(command, actionMissionDrone.getDrone().getIp()+":"+actionMissionDrone.getDrone().getPort());
           // response = executeShellComand.executeCommand(command);
        }
        log.info("Response "+ response);
    }

    public void action(ActionMissionDrone actionMissionDrone){
        log.info("Action on drone MONIK....");

        String command = new String();
        String response = "";

        if(actionMissionDrone.isStart()) {
            String cmdArgs_connect ;
            String cmdArgs_mission ;
            String cmdArgs_idDrone ;
            String cmdArgs_idIntervention ;
            Drone drone = actionMissionDrone.getDrone();

            switch (ModeMissionDrone.getModeFromString(actionMissionDrone.getMission().getMode())) {

                case LOOP:
                    command += "python move_drone_boucle.py ";
                    break;

                case SEGMENT:
                    //command += "./move_drone_segment.py ";
                    command ="./move_drone_segment.sh";
                    break;

                case ZONE:
                    break;

            }
          /*  cmdArgs_connect = "--connect " + drone.getIp()+":"+drone.getPort() + " ";
            cmdArgs_mission = "--mission " + actionMissionDrone.toJson() + " ";
            cmdArgs_idDrone = "--idDrone " + drone.getId() + " ";
            cmdArgs_idIntervention = "--idIntervention " + actionMissionDrone.getIdIntervention() ;*/


         /*   cmdArgs_connect = drone.getIp()+":"+drone.getPort() + " ";
            cmdArgs_mission = actionMissionDrone.stringCommand() + " ";
            cmdArgs_idDrone = drone.getId() + " ";
            cmdArgs_idIntervention = actionMissionDrone.getIdIntervention() ;*/
            cmdArgs_connect = drone.getIp()+":"+drone.getPort();
            cmdArgs_mission = actionMissionDrone.stringCommand();
            cmdArgs_idDrone = drone.getId();
            cmdArgs_idIntervention = actionMissionDrone.getIdIntervention();


           /* command += cmdArgs_connect;
            command += cmdArgs_mission;
            command += cmdArgs_idDrone;
            command += cmdArgs_idIntervention;*/

            log.info("- - -> Requete : "+ command+""+cmdArgs_connect+""+cmdArgs_mission+""+cmdArgs_idDrone+""+ cmdArgs_idIntervention);
            //response = executeShellComand.executeCommand(command);
            response = executeShellComand.executeCommandMove(command,cmdArgs_connect,cmdArgs_mission,cmdArgs_idDrone, cmdArgs_idIntervention );
            log.info("Response "+ response);

        }
        //** ACTION == STOP **//
        else {

            command += "python stop_drone.py --connect " + actionMissionDrone.getDrone().getIp()+":"+actionMissionDrone.getDrone().getPort() ;
            log.info("- - -> Requete : "+ command);
            response = executeShellComand.executeCommand(command);
        }
        log.info("Response "+ response);
    }



    public void stopDrone(){
        log.debug("Stopping drone....");
        String response = executeShellComand.executeCommand("./create-drone.sh 1 48.115127 -1.637972");
        log.info("Response "+ response);
    }

    /**
     *      return "ActionMissionDrone{" +
        "idIntervention='" + idIntervention + '\'' +
     ", drone=" + drone +
     ", mission=" + mission +
     ", start=" + start +
     '}';
     */
}
