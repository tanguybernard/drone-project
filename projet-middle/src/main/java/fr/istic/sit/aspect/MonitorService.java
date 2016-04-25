package fr.istic.sit.aspect;

import fr.istic.sit.dao.ActionRepository;
import fr.istic.sit.domain.Action;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FireDroneTeam
 */

@Aspect
@Component
public class MonitorService {

    @Autowired
    ActionRepository actionRepository;

    private final Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Before("execution(* fr.istic.sit.controller.*Controller.*(..))")
    public void logServiceAccess(JoinPoint joinPoint) {

        log.info("------------------------------------------------");
        log.info("Start: ["+ joinPoint.getSignature()+"]");
        //Arrays.asList(joinPoint.getArgs()).forEach(System.out::println);
        log.info("------------------------------------------------");
    }

    @After("execution(* fr.istic.sit.controller.*Controller.*(..))")
    public void logServiceAction(JoinPoint joinPoint) {
        String login= "Anonymous";
        OAuth2Authentication authentication = null;
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        log.info("------------------------------------------------");
        log.info("End: ["+ joinPoint.getSignature()+"]");

        //Print arguments
        Arrays.asList(joinPoint.getArgs()).forEach(System.out::println);

        List<Object> auths = Arrays.asList(joinPoint.getArgs())
                                .stream()
                                .filter(arg -> arg instanceof OAuth2Authentication)
                                .collect(Collectors.toList());

        List<String> parameters = new ArrayList<>();
        Arrays.asList(joinPoint.getArgs())
                .stream()
                .filter(arg -> {
                    return ( !(arg instanceof OAuth2Authentication) && arg != null );
                })
                .forEach(param->
                        parameters.add(param.toString())
                );

        if(!auths.isEmpty()){
            authentication = (OAuth2Authentication)auths.get(0);
            login = ((User)authentication.getPrincipal()).getUsername();
        }

        String date = dt.format(new Date());
        //Action
        Action action = new Action(date, login, joinPoint.getSignature().getName(), parameters);

        actionRepository.save(action);

        log.info("Date: ["+ new Date() + "] Utilisateur: ["
                + login + "] Action: ["+ joinPoint.getSignature()
                + "] Parametres: " );
        parameters.forEach(log::info);

        log.info("------------------------------------------------");
    }

}
