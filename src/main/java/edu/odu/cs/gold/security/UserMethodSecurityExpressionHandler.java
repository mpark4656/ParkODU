package edu.odu.cs.gold.security;

import edu.odu.cs.gold.service.UserService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class UserMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	private UserService userService;
	
	public UserMethodSecurityExpressionHandler(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		UserMethodSecurityExpressionRoot root = new UserMethodSecurityExpressionRoot(authentication, userService);
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(trustResolver);
		root.setRoleHierarchy(getRoleHierarchy());
		return root;
	}
}
