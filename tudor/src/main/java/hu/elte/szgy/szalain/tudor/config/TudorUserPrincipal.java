/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.config;

import hu.elte.szgy.szalain.tudor.model.User;
import hu.elte.szgy.szalain.tudor.model.User.UserType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author User
 */

public class TudorUserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;
    private User user;
    private List<GrantedAuthority> auths=new ArrayList<GrantedAuthority>(5);
 
    public TudorUserPrincipal(User user) {
        this.user = user;

        auths.add(new SimpleGrantedAuthority("ROLE_" + user.getType().name()));
        
        if(user.getType() == UserType.UGYFEL) {
        	auths.add(new SimpleGrantedAuthority("ROLE_UGYFEL"));
        } else if(user.getType() == UserType.TUDOR) {
        	auths.add(new SimpleGrantedAuthority("ROLE_TUDOR"));
        } else if(user.getType() == UserType.ADMIN) {
        	auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
    }

	public java.util.Collection<? extends GrantedAuthority> getAuthorities() { return auths; }  
	public java.lang.String getUsername() { return user.getUsername(); }
	public java.lang.String getPassword() { return user.getPassword(); }
	public long getUserId() { return user.getId(); }

	public boolean isEnabled() { return true; }
	public boolean isCredentialsNonExpired() { return true; }
	public boolean isAccountNonExpired() { return true; }
	public boolean isAccountNonLocked() { return true; }
}

