package fr.istic.sit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.sit.domain.User;
import fr.istic.sit.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;

    @RequestMapping("")
    public List<User> users() {
        return service.getAll();
    }

    @RequestMapping("/{id}")
    public User searchUser(@PathVariable String id) {
    	return service.getId(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody User user){
    	service.insert(user);
    }
    
    @RequestMapping(method = RequestMethod.PATCH)
    public void update(@RequestBody User user){
    	service.update(user);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="{id}")
    public void delete(@PathVariable String id) {
    	service.delete(service.getId(id));
    }
}