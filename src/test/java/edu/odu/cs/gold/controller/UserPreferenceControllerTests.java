package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.model.SpaceType;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.PermitTypeRepository;
import edu.odu.cs.gold.repository.SpaceTypeRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.security.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPreferenceControllerTests {

    private String USER_ONE_KEY = "a000000000000000000000000001";
    private String USER_TWO_KEY = "a000000000000000000000000002";
    private String PERMIT_ONE_KEY = "b000000000000000000000000001";
    private String PERMIT_TWO_KEY = "b000000000000000000000000002";
    private String SPACE_ONE_KEY = "c000000000000000000000000001";
    private String SPACE_TWO_KEY = "c000000000000000000000000002";

    private String USER_ONE_NAME = "User1";
    private String USER_TWO_NAME = "User2";
    private String PERMIT_ONE_NAME = "Permit1";
    private String PERMIT_TWO_NAME = "Permit2";
    private String SPACE_ONE_NAME = "Space1";
    private String SPACE_TWO_NAME = "Space2";

    private User userOne;
    private User userTwo;

    private PermitType permitTypeOne;
    private PermitType permitTypeTwo;
    private SpaceType spaceTypeOne;
    private SpaceType spaceTypeTwo;

    private UserRepository userRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private BuildingRepository buildingRepository;

    private UserPreferenceController userPreferenceController;

    @Before
    public void setup() {
        userOne = new User();
        userOne.setUserKey(USER_ONE_KEY);
        userOne.setUsername(USER_ONE_NAME);

        userTwo = new User();
        userTwo.setUserKey(USER_TWO_KEY);
        userTwo.setUsername(USER_TWO_NAME);

        permitTypeOne = new PermitType();
        permitTypeOne.setPermitTypeKey(PERMIT_ONE_KEY);
        permitTypeOne.setName(PERMIT_ONE_NAME);

        permitTypeTwo = new PermitType();
        permitTypeTwo.setPermitTypeKey(PERMIT_TWO_KEY);
        permitTypeTwo.setName(PERMIT_TWO_NAME);

        spaceTypeOne = new SpaceType();
        spaceTypeOne.setSpaceTypeKey(SPACE_ONE_KEY);
        spaceTypeOne.setName(SPACE_ONE_NAME);

        spaceTypeTwo = new SpaceType();
        spaceTypeTwo.setSpaceTypeKey(SPACE_TWO_KEY);
        spaceTypeTwo.setName(SPACE_TWO_NAME);

        List<User> users = new ArrayList<> ();
        users.add(userOne);
        users.add(userTwo);

        List<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        List<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        userRepository = mock(UserRepository.class);
        permitTypeRepository = mock(PermitTypeRepository.class);
        spaceTypeRepository = mock(SpaceTypeRepository.class);
        buildingRepository = mock(BuildingRepository.class);

        when(userRepository.findAll()).thenReturn(users);
        when(userRepository.findByKey(USER_ONE_KEY)).thenReturn(userOne);
        when(userRepository.findByKey(USER_TWO_KEY)).thenReturn(userTwo);
        doNothing().when(userRepository).save(any(User.class));
        doNothing().when(userRepository).delete(anyString());

        when(permitTypeRepository.findAll()).thenReturn(permitTypes);
        when(permitTypeRepository.findByKey(PERMIT_ONE_KEY)).thenReturn(permitTypeOne);
        when(permitTypeRepository.findByKey(PERMIT_TWO_KEY)).thenReturn(permitTypeTwo);
        doNothing().when(permitTypeRepository).save(any(PermitType.class));
        doNothing().when(permitTypeRepository).delete(anyString());

        when(spaceTypeRepository.findAll()).thenReturn(spaceTypes);
        when(spaceTypeRepository.findByKey(SPACE_ONE_KEY)).thenReturn(spaceTypeOne);
        when(spaceTypeRepository.findByKey(SPACE_TWO_KEY)).thenReturn(spaceTypeTwo);
        doNothing().when(spaceTypeRepository).save(any(SpaceType.class));
        doNothing().when(spaceTypeRepository).delete(anyString());

        doNothing().when(buildingRepository).save(any(Building.class));
        doNothing().when(buildingRepository).delete(anyString());

        userPreferenceController = new UserPreferenceController(
                userRepository,
                permitTypeRepository,
                spaceTypeRepository,
                buildingRepository
        );
    }

    @Test
    public void testIndex_UserFound() {
        ExtendedModelMap model = new ExtendedModelMap();
        List<User> users = new ArrayList<>();
        users.add(userOne);
        when(userRepository.findByPredicate(any(Predicate.class))).thenReturn(users);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder securityContextHolder = mock(SecurityContextHolder.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        securityContextHolder.setContext(securityContext);

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(USER_ONE_NAME, "FAKEPASS", authorities);

        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        String returnURL = userPreferenceController.index(null, null, null, null, model);

        assertEquals("user_preference/index", returnURL);
        assertTrue(model.containsKey("user"));
        assertEquals(userOne, model.get("user"));
    }

    @Test
    public void testIndex_UserNotFound() {
        ExtendedModelMap model = new ExtendedModelMap();
        List<User> users = new ArrayList<>();
        when(userRepository.findByPredicate(any(Predicate.class))).thenReturn(users);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder securityContextHolder = mock(SecurityContextHolder.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        securityContextHolder.setContext(securityContext);

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(USER_ONE_NAME, "FAKEPASS", authorities);

        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        String returnURL = userPreferenceController.index(null, null, null, null, model);

        assertEquals("home/index", returnURL);
        assertFalse(model.containsKey("user"));
    }

    @Test
    public void testEdit_NonNullTypes() {
        List<String> permitTypes = new ArrayList<> ();
        List<String> spaceTypes = new ArrayList<> ();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        permitTypes.add(PERMIT_ONE_KEY);
        permitTypes.add(PERMIT_TWO_KEY);
        spaceTypes.add(SPACE_ONE_KEY);
        spaceTypes.add(SPACE_TWO_KEY);

        String returnURL = userPreferenceController.edit(
                userOne,
                redirectAttributes
        );
        assertEquals("redirect:/", returnURL);
    }

    @Test
    public void testEdit_NullTypes() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = userPreferenceController.edit(
                userOne,
                redirectAttributes
        );
        assertEquals("redirect:/", returnURL);
    }
}
