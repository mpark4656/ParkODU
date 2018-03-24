package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.PermitTypeRepository;
import edu.odu.cs.gold.service.PermitTypeService;
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

public class PermitTypeSettingsControllerTests {

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

    private PermitTypeSettingsController permitTypeSettingsController;
    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeService permitTypeService;
    private PermitTypeRepository permitTypeRepository;

    private PermitType permitTypeOne;
    private PermitType permitTypeTwo;
    private ParkingSpace parkingSpaceOne;
    private ParkingSpace parkingSpaceTwo;


    @Before
    public void setup() {
        permitTypeOne = new PermitType();
        permitTypeOne.setName(PERMIT_TYPE_ONE_NAME);
        permitTypeOne.setDescription(PERMIT_TYPE_ONE_DESCRIPTION);

        permitTypeTwo = new PermitType();
        permitTypeTwo.setName(PERMIT_TYPE_TWO_NAME);
        permitTypeTwo.setDescription(PERMIT_TYPE_TWO_DESCRIPTION);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setFloor(PARKING_SPACE_ONE_FLOOR);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);
        parkingSpaceOne.setPermitTypeKey(PERMIT_TYPE_ONE_KEY);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);
        parkingSpaceTwo.setPermitTypeKey(PERMIT_TYPE_TWO_KEY);

        Collection<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_ONE_KEY)).thenReturn(parkingSpaceOne);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_TWO_KEY)).thenReturn(parkingSpaceTwo);
        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);
        doNothing().when(parkingSpaceRepository).save(any(ParkingSpace.class));
        doNothing().when(parkingSpaceRepository).delete(anyString());

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        permitTypeRepository = mock(PermitTypeRepository.class);
        when(permitTypeRepository.findByKey(PERMIT_TYPE_ONE_KEY)).thenReturn(permitTypeOne);
        when(permitTypeRepository.findByKey(PERMIT_TYPE_TWO_KEY)).thenReturn(permitTypeTwo);
        when(permitTypeRepository.findAll()).thenReturn(permitTypes);
        doNothing().when(permitTypeRepository).save(any(PermitType.class));
        doNothing().when(permitTypeRepository).delete(anyString());

        permitTypeService = mock(PermitTypeService.class);
        doNothing().when(permitTypeService).refresh(anyString());
        doNothing().when(permitTypeService).refresh();

        permitTypeSettingsController = new PermitTypeSettingsController(
                parkingSpaceRepository,
                permitTypeRepository,
                permitTypeService
        );
    }

    @Test
    public void testIndex_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = permitTypeSettingsController.index(
                null,
                null,
                null,
                null,
                model
        );

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        assertEquals("settings/permit_type/index", returnURL);
        assertTrue(model.containsKey("permitTypes"));
        assertEquals(model.get("permitTypes"), permitTypes);

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_SuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String successMessage = "SUCCESS";

        String returnURL = permitTypeSettingsController.index(
                successMessage,
                null,
                null,
                null,
                model
        );

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        assertEquals("settings/permit_type/index", returnURL);
        assertTrue(model.containsKey("permitTypes"));
        assertEquals(permitTypes, model.get("permitTypes"));
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

        String returnURL = permitTypeSettingsController.index(
                null,
                infoMessage,
                null,
                null,
                model
        );

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        assertEquals("settings/permit_type/index", returnURL);
        assertTrue(model.containsKey("permitTypes"));
        assertEquals(permitTypes, model.get("permitTypes"));
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

        String returnURL = permitTypeSettingsController.index(
                null,
                null,
                warningMessage,
                null,
                model
        );

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        assertEquals("settings/permit_type/index", returnURL);
        assertTrue(model.containsKey("permitTypes"));
        assertEquals(permitTypes, model.get("permitTypes"));
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

        String returnURL = permitTypeSettingsController.index(
                null,
                null,
                null,
                dangerMessage,
                model
        );

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);

        assertEquals("settings/permit_type/index", returnURL);
        assertTrue(model.containsKey("permitTypes"));
        assertEquals(permitTypes, model.get("permitTypes"));
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
    }

    @Test
    public void testCreate_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = permitTypeSettingsController.create(
                null,
                null,
                null,
                null,
                model
        );

        assertEquals("settings/permit_type/create", returnURL);
        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testCreate_SuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        String successMessage = "SUCCESS";

        String returnURL = permitTypeSettingsController.create(
                successMessage,
                null,
                null,
                null,
                model
        );

        assertEquals("settings/permit_type/create", returnURL);
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

        String returnURL = permitTypeSettingsController.create(
                null,
                infoMessage,
                null,
                null,
                model
        );

        assertEquals("settings/permit_type/create", returnURL);
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

        String returnURL = permitTypeSettingsController.create(
                null,
                null,
                warningMessage,
                null,
                model
        );

        assertEquals("settings/permit_type/create", returnURL);
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

        String returnURL = permitTypeSettingsController.create(
                null,
                null,
                null,
                dangerMessage,
                model
        );

        assertEquals("settings/permit_type/create", returnURL);
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
    }

    @Test
    public void testCreate_Post_NullName() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                null,
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The permit name must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_EmptyName() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                "",
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The permit name must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_NullDescription() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                null,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The permit description must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_EmptyDescription() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                "",
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "The permit description must be specified.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_Duplicate() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                PERMIT_TYPE_ONE_NAME,
                PERMIT_TYPE_ONE_DESCRIPTION,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/create",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                permitTypeOne.getName() + " already exists.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testCreate_Post_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = permitTypeSettingsController.create(
                "UNIQUE PERMIT",
                "UNIQUE DESCRIPTIOn",
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/index",returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                "UNIQUE PERMIT has been successfully created.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testSetDescription_NonexistentPermitKey() {
        String returnURL = permitTypeSettingsController.set_description(
                "NONEXISTENT",
                PERMIT_TYPE_ONE_DESCRIPTION
        );

        assertEquals(
                "The specified permit type, NONEXISTENT,does not exist!",
                returnURL
        );
    }

    @Test
    public void testSetDescription_NullDescription() {
        String returnURL = permitTypeSettingsController.set_description(
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
        String returnURL = permitTypeSettingsController.set_description(
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
        String returnURL = permitTypeSettingsController.set_description(
                PERMIT_TYPE_ONE_KEY,
                "NEW DESCRIPTION"
        );

        assertEquals(
                PERMIT_TYPE_ONE_NAME + "'s description was updated successfully.",
                returnURL
        );
    }

    @Test
    public void testSetPermitName_NonexistentPermitKey() {
        String returnURL = permitTypeSettingsController.set_name(
                "DOESNOTEXIST",
                "NAME"
        );

        assertEquals(
                "The specified permit type, DOESNOTEXIST, does not exist!",
                returnURL
        );
    }

    @Test
    public void testSetPermitName_NullPermitName() {
        String returnURL = permitTypeSettingsController.set_name(
                PERMIT_TYPE_ONE_KEY,
                null
        );

        assertEquals(
                "The permit name is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetPermitName_EmptyPermitName() {
        String returnURL = permitTypeSettingsController.set_name(
                PERMIT_TYPE_ONE_KEY,
                ""
        );

        assertEquals(
                "The permit name is null or empty!",
                returnURL
        );
    }

    @Test
    public void testSetPermitName_Success() {
        String returnURL = permitTypeSettingsController.set_name(
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

        String returnURL = permitTypeSettingsController.delete(
                "DOESNOTEXIST",
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "Unable to find the specified permit type.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testDelete_BeingUsed() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);

        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);

        String returnURL = permitTypeSettingsController.delete(
                PERMIT_TYPE_ONE_KEY,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                permitTypeOne.getName() + " is being used by existing parking spaces.",
                redirectAttributes.get("dangerMessage")
        );
    }

    @Test
    public void testDelete_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();

        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);

        String returnURL = permitTypeSettingsController.delete(
                PERMIT_TYPE_ONE_KEY,
                redirectAttributes
        );

        assertEquals("redirect:/settings/permit_type/index", returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                "The permit " + permitTypeOne.getName() + " was successfully deleted",
                redirectAttributes.get("successMessage")
        );
    }
}
