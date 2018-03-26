package edu.odu.cs.gold.security;

import java.io.Serializable;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private edu.odu.cs.gold.model.User user;
	private Set<String> permissions;
	private List<SessionInformation> sessionInformations;
	
	public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.sessionInformations = new ArrayList<>();
	}
	
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
		this.permissions = new HashSet<>();
		for (GrantedAuthority authority : authorities) {
			this.permissions.add(authority.toString());
		}
		this.sessionInformations = new ArrayList<>();
	}
		
	public edu.odu.cs.gold.model.User getUser() {
		return user;
	}

	public void setUser(edu.odu.cs.gold.model.User user) {
		this.user = user;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public boolean containsPermission(String permission) {
		if (permissions.contains(permission)) {
			return true;
		}
		return false;
	}

	public List<SessionInformation> getSessionInformations() {
		return sessionInformations;
	}

	public void setSessionInformations(List<SessionInformation> sessionInformations) {
		this.sessionInformations = sessionInformations;
	}	


}
