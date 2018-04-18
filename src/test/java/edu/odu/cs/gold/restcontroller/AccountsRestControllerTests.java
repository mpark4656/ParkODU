package edu.odu.cs.gold.restcontroller;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountsRestControllerTests {

    private static final String USER_ONE_KEY = "a0000000000000000000000000000001";
    private static final String USER_ONE_CONFIRMATION_TOKEN = "b0000000000000000000000000000001";

    private User userOne;

    private UserRepository userRepository;

    private AccountsRestController accountsRestController;

    private UserService userService;

    @Before
    public void setup() {

        Set<String> permissions = new HashSet<>();
        permissions.add("USER");

        userOne = new User();
        userOne.setUserKey(USER_ONE_KEY);
        userOne.setConfirmationToken(USER_ONE_CONFIRMATION_TOKEN);
        userOne.setFirstName("test");
        userOne.setLastName("user");
        userOne.setEmail("test@odu.edu");
        userOne.setPermissions(permissions);
        userOne.setEnabled(false);

        userRepository = mock(UserRepository.class);
        when(userRepository.findByKey(USER_ONE_KEY)).thenReturn(userOne);

        userService = mock(UserService.class);
        doNothing().when(userService).refresh(anyString());

        accountsRestController = new AccountsRestController(
                userRepository,
                userService
        );
    }

    @Test
    public void testGet() {
        User user = accountsRestController.get(USER_ONE_KEY);
        assertEquals(userOne, user);
    }

    @PostMapping("/add")
    public void  testAdd() {

    }

}
