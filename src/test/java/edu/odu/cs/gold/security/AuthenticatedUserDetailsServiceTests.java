package edu.odu.cs.gold.security;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticatedUserDetailsServiceTests {

    private String userKey;
    private String username;
    private String password;

    private User user;
    private UserService userService;
    private AuthenticatedUserDetailsService authenticatedUserDetailsService;

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

        userService = mock(UserService.class);
        when(userService.findByUsername(username)).thenReturn(user);

        authenticatedUserDetailsService = new AuthenticatedUserDetailsService(userService);
    }

    @Test
    public void loadByUsername() {
        // Test valid authentication
        AuthenticatedUser authenticatedUser = (AuthenticatedUser)authenticatedUserDetailsService.loadUserByUsername(username);
        assertEquals(user.getUserKey(), authenticatedUser.getUser().getUserKey());
        assertEquals(user.getUsername(), authenticatedUser.getUser().getUsername());
        assertEquals(user.getPassword(), authenticatedUser.getUser().getPassword());
        assertTrue(authenticatedUser.containsPermission("USER"));
        assertTrue(authenticatedUser.containsPermission("ADMIN"));
        // Test invalid authentication
        try {
            authenticatedUser = (AuthenticatedUser)authenticatedUserDetailsService.loadUserByUsername(username);
        }
        catch (Exception e) {
            assertNotNull(e);
        }
    }
}
