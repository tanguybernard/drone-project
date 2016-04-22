package fr.istic.sit.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author fracma
 */

@Aspect
@Component
public class MonitorService {

    private final Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Before("execution(* fr.istic.sit.controller.*Controller.*(..))")
    public void logServiceAccess(JoinPoint joinPoint) {

        log.info("------------------------------------------------");
        log.info("Start: ["+ joinPoint.getSignature()+"]");
        Arrays.asList(joinPoint.getArgs()).forEach(System.out::println);
        log.info("------------------------------------------------");
        //System.out.println("S: " + joinPoint);
    }

}
