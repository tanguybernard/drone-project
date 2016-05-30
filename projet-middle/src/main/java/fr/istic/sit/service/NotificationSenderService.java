package fr.istic.sit.service;

import fr.istic.sit.notification.PushyAPI;
import fr.istic.sit.notification.PushyPushRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FireDroneTeam
 */
@Component
public class NotificationSenderService {

    private final Logger log = LoggerFactory.getLogger(NotificationSenderService.class);
    private final String regs[]={"4031ece9c2426321b15720", "ddd7380886807cc49bdafa", "6747dd6d4880861ab2b486"};


    @Autowired
    PushyAPI pushyAPI;


    public void sendSynchroNotification(String message) throws Exception {
        // Set payload (any object, it will be serialized to JSON)
        Map<String, String> payload = new HashMap<String, String>();

        // Add test message
        payload.put("idIntervention", message);
        log.info("sendSynchroNotification --> payload "+payload);
        send(payload);
    }

    public void sendNotification(String name, String message) throws Exception {
        Map<String, String> payload = new HashMap<String, String>();
        payload.put(name, message);
        log.info("SendNotification --> payload "+payload);
        send(payload);
    }

    public void sendNotification(Map<String, String> payload) throws Exception {
        log.info("SendNotification --> payload "+payload);
        send(payload);
    }

    private void send(Map<String, String> payload) throws Exception {
        PushyPushRequest req = new PushyPushRequest(payload, regs);
        pushyAPI.sendPush(req);
    }

}
