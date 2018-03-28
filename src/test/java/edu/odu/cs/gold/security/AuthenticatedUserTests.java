package edu.odu.cs.gold.security;

import edu.odu.cs.gold.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AuthenticatedUserTests {

    private String userKey;
    private String username;
    private String password;

    private AuthenticatedUser authenticatedUser;
    private User user;

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
        authenticatedUser = new AuthenticatedUser(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                user,
                new ArrayList<>());
    }

    @Test
    public void testAuthenticatedUser() {
        assertEquals(user.getUserKey(), authenticatedUser.getUser().getUserKey());
        assertEquals(user.getUsername(), authenticatedUser.getUser().getUsername());
        assertEquals(user.getPassword(), authenticatedUser.getUser().getPassword());
        assertTrue(authenticatedUser.containsPermission("USER"));
        assertTrue(authenticatedUser.containsPermission("ADMIN"));
    }
}
