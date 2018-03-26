package edu.odu.cs.gold.security;

import edu.odu.cs.gold.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UserService userService;
	
	public UserMethodSecurityExpressionRoot(Authentication authentication,
											UserService userService) {
		super(authentication);
		this.userService = userService;
	}

	@Override
	public boolean hasPermission(Object target, Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission(Object targetId, String targetType, Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getFilterObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getReturnObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getThis() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isAdmin() {
		if (getPermissions().contains("ADMIN")) {
			return true;			
		}
		AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
		WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
		logger.info("'" + user.getUsername() + "' Failed attempt to access an Administrative Function. Remote Address: " + webAuthenticationDetails.getRemoteAddress() + ". Session Id: " + webAuthenticationDetails.getSessionId() + ".");
		return false;
	}
	
	private Set<String> getPermissions() {
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
		Set<String> permissions = new HashSet<>();
		for (GrantedAuthority authority : authorities) {	
			permissions.add(authority.toString());
		}
		return permissions;
	}
}
