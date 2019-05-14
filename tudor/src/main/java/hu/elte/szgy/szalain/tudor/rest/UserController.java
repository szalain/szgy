package hu.elte.szgy.szalain.tudor.rest;


import hu.elte.szgy.szalain.tudor.config.TudorUserPrincipal;
import hu.elte.szgy.szalain.tudor.model.User;
import hu.elte.szgy.szalain.tudor.model.User.UserType;
import hu.elte.szgy.szalain.tudor.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Transactional
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;

    long userID(Authentication auth) {
        return ((TudorUserPrincipal)auth.getPrincipal()).getUserId();
    }
    
    private String printUser(User u) {
	return "{ \"username\":\"" + u.getUsername() + "\", " + "\"type\":\"" + u.getType().name() + "\", "
		+ "\"id\":\"" + u.getId() + "\"}";
    }

    @GetMapping("/self")
    public String selfUser(Authentication a) {
	User u = userRepo.findByUsername(a.getName());
	return printUser(u);
    }

    @PutMapping("/self")
    public ResponseEntity updateUser(@RequestBody User u, Authentication auth) {
        User thisUser = userRepo.getOne(userID(auth));
        if(u.getEmail() != null && u.getEmail().length() > 0) thisUser.setEmail(u.getEmail());
        if(u.getPassword() != null && u.getPassword().length() > 0) {
            if(!u.getPassword().startsWith("{")) thisUser.setPassword("{noop}" + u.getPassword());
        }
        userRepo.save(thisUser);
        return ResponseEntity.ok(thisUser);
    }
    
    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody(required = false) User u) {

	if (!u.getPassword().startsWith("{"))
	    u.setPassword("{noop}" + u.getPassword());
	if (userRepo.findByUsername(u.getUsername()) != null) {
	    throw new EntityExistsException("Name already used");
	}
        u.setType(User.UserType.UGYFEL);
        //KÖNNYEBB TESZTELÉSRE
        if(u.getUsername().equals("admin")) u.setType(UserType.ADMIN);
        if(u.getUsername().equals("tudor")) u.setType(UserType.TUDOR);
	userRepo.save(u);
	log.info("Creating user: " + u.getId());
	return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{userid}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userid") long userId, Authentication a) {
        if(!userRepo.existsById(userId)) return ResponseEntity.badRequest().build();
	User u = userRepo.getOne(userId);
	if (!a.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || u.getType() == UserType.ADMIN) {
	    throw new AccessDeniedException("Not authorized to delete");
	}
	userRepo.delete(u);
	return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/dispatch")
    public ResponseEntity<Void> dispatchUser() {
	// log.debug("Into URI: " + rr.getURI().toString() );
	SecurityContext cc = SecurityContextHolder.getContext();
	HttpHeaders headers = new HttpHeaders();
	if (cc.getAuthentication() != null) {
	    Authentication a = cc.getAuthentication();
	    try {
		headers.setLocation(new URI("/questionall"));
	    } catch (URISyntaxException e) {
		log.warn("Dispatcher cannot redirect");
	    }
	}

	return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
