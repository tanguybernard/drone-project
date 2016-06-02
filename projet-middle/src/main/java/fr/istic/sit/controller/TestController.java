package fr.istic.sit.controller;

import fr.istic.sit.service.DroneService;
import fr.istic.sit.service.NotificationSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

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
    public String drone() {
        log.info("sadkjshfhdsfgdgjfdgjfdgjfdjgf");
        droneService.createDrone("3", "48.1493215", "-1.772861");
        return "jsahdjashdjk";
    }

    @RequestMapping("")
    public String hello() {
        return "hola";
    }



}
