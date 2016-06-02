package fr.istic.sit.controller;

import fr.istic.sit.service.DroneService;
import fr.istic.sit.service.NotificationSenderService;
import fr.istic.sit.util.ExecuteShellComand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fracma on 4/19/16.
 */
@Controller
@RequestMapping("/hello")
public class TestController {

    @Autowired
    DroneService droneService;

    @Autowired
    NotificationSenderService notificationSenderService;

    @Autowired
    ExecuteShellComand executeShellComand;

    private final Logger log = LoggerFactory.getLogger(TestController.class);


    @RequestMapping("/push")
    public String sendPushNotif() throws Exception {
    /*    String regs[]={"4031ece9c2426321b15720"};

        // Set payload (any object, it will be serialized to JSON)
        Map<String, String> payload = new HashMap<String, String>();

        // Add test message
        payload.put("message", "INTERVENTION");
*/

/*
        PushyPushRequest req = new PushyPushRequest(payload, regs);

        PushyAPI.sendPush(req);
*/

        notificationSenderService.sendNotification("TEST","TEST");
        return "ok";
    }

    @RequestMapping("/drone")
    public @ResponseBody String drone(@RequestParam(value = "command" , required = false) String command) {
        log.info("Drone");
        executeShellComand.executeCommand(command);
        return "Ok Drone 1";
    }

    @RequestMapping("/drone2")
    public @ResponseBody String drone2(@RequestParam(value = "command" , required = false) String command) {
        log.info("Drone");
        executeShellComand.executeCommand2(command);
        return "Ok Drone 2";
    }

    @RequestMapping("/drone3")
    public @ResponseBody String drone3(@RequestParam(value = "command" , required = false) String command) {
        log.info("Drone");
        executeShellComand.executeCommandStart(command,"1   ", "48.1493215", "-1.772861");
        return "Ok Drone 3";
    }

    @RequestMapping("/drone4")
    public @ResponseBody String drone4() {
        log.info("Drone");
        executeShellComand.executeCommandMove("./move_drone_segment.sh","127.0.0.1:14551", "'((48.151669721619704,-1.7680541798472404),(48.14909260304147,-1.7660800740122795),(48.147417551969944,-1.7695562168955803),(48.14701667693945,-1.773805171251297))'", "1", "571f7731b760adc0c3bec8fb");
        return "Ok Drone4";
    }
    @RequestMapping("/drone5")
    public @ResponseBody String drone5() {
        log.info("Drone");
        executeShellComand.executeCommandMove("./move_drone_segment.sh","127.0.0.1:14551", "\'((48.151669721619704,-1.7680541798472404),(48.14909260304147,-1.7660800740122795),(48.147417551969944,-1.7695562168955803),(48.14701667693945,-1.773805171251297))\'", "1", "571f7731b760adc0c3bec8fb");
        return "Ok Drone 5";
    }

    @RequestMapping("")
    public String hello() {
        return "hola";
    }



}
