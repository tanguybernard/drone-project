package fr.istic.sit.security;


import fr.istic.sit.dao.UserRepository;
import fr.istic.sit.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author FireDroneTeam
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {

        log.debug("Authenticating [", login +"]");
        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase;
        //if(lowercaseLogin.contains("@")) {
            userFromDatabase = userRepository.findByLogin(lowercaseLogin);
       /* } else {
            userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);
        }*/

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } /*else if (!userFromDatabase.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not activated");
        }*/

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
       // for (Authority authority : userFromDatabase.getAuthorities()) {
       //     GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
       // GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userFromDatabase.getRole());
        grantedAuthorities.add(grantedAuthority);
       // }

        //return new org.springframework.security.core.userdetails.User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), grantedAuthorities);
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getLogin(), userFromDatabase.getPassword(), grantedAuthorities);

    }

}
