package edu.odu.cs.gold.controller;

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
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class GarageControllerTests {

    private static String GARAGE_ONE_KEY = "00000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "00000000000000000000000000000002";
    private static String FLOOR_ONE_KEY = "00000000000000000000000000000003";
    private static String FLOOR_TWO_KEY = "00000000000000000000000000000004";
    private static String PARKING_SPACE_ONE_KEY = "00000000000000000000000000000005";
    private static String PARKING_SPACE_TWO_KEY = "00000000000000000000000000000006";
    private static String PARKING_SPACE_THREE_KEY = "00000000000000000000000000000007";
    private static String PARKING_SPACE_FOUR_KEY = "00000000000000000000000000000008";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";
    private static String FLOOR_ONE_NUMBER = "1";
    private static String FLOOR_TWO_NUMBER = "1";
    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "1";
    private static String PARKING_SPACE_THREE_FLOOR = "1";
    private static String PARKING_SPACE_FOUR_FLOOR = "1";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 2;
    private static Integer PARKING_SPACE_THREE_NUMBER = 1;
    private static Integer PARKING_SPACE_FOUR_NUMBER = 2;

    private GarageController garageController;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private GarageService garageService;

    private Garage garageOne;
    private Garage garageTwo;
    private Floor floorOne;
    private Floor floorTwo;
    private ParkingSpace parkingSpaceOne;
    private ParkingSpace  parkingSpaceTwo;
    private ParkingSpace  parkingSpaceThree;
    private ParkingSpace  parkingSpaceFour;

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

        garageService = new GarageService(garageRepository, floorRepository, parkingSpaceRepository);
        garageController = new GarageController(garageRepository, floorRepository, garageService);
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageController.index(model);
        Collection<Garage> garages = (Collection)model.get("garages");

        // Check that the returned string is correct
        assertEquals("garage/index", returnURL);

        // Check the size of the collection is the same
        assertTrue(garages.size() == 2);

        // Check that the garages are equal but not same reference
        assertEquals(garages, garageRepository.findAll());
    }

    @Test
    public void testDetails() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageController.details(garageOne.getGarageKey(), model);
        Garage garage = (Garage)model.get("garage");
        Collection<Floor> floors = (Collection)model.get("floors");

        // Test with garageOne
        assertEquals("garage/details", returnURL);
        assertEquals(garage, garageOne);
        assertEquals(floors,
                floorRepository.findByPredicate(Predicates.equal("garageKey", garageOne.getGarageKey())));

        // Test with garageTwo
        model = new ExtendedModelMap();
        returnURL = garageController.details(garageTwo.getGarageKey(), model);
        garage = (Garage)model.get("garage");
        floors = (Collection)model.get("floors");

        assertEquals("garage/details", returnURL);
        assertEquals(garage, garageTwo);
        assertEquals(floors,
                floorRepository.findByPredicate(Predicates.equal("garageKey", garageTwo.getGarageKey())));
    }
}
