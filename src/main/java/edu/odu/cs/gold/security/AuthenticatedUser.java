package edu.odu.cs.gold.security;

import java.io.Serializable;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private edu.odu.cs.gold.model.User user;
	private Set<String> permissions;
	
	public AuthenticatedUser(String username, 
			String password, 
			boolean enabled, 
			boolean accountNonExpired,
			boolean credentialsNonExpired, 
			boolean accountNonLocked,
		 	edu.odu.cs.gold.model.User user,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
		this.permissions = user.getPermissions();
	}
		
	public edu.odu.cs.gold.model.User getUser() {
		return user;
	}

	public boolean containsPermission(String permission) {
		return permissions.contains(permission);
	}
}
