package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.model.RoleType;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.repository.RoleTypeRepository;

import edu.odu.cs.gold.service.EmailService;
import edu.odu.cs.gold.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RegisterControllerTests {

    private RegisterController registerController;
    private RoleTypeRepository roleTypeRepository;
    private UserRepository userRepository;

    private UserService userService;
    private EmailService emailService;
    private SimpleMailMessage simpleMailMessage;

    private User userOne;
    private User userTwo;

    private RoleType roleTypeOne;
    private RoleType roleTypeTwo;

    private static final String USER_ONE_KEY = "a0000000000000000000000000000001";
    private static final String ROLETYPE_ONE_KEY = "a0000000000000000000000000000001";



    @Before
    public void setup() {
        // TODO
        userRepository = mock(UserRepository.class);
        when(userRepository.findByKey(USER_ONE_KEY)).thenReturn(userOne);

        roleTypeRepository = mock(RoleTypeRepository.class);
        when(roleTypeRepository.findByKey(ROLETYPE_ONE_KEY)).thenReturn(roleTypeOne);

        emailService = mock(EmailService.class);
        doNothing().when(emailService).sendEmail(simpleMailMessage);

        registerController = new RegisterController(userService,emailService,userRepository);
    }

    @Test
    public void testIndex() {
        // TODO
    }

    @Test
    public void testRegister_Get() {
        // TODO
    }

    @Test
    public void testRegister_Post() {
        // TODO
    }

    @Test
    public void testConfirm_Get_Success() {
        // TODO
    }

    @Test
    public void testConfirm_Get_Error() {

    }

    @Test
    public void testLogin_Success() {
        String token = "confirmationLinkSuccess";
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = registerController.login(model, token);

        assertTrue(model.containsKey("successMessage"));
        assertEquals("Confirmation link verified!", model.get("successMessage"));
        assertEquals("redirect:home/login", returnURL);
    }

    @Test
    public void testLogin_Error() {
        String token = "confirmationLinkError";
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = registerController.login(model, token);

        assertTrue(model.containsKey("dangerMessage"));
        assertEquals("Oops! Confirmation link not valid!", model.get("dangerMessage"));
        assertEquals("redirect:home/login", returnURL);
    }
}
