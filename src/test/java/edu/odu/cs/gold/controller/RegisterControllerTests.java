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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private static final String USER_TWO_KEY = "a0000000000000000000000000000002";
    private static final String ROLETYPE_ONE_KEY = "b0000000000000000000000000000001";
    private static final String ROLETYPE_TWO_KEY = "b0000000000000000000000000000002";


    private static final String USER_ONE_NAME = "Username1";
    private static final String USER_TWO_NAME = "Username2";
    private static final String ROLETYPE_ONE_NAME = "ADMIN";
    private static final String ROLETYPE_TWO_NAME = "USER";


    @Before
    public void setup() {

        userOne = new User();
        userOne.setUserKey(USER_ONE_KEY);
        userOne.setUsername(USER_ONE_NAME);

        userTwo = new User();
        userTwo.setUserKey(USER_TWO_KEY);
        userTwo.setUsername(USER_TWO_NAME);

        List<User> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);

        roleTypeOne = new RoleType();
        roleTypeOne.setRoleKey(ROLETYPE_ONE_KEY);
        roleTypeOne.setName(ROLETYPE_ONE_NAME);

        roleTypeTwo = new RoleType();
        roleTypeTwo.setRoleKey(ROLETYPE_TWO_KEY);
        roleTypeTwo.setName(ROLETYPE_TWO_NAME);

        List<RoleType> roleTypes = new ArrayList<>();
        roleTypes.add(roleTypeOne);
        roleTypes.add(roleTypeTwo);

        userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.findByKey(USER_ONE_KEY)).thenReturn(userOne);
        when(userRepository.findByKey(USER_TWO_KEY)).thenReturn(userTwo);
        doNothing().when(userRepository).save(any(User.class));
        doNothing().when(userRepository).delete(anyString());

        roleTypeRepository = mock(RoleTypeRepository.class);
        when(roleTypeRepository.findByKey(ROLETYPE_ONE_KEY)).thenReturn(roleTypeOne);
        when(roleTypeRepository.findByKey(ROLETYPE_TWO_KEY)).thenReturn(roleTypeTwo);
        when(roleTypeRepository.findAll()).thenReturn(roleTypes);
        doNothing().when(roleTypeRepository).save(any(RoleType.class));
        doNothing().when(roleTypeRepository).delete(anyString());

        emailService = mock(EmailService.class);
        doNothing().when(emailService).sendEmail(simpleMailMessage);

        userService = mock(UserService.class);
        doNothing().when(userService).saveUser(any(User.class));
        doNothing().when(userService).deleteUser(anyString());

        registerController = new RegisterController(userService,emailService,userRepository);
    }

    @Test
    public void testShowRegistrationPage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = registerController.showRegistrationPage(
                model,
                userOne
        );

        assertEquals("user/register", returnURL);
        assertEquals(userOne, model.get("user"));
    }

    @Test
    public void testProcessRegistrationForm_Failure() {
        ExtendedModelMap model = new ExtendedModelMap();

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(userService.userExists(anyString())).thenReturn(true);

        String returnURL = registerController.processRegistrationForm(
                model,
                userOne,
                result,
                request
        );

        assertTrue(model.containsKey("dangerMessage"));

        assertEquals("user/register", returnURL);
    }

    @Test
    public void testProcessRegistrationForm_Success() {
        ExtendedModelMap model = new ExtendedModelMap();

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(userService.userExists(anyString())).thenReturn(false);

        String returnURL = registerController.processRegistrationForm(
                model,
                userOne,
                result,
                request
        );

        assertTrue(model.containsKey("successMessage"));

        assertEquals("user/register", returnURL);
    }

    @Test
    public void testShowConfirmationPage_Success() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        List<User> users = new ArrayList<>();
        users.add(userOne);

        when(userRepository.findByPredicate(any(Predicate.class))).thenReturn(users);
        String returnURL = registerController.showConfirmationPage(
                model,
                "TOKEN",
                redirectAttributes
        );

        assertEquals("redirect:user/login", returnURL);
        assertTrue(model.containsKey("successMessage"));
    }

    @Test
    public void testShowConfirmationPage_Failure() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        userTwo.setEnabled(true);
        List<User> users = new ArrayList<>();
        users.add(userTwo);

        when(userRepository.findByPredicate(any(Predicate.class))).thenReturn(users);
        String returnURL = registerController.showConfirmationPage(
                model,
                "TOKEN",
                redirectAttributes
        );

        assertEquals("redirect:user/login", returnURL);
        assertTrue(model.containsKey("dangerMessage"));
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
