package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FloorSettingsControllerTests {

    private static String GARAGE_ONE_KEY = "a0000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "a0000000000000000000000000000002";
    private static String FLOOR_ONE_KEY = "b0000000000000000000000000000001";
    private static String FLOOR_TWO_KEY = "b0000000000000000000000000000002";
    private static String PARKING_SPACE_ONE_KEY = "c0000000000000000000000000000001";
    private static String PARKING_SPACE_TWO_KEY = "c0000000000000000000000000000002";
    private static String PARKING_SPACE_THREE_KEY = "c0000000000000000000000000000003";
    private static String PARKING_SPACE_FOUR_KEY = "c0000000000000000000000000000004";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";
    private static String FLOOR_ONE_NUMBER = "1";
    private static String FLOOR_TWO_NUMBER = "2";
    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "2";
    private static String PARKING_SPACE_THREE_FLOOR = "3";
    private static String PARKING_SPACE_FOUR_FLOOR = "4";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 2;
    private static Integer PARKING_SPACE_THREE_NUMBER = 1;
    private static Integer PARKING_SPACE_FOUR_NUMBER = 2;

    private FloorSettingsController floorSettingsController;
    private GarageService garageService;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    private Garage garageOne;
    private Garage garageTwo;
    private Floor floorOne;
    private Floor floorTwo;
    private ParkingSpace parkingSpaceOne;
    private ParkingSpace parkingSpaceTwo;
    private ParkingSpace parkingSpaceThree;
    private ParkingSpace parkingSpaceFour;


    @Before
    public void setup() {

        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        garageTwo = new Garage();
        garageTwo.setGarageKey(GARAGE_TWO_KEY);
        garageTwo.setName(GARAGE_TWO_NAME);

        floorOne = new Floor();
        floorOne.setFloorKey(FLOOR_ONE_KEY);
        floorOne.setGarageKey(GARAGE_ONE_KEY);
        floorOne.setNumber(FLOOR_ONE_NUMBER);

        floorTwo = new Floor();
        floorTwo.setFloorKey(FLOOR_TWO_KEY);
        floorTwo.setGarageKey(GARAGE_TWO_KEY);
        floorTwo.setNumber(FLOOR_TWO_NUMBER);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceOne.setFloor(PARKING_SPACE_ONE_FLOOR);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);

        parkingSpaceThree = new ParkingSpace();
        parkingSpaceThree.setParkingSpaceKey(PARKING_SPACE_THREE_KEY);
        parkingSpaceThree.setGarageKey(GARAGE_TWO_KEY);
        parkingSpaceThree.setFloor(PARKING_SPACE_THREE_FLOOR);
        parkingSpaceThree.setNumber(PARKING_SPACE_THREE_NUMBER);

        parkingSpaceFour = new ParkingSpace();
        parkingSpaceFour.setParkingSpaceKey(PARKING_SPACE_FOUR_KEY);
        parkingSpaceFour.setGarageKey(GARAGE_TWO_KEY);
        parkingSpaceFour.setFloor(PARKING_SPACE_FOUR_FLOOR);
        parkingSpaceFour.setNumber(PARKING_SPACE_FOUR_NUMBER);

        Collection<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        Collection<Floor> floors = new ArrayList<>();
        floors.add(floorOne);
        floors.add(floorTwo);

        Collection<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);
        parkingSpaces.add(parkingSpaceThree);
        parkingSpaces.add(parkingSpaceFour);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findByKey(GARAGE_TWO_KEY)).thenReturn(garageTwo);
        when(garageRepository.findAll()).thenReturn(garages);
        doNothing().when(garageRepository).save(any(Garage.class));
        doNothing().when(garageRepository).delete(anyString());

        floorRepository = mock(FloorRepository.class);
        when(floorRepository.findByKey(FLOOR_ONE_KEY)).thenReturn(floorOne);
        when(floorRepository.findByKey(FLOOR_TWO_KEY)).thenReturn(floorTwo);
        when(floorRepository.findAll()).thenReturn(floors);
        doNothing().when(floorRepository).save(any(Floor.class));
        doNothing().when(floorRepository).delete(anyString());

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_ONE_KEY)).thenReturn(parkingSpaceOne);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_TWO_KEY)).thenReturn(parkingSpaceTwo);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_THREE_KEY)).thenReturn(parkingSpaceThree);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_FOUR_KEY)).thenReturn(parkingSpaceFour);
        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);
        doNothing().when(parkingSpaceRepository).save(any(ParkingSpace.class));
        doNothing().when(parkingSpaceRepository).delete(anyString());

        garageService = mock(GarageService.class);
        doNothing().when(garageService).refresh(garageOne.getGarageKey());
        doNothing().when(garageService).refresh(garageTwo.getGarageKey());

        floorSettingsController = new FloorSettingsController(
                garageRepository,
                floorRepository,
                parkingSpaceRepository,
                garageService);
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = floorSettingsController.index(
                null,
                null,
                null,
                null,
                model);

        Collection<Garage> garages = (Collection) model.get("garages");
        assertEquals("settings/floor/index",returnURL);
        assertEquals(garageRepository.findAll(), garages);
        assertNull((String) model.get("successMessage"));
        assertNull((String) model.get("infoMessage"));
        assertNull((String) model.get("warningMessage"));
        assertNull((String) model.get("dangerMessage"));
    }

    @Test
    public void testIndexSuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String successMessage = "SUCCESSSSSSSS";

        String returnURL = floorSettingsController.index(
                successMessage,
                null,
                null,
                null,
                model);

        Collection<Garage> garages = (Collection) model.get("garages");
        assertEquals("settings/floor/index",returnURL);
        assertEquals(garageRepository.findAll(), garages);
        assertTrue(model.containsKey("successMessage"));
        assertEquals(successMessage, model.get("successMessage"));
        assertNull((String) model.get("infoMessage"));
        assertNull((String) model.get("warningMessage"));
        assertNull((String) model.get("dangerMessage"));
    }

    @Test
    public void testIndexInfoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String infoMessage = "Information";

        String returnURL = floorSettingsController.index(
                null,
                infoMessage,
                null,
                null,
                model);

        Collection<Garage> garages = (Collection) model.get("garages");
        assertEquals("settings/floor/index",returnURL);
        assertEquals(garageRepository.findAll(), garages);
        assertTrue(model.containsKey("infoMessage"));
        assertEquals(infoMessage, model.get("infoMessage"));
        assertNull((String) model.get("successMessage"));
        assertNull((String) model.get("warningMessage"));
        assertNull((String) model.get("dangerMessage"));
    }

    @Test
    public void testIndexWarningMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String warningMessage = "warning";

        String returnURL = floorSettingsController.index(
                null,
                null,
                warningMessage,
                null,
                model);

        Collection<Garage> garages = (Collection) model.get("garages");
        assertEquals("settings/floor/index",returnURL);
        assertEquals(garageRepository.findAll(), garages);
        assertTrue(model.containsKey("warningMessage"));
        assertEquals(warningMessage, model.get("warningMessage"));
        assertNull((String) model.get("infoMessage"));
        assertNull((String) model.get("successMessage"));
        assertNull((String) model.get("dangerMessage"));
    }

    @Test
    public void testIndexDangerMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String dangerMessage = "DANGER!";

        String returnURL = floorSettingsController.index(
                null,
                null,
                null,
                dangerMessage,
                model);

        Collection<Garage> garages = (Collection) model.get("garages");
        assertEquals("settings/floor/index",returnURL);
        assertEquals(garageRepository.findAll(), garages);
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));
        assertNull((String) model.get("infoMessage"));
        assertNull((String) model.get("successMessage"));
        assertNull((String) model.get("warningMessage"));
    }

    @Test
    public void testGarageNoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        List<Floor> floors = new ArrayList<> ();
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);
        floors.add(floorOne);

        String returnURL = floorSettingsController.garage(
                garageOne.getGarageKey(),
                null,
                null,
                null,
                null,
                model);

        List<Floor> returnFloors = (ArrayList<Floor>)model.get("floors");

        assertEquals(garageOne, model.get("garage"));
        assertEquals(1, returnFloors.size());
        assertEquals(floors, returnFloors);
        assertEquals("settings/floor/garage", returnURL);
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testGarageSuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        List<Floor> floors = new ArrayList<> ();
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);
        floors.add(floorTwo);

        String successMessage = "It's success, Bruh!";

        String returnURL = floorSettingsController.garage(
                garageTwo.getGarageKey(),
                successMessage,
                null,
                null,
                null,
                model);

        List<Floor> returnFloors = (ArrayList<Floor>)model.get("floors");

        assertEquals(garageTwo, model.get("garage"));
        assertEquals(1, returnFloors.size());
        assertEquals(floors, returnFloors);
        assertEquals("settings/floor/garage", returnURL);
        assertTrue(model.containsKey("successMessage"));

        assertEquals(successMessage, model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testGarageInfoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        List<Floor> floors = new ArrayList<> ();
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);
        floors.add(floorTwo);

        String infoMessage = "It's INFO, Bruh!";

        String returnURL = floorSettingsController.garage(
                garageTwo.getGarageKey(),
                null,
                infoMessage,
                null,
                null,
                model);

        List<Floor> returnFloors = (ArrayList<Floor>)model.get("floors");

        assertEquals(garageTwo, model.get("garage"));
        assertEquals(1, returnFloors.size());
        assertEquals(floors, returnFloors);
        assertEquals("settings/floor/garage", returnURL);
        assertTrue(model.containsKey("infoMessage"));

        assertEquals(infoMessage, model.get("infoMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testGarageWarningMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        List<Floor> floors = new ArrayList<> ();
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);
        floors.add(floorTwo);

        String warningMessage = "It's a warning, Bruh!";

        String returnURL = floorSettingsController.garage(
                garageTwo.getGarageKey(),
                null,
                null,
                warningMessage,
                null,
                model);

        List<Floor> returnFloors = (ArrayList<Floor>)model.get("floors");

        assertEquals(garageTwo, model.get("garage"));
        assertEquals(1, returnFloors.size());
        assertEquals(floors, returnFloors);
        assertEquals("settings/floor/garage", returnURL);
        assertTrue(model.containsKey("warningMessage"));

        assertEquals(warningMessage, model.get("warningMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testGarageDangerMessage() {
        ExtendedModelMap model = new ExtendedModelMap();

        List<Floor> floors = new ArrayList<> ();
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);
        floors.add(floorTwo);

        String dangerMessage = "It's DANGEROUS, Bruh!";

        String returnURL = floorSettingsController.garage(
                garageTwo.getGarageKey(),
                null,
                null,
                null,
                dangerMessage,
                model);

        List<Floor> returnFloors = (ArrayList<Floor>)model.get("floors");

        assertEquals(garageTwo, model.get("garage"));
        assertEquals(1, returnFloors.size());
        assertEquals(floors, returnFloors);
        assertEquals("settings/floor/garage", returnURL);
        assertTrue(model.containsKey("dangerMessage"));

        assertEquals(dangerMessage, model.get("dangerMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
    }

    @Test
    public void testCreate_Get_NoMessage() {
        ExtendedModelMap model =new ExtendedModelMap();

        String returnURL = floorSettingsController.create(
                null,
                null,
                null,
                null,
                garageOne.getGarageKey(),
                model
        );

        Floor returnFloor = (Floor)model.get("floor");

        assertEquals("settings/floor/create", returnURL);
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("garage"));
        assertEquals(garageOne, model.get("garage"));
        assertEquals(garageOne.getGarageKey(), returnFloor.getGarageKey());
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testCreate_Get_SuccessMessage() {
        ExtendedModelMap model =new ExtendedModelMap();
        String successMessage = "It's success, Bruh";

        String returnURL = floorSettingsController.create(
                successMessage,
                null,
                null,
                null,
                garageOne.getGarageKey(),
                model
        );

        Floor returnFloor = (Floor)model.get("floor");

        assertEquals("settings/floor/create", returnURL);
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("garage"));
        assertEquals(garageOne, model.get("garage"));
        assertEquals(garageOne.getGarageKey(), returnFloor.getGarageKey());
        assertTrue(model.containsKey("successMessage"));
        assertEquals(successMessage, model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testCreate_Get_InfoMessage() {
        ExtendedModelMap model =new ExtendedModelMap();
        String infoMessage = "It's INFORMATION, Bruh";

        String returnURL = floorSettingsController.create(
                null,
                infoMessage,
                null,
                null,
                garageOne.getGarageKey(),
                model
        );

        Floor returnFloor = (Floor)model.get("floor");

        assertEquals("settings/floor/create", returnURL);
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("garage"));
        assertEquals(garageOne, model.get("garage"));
        assertEquals(garageOne.getGarageKey(), returnFloor.getGarageKey());
        assertTrue(model.containsKey("infoMessage"));
        assertEquals(infoMessage, model.get("infoMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("warningMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testCreate_Get_WarningMessage() {
        ExtendedModelMap model =new ExtendedModelMap();
        String warningMessage = "It's warning, Bruh";

        String returnURL = floorSettingsController.create(
                null,
                null,
                warningMessage,
                null,
                garageOne.getGarageKey(),
                model
        );

        Floor returnFloor = (Floor)model.get("floor");

        assertEquals("settings/floor/create", returnURL);
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("garage"));
        assertEquals(garageOne, model.get("garage"));
        assertEquals(garageOne.getGarageKey(), returnFloor.getGarageKey());
        assertTrue(model.containsKey("warningMessage"));
        assertEquals(warningMessage, model.get("warningMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("dangerMessage"));
    }

    @Test
    public void testCreate_Get_DangerMessage() {
        ExtendedModelMap model =new ExtendedModelMap();
        String dangerMessage = "It's danger, Bruh";

        String returnURL = floorSettingsController.create(
                null,
                null,
                null,
                dangerMessage,
                garageOne.getGarageKey(),
                model
        );

        Floor returnFloor = (Floor)model.get("floor");

        assertEquals("settings/floor/create", returnURL);
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("garage"));
        assertEquals(garageOne, model.get("garage"));
        assertEquals(garageOne.getGarageKey(), returnFloor.getGarageKey());
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));
        assertNull(model.get("successMessage"));
        assertNull(model.get("infoMessage"));
        assertNull(model.get("warningMessage"));
    }

    @Test
    public void testCreate_Post_NullGarageKey() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        Floor floor= new Floor();

        String returnURL = floorSettingsController.create(
                floor,
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/floor/index",
                returnURL);
        assertEquals("redirect:/settings/floor/index",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals("The garage key cannot be null or empty.",
                redirectAttributes.get("dangerMessage"));
    }

    @Test
    public void testCreate_Post_EmptyGarageKey() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        Floor floor= new Floor();
        floor.setGarageKey("");

        String returnURL = floorSettingsController.create(
                floor,
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/floor/index",
                returnURL);
        assertFalse(redirectAttributes.containsKey("successMessage"));
        assertTrue(redirectAttributes.containsKey("dangerMessage"));

        assertEquals("redirect:/settings/floor/index",returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals("The garage key cannot be null or empty.",
                redirectAttributes.get("dangerMessage"));
    }

    @Test
    public void testCreate_Post_Duplicate() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        Floor floor= new Floor();
        floor.setGarageKey(garageOne.getGarageKey());
        floor.setTotalSpaces(2);

        when(floorRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        String returnURL = floorSettingsController.create(
                floor,
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/floor/create/" + garageOne.getGarageKey(),
                returnURL);
        assertFalse(redirectAttributes.containsKey("successMessage"));
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals("successMessage",
                "The floor " +
                        floor.getNumber() +
                        " already exists in " +
                        garageOne.getName(), redirectAttributes.get("dangerMessage"));
    }

    @Test
    public void testCreate_Post_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        Floor floor= new Floor();
        floor.setGarageKey(garageOne.getGarageKey());
        floor.setTotalSpaces(2);

        when(floorRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        String returnURL = floorSettingsController.create(
                floor,
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/floor/garage/" + garageOne.getGarageKey(),
                returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertFalse(redirectAttributes.containsKey("dangerMessage"));
        assertEquals("successMessage",
                "The floor, " +
                        floor.getNumber() +
                        ", was successfully created in garage " +
                        garageOne.getName(), redirectAttributes.get("successMessage"));
    }
}
