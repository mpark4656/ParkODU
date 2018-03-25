package edu.odu.cs.gold.configuration;

import edu.odu.cs.gold.security.UserMethodSecurityExpressionHandler;
import edu.odu.cs.gold.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	private UserService userService;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		UserMethodSecurityExpressionHandler expressionHandler = new UserMethodSecurityExpressionHandler(userService);
		return expressionHandler;
	}
}
