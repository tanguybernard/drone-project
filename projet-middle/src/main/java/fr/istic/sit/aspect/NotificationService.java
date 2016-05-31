package fr.istic.sit.aspect;

import fr.istic.sit.annotation.ParamNotification;
import fr.istic.sit.service.NotificationSenderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author FireDroneTeam
 */

@Aspect
@Component
public class NotificationService {

    @Autowired
    NotificationSenderService sender;

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @After("@annotation(fr.istic.sit.annotation.Notification)")
    public void notificationServiceAction(JoinPoint joinPoint) throws Exception {
        log.info("Sending notification .......");

        Arrays.asList(joinPoint.getArgs()).forEach(System.out::println);

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        String id = "";
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof ParamNotification))
                    continue;
                id = (String)args[argIndex];
            }
        }

        sender.sendSynchroNotification(id);
    }
}
