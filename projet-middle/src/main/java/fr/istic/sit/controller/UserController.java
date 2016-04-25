package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.User;
import fr.istic.sit.service.UserService;

/**
 * @author FireDroneTeam
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
	private UserService userService;

    @RequestMapping("")
    public List<User> users(OAuth2Authentication authentication) {
        return userService.getAll();
    }


    @RequestMapping("/{login}")
    public User searchUser(OAuth2Authentication authentication, @PathVariable String login) {
        return userService.getByLogin(login);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void insert(OAuth2Authentication authentication, @RequestBody User user){
    	userService.insert(user);
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    public void update(OAuth2Authentication authentication, @RequestBody User user){
    	userService.update(user);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void delete(OAuth2Authentication authentication, @PathVariable String id) {
    	userService.delete(userService.getId(id));
    }
}