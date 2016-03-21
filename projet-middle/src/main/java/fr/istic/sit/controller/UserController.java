package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.User;
import fr.istic.sit.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;
    
    @RequestMapping("/user")
    public User userID(@RequestParam(value="id", required=true) String id) {
    	return service.getId(id);
    }
    
    @RequestMapping("/users")
    public List<User> users() {
    	return service.getAll();
    }
    
    //envoyer un format json comme ça :
    // {"id":"56ebd15ccad8809e1bdf0f63","login":"Alice","password":"Alice","lastname":"Alice","firstname":"Alice","phone":"0202020202"}
    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody User user){
    	service.insert(user);
    }
    
    //envoyer un format json comme ça :
    // {"id":"56ebd15ccad8809e1bdf0f63","login":"Alice","password":"Alice","lastname":"Alice","firstname":"Alice","phone":"0202020202"}
    @RequestMapping(method = RequestMethod.PATCH)
    public void update(@RequestBody User user){
    	service.update(user);
    }
    
    //url de ce type : http://localhost:8080/56ebd15ccad8809e1bdf0f67
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void delete(@PathVariable String id) {
    	service.delete(service.getId(id));
    }
}