package edu.odu.cs.gold.security;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMethodSecurityExpressionHandlerTests {

    private String userKey;
    private String username;
    private String password;

    private User user;
    private UserService userService;
    private UserMethodSecurityExpressionHandler userMethodSecurityExpressionHandler;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        userMethodSecurityExpressionHandler = new UserMethodSecurityExpressionHandler(userService);
    }

    @Test
    public void createSecurityExpressionRoot() {
        // TODO
    }
}
