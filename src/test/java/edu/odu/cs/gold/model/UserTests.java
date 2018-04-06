package edu.odu.cs.gold.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class UserTests {

    private static String USER_KEY = "u124126481237681289123";
    private static String USER_EMAIL = "user@odu.edu";
    private static String USER_NAME = "userName";
    private static String USER_PASSWORD = "PASSW@RD";
    private static String USER_FIRST_NAME = "Myfirst";
    private static String USER_LAST_NAME = "Mylast";
    private static String USER_ROLE_TYPE = "user";
    private static String USER_ROLE_TYPE_KEY = "u08087565635344353432434";
    private static boolean USER_ENABLED = true;
    private static String USER_CONFIRMATION_TOKEN = "c324233453454213656545658796";
    private static String USER_LAST_VIEWED_DATE = "345678:2323";
    private static Set<String> USER_PERMISSIONS;
    private static Set<String> USER_PREFERRED_PERMIT_TYPES;
    private static Set<String> USER_PREFERRED_SPACE_TYPES;

    private static String PREFERRED_PERMIT_TYPE_ONE = "p312412138712097018326";
    private static String PREFERRED_PERMIT_TYPE_TWO = "p827371223429239245394";

    private static String PREFERRED_SPACE_TYPE_ONE = "s120398192483274923743";
    private static String PREFERRED_SPACE_TYPE_TWO = "s567089578583475932346";

    private static String USER_PERMISSION_ONE = "p56954893278327545489332";
    private static String USER_PERMISSION_TWO = "p48954732763854893548934";

    private static User user;

    @Before
    public void setup() {
        USER_PREFERRED_PERMIT_TYPES = new HashSet<> ();
        USER_PREFERRED_PERMIT_TYPES.add(PREFERRED_PERMIT_TYPE_ONE);
        USER_PREFERRED_PERMIT_TYPES.add(PREFERRED_PERMIT_TYPE_TWO);

        USER_PREFERRED_SPACE_TYPES = new HashSet<> ();
        USER_PREFERRED_SPACE_TYPES.add(PREFERRED_SPACE_TYPE_ONE);
        USER_PREFERRED_SPACE_TYPES.add(PREFERRED_SPACE_TYPE_TWO);

        USER_PERMISSIONS = new HashSet<> ();
        USER_PERMISSIONS.add(USER_PERMISSION_ONE);
        USER_PERMISSIONS.add(USER_PERMISSION_TWO);

        user = new User();
    }

    @Test
    public void testUser() {
        User user = new User();
        assertNull(user.getUserKey());
        assertNull(user.getEmail());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getRoleType());
        assertNull(user.getConfirmationToken());
        assertTrue(user.getPermissions().isEmpty());
        assertTrue(user.getPreferredPermitTypes().isEmpty());
        assertTrue(user.getPreferredSpaceTypes().isEmpty());
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    public void testUserStringStringStringStringStringStringStringBoolean() {
        User user = new User(
                USER_EMAIL,
                USER_NAME,
                USER_PASSWORD,
                USER_FIRST_NAME,
                USER_LAST_NAME,
                USER_ROLE_TYPE,
                USER_ROLE_TYPE_KEY,
                USER_ENABLED,
                USER_CONFIRMATION_TOKEN
        );

        assertNull(user.getUserKey());
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(USER_PASSWORD, user.getPassword());
        assertEquals(USER_FIRST_NAME, user.getFirstName());
        assertEquals(USER_LAST_NAME, user.getLastName());
        assertEquals(USER_ROLE_TYPE, user.getRoleType());
        assertEquals(USER_ROLE_TYPE_KEY, user.getRoleTypeKey());
        assertEquals(USER_ENABLED, user.getEnabled());
        assertEquals(USER_CONFIRMATION_TOKEN, user.getConfirmationToken());
        assertTrue(user.getPermissions().isEmpty());
        assertTrue(user.getPreferredPermitTypes().isEmpty());
        assertTrue(user.getPreferredSpaceTypes().isEmpty());
        assertTrue(user.getAuthorities().isEmpty());
    }

    @Test
    public void testGenerateUserKey() {
        User user = new User();
        user.generateUserKey();

        assertNotNull(user.getUserKey());
        assertFalse(user.getUserKey().isEmpty());
    }

    @Test
    public void testGenerateConfirmationToken() {
        User user = new User();
        user.generateConfirmationToken();

        assertNotNull(user.getConfirmationToken());
        assertFalse(user.getConfirmationToken().isEmpty());
    }

    @Test
    public void testSetConfirmationToken() {
        user.setConfirmationToken(USER_CONFIRMATION_TOKEN);
        assertEquals(USER_CONFIRMATION_TOKEN, user.getConfirmationToken());
    }

    @Test
    public void testGetConfirmationToken() {
        user.setConfirmationToken(USER_CONFIRMATION_TOKEN);
        assertEquals(USER_CONFIRMATION_TOKEN, user.getConfirmationToken());
    }

    @Test
    public void testSetUserKey() {
        user.setUserKey(USER_KEY);
        assertEquals(USER_KEY, user.getUserKey());
    }

    @Test
    public void testGetUserKey() {
        user.setUserKey(USER_KEY);
        assertEquals(USER_KEY, user.getUserKey());
    }

    @Test
    public void testGetPassword() {
        user.setPassword(USER_PASSWORD);
        assertEquals(USER_PASSWORD, user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword(USER_PASSWORD);
        assertEquals(USER_PASSWORD, user.getPassword());
    }

    @Test
    public void testGetFirstName() {
        user.setFirstName(USER_FIRST_NAME);
        assertEquals(USER_FIRST_NAME, user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName(USER_FIRST_NAME);
        assertEquals(USER_FIRST_NAME, user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        user.setLastName(USER_LAST_NAME);
        assertEquals(USER_LAST_NAME, user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName(USER_LAST_NAME);
        assertEquals(USER_LAST_NAME, user.getLastName());
    }

    @Test
    public void testGetEmail() {
        user.setEmail(USER_EMAIL);
        assertEquals(USER_EMAIL, user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail(USER_EMAIL);
        assertEquals(USER_EMAIL, user.getEmail());
    }

    @Test
    public void testGetUsername(){
        user.setUsername(USER_NAME);
        assertEquals(USER_NAME, user.getUsername());
    }

    @Test
    public void testSetUsername() {
        user.setUsername(USER_NAME);
        assertEquals(USER_NAME, user.getUsername());
    }

    @Test
    public void testGetEnabled() {
        user.setEnabled(USER_ENABLED);
        assertEquals(USER_ENABLED, user.getEnabled());
    }

    @Test
    public void testSetEnabled() {
        user.setEnabled(USER_ENABLED);
        assertEquals(USER_ENABLED, user.getEnabled());
    }

    @Test
    public void testGetRoleType() {
        user.setRoleType(USER_ROLE_TYPE);
        assertEquals(USER_ROLE_TYPE, user.getRoleType());
    }

    @Test
    public void testSetRoleType() {
        user.setRoleType(USER_ROLE_TYPE);
        assertEquals(USER_ROLE_TYPE, user.getRoleType());
    }

    @Test
    public void testGetRoleTypeKey() {
        user.setRoleTypeKey(USER_ROLE_TYPE_KEY);
        assertEquals(USER_ROLE_TYPE_KEY, user.getRoleTypeKey());
    }

    @Test
    public void testSetRoleTypeKey() {
        user.setRoleTypeKey(USER_ROLE_TYPE_KEY);
        assertEquals(USER_ROLE_TYPE_KEY, user.getRoleTypeKey());
    }

    @Test
    public void testGetPermissions() {
        user.setPermissions(USER_PERMISSIONS);
        assertEquals(USER_PERMISSIONS, user.getPermissions());
    }

    @Test
    public void testSetPermissions() {
        user.setPermissions(USER_PERMISSIONS);
        assertEquals(USER_PERMISSIONS, user.getPermissions());
    }

    @Test
    public void testGetPreferredPermitTypes() {
        user.setPreferredPermitTypes(USER_PREFERRED_PERMIT_TYPES);
        assertEquals(USER_PREFERRED_PERMIT_TYPES, user.getPreferredPermitTypes());
    }

    @Test
    public void testSetPreferredPermitTypes() {
        user.setPreferredPermitTypes(USER_PREFERRED_PERMIT_TYPES);
        assertEquals(USER_PREFERRED_PERMIT_TYPES, user.getPreferredPermitTypes());
    }

    @Test
    public void testGetPreferredSpaceTypes() {
        user.setPreferredSpaceTypes(USER_PREFERRED_SPACE_TYPES);
        assertEquals(USER_PREFERRED_SPACE_TYPES, user.getPreferredSpaceTypes());
    }

    @Test
    public void testSetPreferredSpaceTypes() {
        user.setPreferredSpaceTypes(USER_PREFERRED_SPACE_TYPES);
        assertEquals(USER_PREFERRED_SPACE_TYPES, user.getPreferredSpaceTypes());
    }

    @Test
    public void testGetAuthorities() {
        user.setPermissions(USER_PERMISSIONS);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(USER_PERMISSION_ONE));
        authorities.add(new SimpleGrantedAuthority(USER_PERMISSION_TWO));

        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    public void testToString() {
        String string = "User{ " +
                "userKey='" + USER_KEY + '\'' +
                ", email='" + USER_EMAIL + '\'' +
                ", username='" + USER_NAME + '\'' +
                ", password='" + USER_PASSWORD + '\'' +
                ", firstName='" + USER_FIRST_NAME + '\'' +
                ", lastName='" + USER_LAST_NAME + '\'' +
                ", roleType='" + USER_ROLE_TYPE + '\'' +
                ", roleTypeKey='" + USER_ROLE_TYPE_KEY + '\'' +
                ", lastNotificationViewedDate=" + USER_LAST_VIEWED_DATE +
                ", enabled=" + USER_ENABLED +
                ", confirmationToken='" + USER_CONFIRMATION_TOKEN + '\'' +
                ", permissions=" + USER_PERMISSIONS +
                ", preferredStartingAddress=" + null + '\'' +
                ", preferredDestinationBuilding=" + null + '\'' +
                ", preferredPermitTypes=" + USER_PREFERRED_PERMIT_TYPES +
                ", preferredSpaceTypes=" + USER_PREFERRED_SPACE_TYPES +
                " }";

        user.setUserKey(USER_KEY);
        user.setEmail(USER_EMAIL);
        user.setUsername(USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setRoleType(USER_ROLE_TYPE);
        user.setRoleTypeKey(USER_ROLE_TYPE_KEY);
        user.setEnabled(USER_ENABLED);
        user.setConfirmationToken(USER_CONFIRMATION_TOKEN);
        user.setLastNotificationViewedDate(USER_LAST_VIEWED_DATE);
        user.setPermissions(USER_PERMISSIONS);
        user.setPreferredPermitTypes(USER_PREFERRED_PERMIT_TYPES);
        user.setPreferredSpaceTypes(USER_PREFERRED_SPACE_TYPES);

        assertEquals(string, user.toString());

    }
}
