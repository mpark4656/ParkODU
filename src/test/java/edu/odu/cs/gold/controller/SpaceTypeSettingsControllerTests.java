package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.SpaceType;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.SpaceTypeRepository;
import edu.odu.cs.gold.service.SpaceTypeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class 4aeSpaceTypeSettingsControllerTests {

    private static String PERMIT_TYPE_ONE_KEY = "a0000000000000000000000000000001";
    private static String PERMIT_TYPE_TWO_KEY = "a0000000000000000000000000000002";
    private static String PARKING_SPACE_ONE_KEY = "c0000000000000000000000000000001";
    private static String PARKING_SPACE_TWO_KEY = "c0000000000000000000000000000002";

    private static String PERMIT_TYPE_ONE_NAME = "Commuter";
    private static String PERMIT_TYPE_TWO_NAME = "Faculty";

    private static String PERMIT_TYPE_ONE_DESCRIPTION = "Description1";
    private static String PERMIT_TYPE_TWO_DESCRIPTION = "Description2";

    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "1";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 1;

    private SpaceTypeSettingsController spaceTypeSettingsController;
    private ParkingSpaceRepository parkingSpaceRepository;
    private SpaceTypeService spaceTypeService;
    private SpaceTypeRepository spaceTypeRepository;

    private SpaceType spaceTypeOne;
    private SpaceType spaceTypeTwo;
    private ParkingSpace parkingSpaceOne;
    private ParkingSpace parkingSpaceTwo;


    @Before
    public void setup() {
        spaceTypeOne = new SpaceType();
        spaceTypeOne.setName(PERMIT_TYPE_ONE_NAME);
        spaceTypeOne.setDescription(PERMIT_TYPE_ONE_DESCRIPTION);

        spaceTypeTwo = new SpaceType();
        spaceTypeTwo.setName(PERMIT_TYPE_TWO_NAME);
        spaceTypeTwo.setDescription(PERMIT_TYPE_TWO_DESCRIPTION);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setFloor(PARKING_SPACE_ONE_FLOOR);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);
        parkingSpaceOne.setSpaceTypeKey(PERMIT_TYPE_ONE_KEY);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);
        parkingSpaceTwo.setSpaceTypeKey(PERMIT_TYPE_TWO_KEY);

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_ONE_KEY)).thenReturn(parkingSpaceOne);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_TWO_KEY)).thenReturn(parkingSpaceTwo);
        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);
        doNothing().when(parkingSpaceRepository).save(any(ParkingSpace.class));
        doNothing().when(parkingSpaceRepository).delete(anyString());

        List<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        spaceTypeRepository = mock(SpaceTypeRepository.class);
        when(spaceTypeRepository.findByKey(PERMIT_TYPE_ONE_KEY)).thenReturn(spaceTypeOne);
        when(spaceTypeRepository.findByKey(PERMIT_TYPE_TWO_KEY)).thenReturn(spaceTypeTwo);
        when(spaceTypeRepository.findAll()).thenReturn(spaceTypes);
        doNothing().when(spaceTypeRepository).save(any(SpaceType.class));
        doNothing().when(spaceTypeRepository).delete(anyString());

        spaceTypeService = mock(SpaceTypeService.class);
        doNothing().when(spaceTypeService).refresh(anyString());
        doNothing().when(spaceTypeService).refresh();

        spaceTypeSettingsController = new SpaceTypeSettingsController(
                parkingSpaceRepository,
                spaceTypeRepository,
                spaceTypeService
        );
    }

    @Test
    public void testIndex_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = spaceTypeSettingsController.index(
                null,
                null,
                null,
                null,
                model
        );

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        assertEquals("settings/space_type/index", returnURL);
        assertTrue(model.containsKey("spaceTypes"));
        assertEquals(model.get("spaceTypes"), spaceTypes);

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_SuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String successMessage = "SUCCESS";

        String returnURL = spaceTypeSettingsController.index(
                successMessage,
                null,
                null,
                null,
                model
        );

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        assertEquals("settings/space_type/index", returnURL);
        assertTrue(model.containsKey("spaceTypes"));
        assertEquals(spaceTypes, model.get("spaceTypes"));
        assertTrue(model.containsKey("successMessage"));
        assertEquals(successMessage, model.get("successMessage"));

        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_InfoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String infoMessage = "INFORMATION";

        String returnURL = spaceTypeSettingsController.index(
                null,
                infoMessage,
                null,
                null,
                model
        );

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        assertEquals("settings/space_type/index", returnURL);
        assertTrue(model.containsKey("spaceTypes"));
        assertEquals(spaceTypes, model.get("spaceTypes"));
        assertTrue(model.containsKey("infoMessage"));
        assertEquals(infoMessage, model.get("infoMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_WarningMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String warningMessage = "warning";

        String returnURL = spaceTypeSettingsController.index(
                null,
                null,
                warningMessage,
                null,
                model
        );

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        assertEquals("settings/space_type/index", returnURL);
        assertTrue(model.containsKey("spaceTypes"));
        assertEquals(spaceTypes, model.get("spaceTypes"));
        assertTrue(model.containsKey("warningMessage"));
        assertEquals(warningMessage, model.get("warningMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_DangerMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String dangerMessage = "danger";

        String returnURL = spaceTypeSettingsController.index(
                null,
                null,
                null,
                dangerMessage,
                model
        );

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);

        assertEquals("settings/space_type/index", returnURL);
        assertTrue(model.containsKey("spaceTypes"));
        assertEquals(spaceTypes, model.get("spaceTypes"));
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
    }

    @Test
    public void testCreate_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = spaceTypeSettingsController.create(
                null,
                null,
                null,
                null,
                model
        );

        assertEquals("settings/space_type/create", returnURL);
        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testCreate_SuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String successMessage = "SUCCESS";

        String returnURL = spaceTypeSettingsController.create(
                successMessage,
                null,
                null,
                null,
                model
        );

        assertEquals("settings/space_type/create", returnURL);
        assertTrue(model.containsKey("successMessage"));
        assertEquals(successMessage, model.get("successMessage"));

        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testCreate_InfoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String infoMessage = "INFOinfo";

        String returnURL = spaceTypeSettingsController.create(
                null,
                infoMessage,
                null,
                null,
                model
        );

        assertEquals("settings/space_type/create", returnURL);
        assertTrue(model.containsKey("infoMessage"));
        assertEquals(infoMessage, model.get("infoMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testCreate_WarningMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String warningMessage = "warning";

        String returnURL = spaceTypeSettingsController.create(
                null,
                null,
                warningMessage,
                null,
                model
        );

        assertEquals("settings/space_type/create", returnURL);
        assertTrue(model.containsKey("warningMessage"));
        assertEquals(warningMessage, model.get("warningMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testCreate_DangerMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String dangerMessage = "danger";

        String returnURL = spaceTypeSettingsController.create(
                null,
                null,
                null,
                dangerMessage,
                model
        );

        assertEquals("settings/space_type/create", returnURL);
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
    }

    @Test
    public void testCreate_Post_NullName() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                null,
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The space name must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_EmptyName() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                "",
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The space name must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_NullDescription() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                null,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The space description must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_EmptyDescription() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                "",
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The space description must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_Duplicate() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                spaceTypeOne.getName() + " already exists.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.create(
                "UNIQUE PERMIT",
                "UNIQUE DESCRIPTIOn",
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/index",returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                "UNIQUE PERMIT has been successfully created.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testSetDescription_NonexistentSpaceKey() {
        String returnURL = spaceTypeSettingsController.set_description(
                "NONEXISTENT",
                PERMIT_TYPE_ONE_DESCRIPTION
        );

        assertEquals(
                "The specified space type, NONEXISTENT, does not exist!",
                returnURL
        );
    }

    @Test
    public void testSetDescription_NullDescription() {
        String returnURL = spaceTypeSettingsController.set_description(
                PERMIT_TYPE_ONE_KEY,
                null
        );

        assertEquals(
                PERMIT_TYPE_ONE_NAME + "'s description is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetDescription_EmptyDescription() {
        String returnURL = spaceTypeSettingsController.set_description(
                PERMIT_TYPE_ONE_KEY,
                ""
        );

        assertEquals(
                PERMIT_TYPE_ONE_NAME + "'s description is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetDescription_Success() {
        String returnURL = spaceTypeSettingsController.set_description(
                PERMIT_TYPE_ONE_KEY,
                "NEW DESCRIPTION"
        );

        assertEquals(
                PERMIT_TYPE_ONE_NAME + "'s description was updated successfully.",
                returnURL
        );
    }

    @Test
    public void testSetSpaceName_NonexistentSpaceKey() {
        String returnURL = spaceTypeSettingsController.set_name(
                "DOESNOTEXIST",
                "NAME"
        );

        assertEquals(
                "The specified space type, DOESNOTEXIST, does not exist!",
                returnURL
        );
    }

    @Test
    public void testSetSpaceName_NullSpaceName() {
        String returnURL = spaceTypeSettingsController.set_name(
                PERMIT_TYPE_ONE_KEY,
                null
        );

        assertEquals(
                "The space name is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetSpaceName_EmptySpaceName() {
        String returnURL = spaceTypeSettingsController.set_name(
                PERMIT_TYPE_ONE_KEY,
                ""
        );

        assertEquals(
                "The space name is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetSpaceName_Success() {
        String returnURL = spaceTypeSettingsController.set_name(
                PERMIT_TYPE_ONE_KEY,
                "NEWNAME"
        );

        assertEquals(
                "NEWNAME was updated successfully.",
                returnURL
        );
    }

    @Test
    public void testDelete_Nonexistent() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = spaceTypeSettingsController.delete(
                "DOESNOTEXIST",
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "Unable to find the specified space type.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testDelete_BeingUsed() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);

        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);

        String returnURL = spaceTypeSettingsController.delete(
                PERMIT_TYPE_ONE_KEY,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                spaceTypeOne.getName() + " is being used by existing parking spaces.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testDelete_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();

        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);

        String returnURL = spaceTypeSettingsController.delete(
                PERMIT_TYPE_ONE_KEY,
                redirectAttributes
        );

        assertEquals("redirect:/settings/space_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                spaceTypeOne.getName() + " was successfully deleted",
                redirectAttributes.get("successMessage")
        );
    }
}
