package edu.odu.cs.gold.security;

import edu.odu.cs.gold.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class AuthenticatedUserDetailsService implements UserDetailsService, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserService userService;
	
	public AuthenticatedUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		edu.odu.cs.gold.model.User user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.getUsername(),
				user.getPassword(),
				user.isEnabled(),
				true,
				true,
				true,
				user,
				user.getAuthorities());
		logger.info("Authenticated User: " + authenticatedUser);
		logger.info("Granted Authorities: " + user.getAuthorities());
		return authenticatedUser;
	}
}
