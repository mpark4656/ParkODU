package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.PermitTypeRepository;
import edu.odu.cs.gold.service.PermitTypeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import java.util.ArrayList;
import java.util.Collection;
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

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);

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

        permitTypeService = new PermitTypeService(
                parkingSpaceRepository,
                permitTypeRepository
        );

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
}
