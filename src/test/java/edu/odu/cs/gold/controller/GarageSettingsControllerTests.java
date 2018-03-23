package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class GarageSettingsControllerTests {

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

    private GarageSettingsController garageSettingsController;
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

        garageSettingsController = new GarageSettingsController(
                garageRepository,
                floorRepository,
                parkingSpaceRepository);
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageSettingsController.index(null, null, null, null, model);

        assertEquals("settings/garage/index", returnURL);
        Collection<Garage> garages = (Collection)model.get("garages");
        assertTrue(garages.size() == 2); // Asserts that the number of Garages to be displayed in index.html is 2
    }

    @Test
    public void testCreate_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageSettingsController.create(model);

        assertEquals("settings/garage/create", returnURL);
        assertTrue(model.containsKey("garage"));
    }

    @Test
    public void testCreate_Post_Success() {
        Garage newGarage = new Garage();
        newGarage.setGarageKey("0000000000000000000000000000003");
        newGarage.setName("NewGarage");

        // This mocks that if countByPredicate is called, then it will return an integer value of 0 to mock that there is no duplicates with the same garageKey or name as the new Garage
        when(garageRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = garageSettingsController.create(newGarage, model, redirectAttributes);

        assertEquals("redirect:/settings/garage/index", returnURL);
        assertFalse(model.containsKey("garage"));
        assertTrue(redirectAttributes.containsKey("successMessage"));
    }

    @Test
    public void testCreate_Post_Duplicate() {
        Garage newGarage = new Garage();
        newGarage.setGarageKey(GARAGE_ONE_KEY);
        newGarage.setName(GARAGE_ONE_NAME);

        // This mocks that if countByPredicate is called, then it will return an integer value of 1 to mock that there exists a Garage with the same name
        when(garageRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = garageSettingsController.create(newGarage, model, redirectAttributes);

        assertEquals("settings/garage/create", returnURL);
        assertTrue(model.containsKey("garage"));
        assertTrue(model.containsKey("dangerMessage"));
    }

    @Test
    public void testEdit_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageSettingsController.edit(GARAGE_ONE_KEY, model);

        assertEquals("settings/garage/edit", returnURL);
        assertTrue(model.get("garage").equals(garageOne));
    }

    @Test
    public void testEdit_Post_Success() {
        Garage editedGarage = new Garage();
        editedGarage.setGarageKey(GARAGE_ONE_KEY);
        editedGarage.setName("EditedTest1");

        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = garageSettingsController.edit(editedGarage, model, redirectAttributes);

        assertEquals("redirect:/settings/garage/index", returnURL);
        assertFalse(model.containsKey("garage"));
        assertTrue(redirectAttributes.containsKey("successMessage"));
    }

    @Test
    public void testEdit_Post_Duplicate() {

        Garage editedGarage = new Garage();
        editedGarage.setGarageKey(GARAGE_ONE_KEY);
        editedGarage.setName(GARAGE_ONE_NAME);

        // This mocks that if countByPredicate is called, then it will return an integer value of 1 to mock that there exists a Garage with the same name
        when(garageRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = garageSettingsController.edit(editedGarage, model, redirectAttributes);

        assertEquals("settings/garage/edit", returnURL);
        assertTrue(model.containsKey("garage"));
        assertTrue(model.containsKey("dangerMessage"));
    }

    @Test
    public void testDelete_Post_Success() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = garageSettingsController.delete(GARAGE_ONE_KEY, redirectAttributes);

        assertEquals("redirect:/settings/garage/index", returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
    }

    @Test
    public void testDelete_Post_Fail() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String randomUUID = UUID.randomUUID().toString();
        when(garageRepository.findByKey(randomUUID)).thenReturn(null);
        String returnURL = garageSettingsController.delete(randomUUID, redirectAttributes);

        assertEquals("redirect:/settings/garage/index", returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
    }
}
