package edu.odu.cs.gold.security;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMethodSecurityExpressionRootTests {

    private String userKey;
    private String username;
    private String password;

    private User user;
    private UserService userService;
    private Authentication authentication;
    private AuthenticatedUserDetailsService authenticatedUserDetailsService;
    private UserMethodSecurityExpressionRoot userMethodSecurityExpressionRoot;

    @Before
    public void setup() {
        userKey = UUID.randomUUID().toString();
        username = "Test";
        password = "Password";
        user = new User();
        user.setUserKey(userKey);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        user.getPermissions().add("USER");
        user.getPermissions().add("ADMIN");

        authentication = mock(Authentication.class);

        userService = mock(UserService.class);
        when(userService.findByUsername(username)).thenReturn(user);

        authenticatedUserDetailsService = new AuthenticatedUserDetailsService(userService);
        userMethodSecurityExpressionRoot = new UserMethodSecurityExpressionRoot(authentication, userService);
    }

    @Test
    public void hasPermission() {
        assertFalse(userMethodSecurityExpressionRoot.hasPermission(null, null));
        assertFalse(userMethodSecurityExpressionRoot.hasPermission(null, null, null));
    }

    @Test
    public void setFilterObject() {
        userMethodSecurityExpressionRoot.setFilterObject(null);
    }

    @Test
    public void getFilterObject() {
        assertNull(userMethodSecurityExpressionRoot.getFilterObject());
    }

    @Test
    public void setReturnObject() {
        userMethodSecurityExpressionRoot.setReturnObject(null);
    }

    @Test
    public void getReturnObject() {
        assertNull(userMethodSecurityExpressionRoot.getReturnObject());
    }

    @Test
    public void getThis() {
        assertNull(userMethodSecurityExpressionRoot.getThis());
    }

    @Test
    public void isAdmin() {
        // TODO
    }

    @Test
    public void getPermissions() {
        // TODO
    }
}
