package hu.elte.szgy.szalain.tudor.config;

import hu.elte.szgy.szalain.tudor.model.User;
import hu.elte.szgy.szalain.tudor.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

public class TudorUserService  implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger( TudorUserService.class );
	
    @Autowired
    private UserRepository userRepository;
 
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
    	logger.info("Authenticating " + username);
        User user = userRepository.findByUsername(username);
        System.out.println(user);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
    	logger.info("User data " + user.getPassword() + " " + user.getType());
        return new TudorUserPrincipal(user);
    }
}
