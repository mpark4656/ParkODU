package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;

import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.model.RoleType;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.repository.RoleTypeRepository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AccountsControllerTests {

    private String USER_ONE_KEY = "a000000000000000000000000001";
    private String USER_TWO_KEY = "a000000000000000000000000002";
    private String ROLE_TYPE_ONE_KEY = "b0000000000000000000000001";
    private String ROLE_TYPE_TWO_KEY = "b0000000000000000000000001";

    private String ROLE_TYPE_ONE_NAME = "ADMIN";
    private String ROLE_TYPE_TWO_NAME = "USER";

    private String USER_ONE_NAME = "User1";
    private String USER_TWO_NAME = "User2";

    private User userOne;
    private User userTwo;

    RoleType roleTypeOne;
    RoleType roleTypeTwo;

    private RoleTypeRepository roleTypeRepository;
    private UserRepository userRepository;
    private SessionRegistry sessionRegistry;

    private AccountsController accountsController;

    @Before
    public void setup() {
        userOne = new User();
        userOne.setUserKey(USER_ONE_KEY);
        userOne.setUsername(USER_ONE_NAME);
        userOne.setFirstName(USER_ONE_NAME);

        userTwo = new User();
        userTwo.setUserKey(USER_TWO_KEY);
        userTwo.setUsername(USER_TWO_NAME);
        userTwo.setFirstName(USER_TWO_NAME);

        roleTypeOne = new RoleType();
        roleTypeOne.setRoleKey(ROLE_TYPE_ONE_KEY);
        roleTypeOne.setName(ROLE_TYPE_ONE_NAME);

        roleTypeTwo = new RoleType();
        roleTypeTwo.setRoleKey(ROLE_TYPE_TWO_KEY);
        roleTypeTwo.setName(ROLE_TYPE_TWO_NAME);

        List<User> users = new ArrayList<> ();
        users.add(userOne);
        users.add(userTwo);

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
        when(roleTypeRepository.findAll()).thenReturn(roleTypes);
        when(roleTypeRepository.findByKey(ROLE_TYPE_ONE_KEY)).thenReturn(roleTypeOne);
        when(roleTypeRepository.findByKey(ROLE_TYPE_TWO_KEY)).thenReturn(roleTypeTwo);
        doNothing().when(roleTypeRepository).save(any(RoleType.class));
        doNothing().when(roleTypeRepository).delete(anyString());

        sessionRegistry = mock(SessionRegistry.class);
        when(sessionRegistry.getAllPrincipals()).thenReturn(new ArrayList<>());

        accountsController = new AccountsController(
                userRepository,
                roleTypeRepository,
                sessionRegistry
        );
    }

    @Test
    public void testIndex_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = accountsController.index(
                null,
                null,
                null,
                null,
                model
        );

        List<User> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);

        assertEquals("settings/accounts/index", returnURL);
        assertTrue(model.containsKey("users"));
        assertEquals(users, model.get("users"));
    }

    @Test
    public void testIndex_WithMessages() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = accountsController.index(
                "successMessage",
                "infoMessage",
                "warningMessage",
                "dangerMessage",
                model
        );

        List<User> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);

        assertEquals("settings/accounts/index", returnURL);
        assertTrue(model.containsKey("users"));
        assertEquals(users, model.get("users"));
        assertEquals("successMessage", model.get("successMessage"));
        assertEquals("infoMessage", model.get("infoMessage"));
        assertEquals("warningMessage", model.get("warningMessage"));
        assertEquals("dangerMessage", model.get("dangerMessage"));
    }

    @Test
    public void testCreate_Get() {
        ExtendedModelMap model= new ExtendedModelMap();

        String returnURL = accountsController.create(model);

        assertEquals("settings/accounts/create", returnURL);
        assertTrue(model.containsKey("user"));
        assertTrue(model.containsAttribute("user"));

        List<RoleType> roleTypes = new ArrayList<>();
        roleTypes.add(roleTypeOne);
        roleTypes.add(roleTypeTwo);

        assertEquals(roleTypes, model.get("roleTypes"));
    }

    @Test
    public void testCreate_Post_Success() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        when(userRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        String returnURL = accountsController.create(
                userOne,
                model,
                redirectAttributes
        );

        assertEquals("redirect:/settings/accounts/index", returnURL);
        assertEquals(
                "The user " + userOne.getEmail() + " was successfully created.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testCreate_Post_Failure() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        when(userRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        String returnURL = accountsController.create(
                userOne,
                model,
                redirectAttributes
        );

        assertEquals("settings/accounts/create", returnURL);
        assertTrue(model.containsKey("dangerMessage"));
        assertTrue(model.containsAttribute("dangerMessage"));
    }

    @Test
    public void testEdit_Get() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = accountsController.edit(
                USER_ONE_KEY,
                model
        );

        List<RoleType> roleTypes = new ArrayList<>();
        roleTypes.add(roleTypeOne);
        roleTypes.add(roleTypeTwo);

        assertEquals("settings/accounts/edit", returnURL);
        assertEquals(userOne, model.get("user"));
        assertEquals(roleTypes, model.get("roleTypes"));
    }

    @Test
    public void testEdit_Post_Success() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        String returnURL = accountsController.edit(
                userOne,
                true,
                model,
                redirectAttributes
        );

        assertEquals("redirect:/settings/accounts/index", returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertTrue(redirectAttributes.containsAttribute("successMessage"));
        assertEquals(
                "The user " + userOne.getEmail() + " was successfully updated.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testEdit_Post_Failure() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(userRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        String returnURL = accountsController.edit(
                userOne,
                true,
                model,
                redirectAttributes
        );

        assertEquals("settings/accounts/edit", returnURL);
        assertTrue(model.containsKey("dangerMessage"));
        assertTrue(model.containsAttribute("dangerMessage"));
        assertEquals(
                "The user " + userOne.getEmail() + " already exists.",
                model.get("dangerMessage")
        );
    }

    @Test
    public void testResetPassword_Success(){
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = accountsController.reset_password(
                userOne.getUserKey(),
                "CORRECT",
                "CORRECT",
                redirectAttributes
        );

        assertEquals("redirect:/settings/accounts/index", returnURL);
        assertEquals(
                "The password for " + userOne.getUsername() + " was successfully changed.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testResetPassword_Failure(){
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = accountsController.reset_password(
                userOne.getUserKey(),
                "CORRECT",
                "INCORRECT",
                redirectAttributes
        );

        assertEquals("redirect:/settings/accounts/index", returnURL);
        assertEquals(
                "Failed to change the password of a User due to a password mismatch.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testSetEnabled() {
        String returnURL = accountsController.setEnabled(
                true,
                userOne.getUserKey()
        );

        assertEquals(
                userOne.getUserKey() + " enabled: " + true,
                returnURL);
    }

    @Test
    public void testDelete_Post() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = accountsController.delete(
                userOne.getUserKey(),
                redirectAttributes
        );

        assertEquals("redirect:/settings/accounts/index", returnURL);
        assertEquals(
                "The user " + userOne.getEmail() + " was successfully deleted.",
                redirectAttributes.get("successMessage")
        );
    }
}
