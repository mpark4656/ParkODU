package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FloorStatisticControllerTest {

    private static String GARAGE_ONE_KEY = "00000000000000000000000000000001";
    private static String FLOOR_ONE_KEY = "00000000000000000000000000000003";
    private static String FLOOR_STATISTIC_ONE_KEY = "f0000000000000000000000000000001";
    private static String PARKING_SPACE_ONE_KEY = "00000000000000000000000000000005";
    private static String PARKING_SPACE_TWO_KEY = "00000000000000000000000000000006";
    private static String PARKING_SPACE_THREE_KEY = "00000000000000000000000000000007";
    private static String PARKING_SPACE_FOUR_KEY = "00000000000000000000000000000008";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String FLOOR_ONE_NUMBER = "1";
    private static Double FLOOR_STATISTIC_ONE_CAPACITY = 50.0;

    private static Date FLOOR_STATISTIC_ONE_TIMESTAMP = new Date();

    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "1";
    private static String PARKING_SPACE_THREE_FLOOR = "1";
    private static String PARKING_SPACE_FOUR_FLOOR = "1";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 2;
    private static Integer PARKING_SPACE_THREE_NUMBER = 3;
    private static Integer PARKING_SPACE_FOUR_NUMBER = 4;

    private static boolean PARKING_SPACE_ONE_AVAILABLE = true;
    private static boolean PARKING_SPACE_TWO_AVAILABLE = true;
    private static boolean PARKING_SPACE_THREE_AVAILABLE = false;
    private static boolean PARKING_SPACE_FOUR_AVAILABLE = false;

    private FloorStatisticController floorStatisticController;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private GarageService garageService;

    private Garage garageOne;
    private Floor floorOne;
    private FloorStatistic floorStatisticOne;
    private ParkingSpace  parkingSpaceOne;
    private ParkingSpace  parkingSpaceTwo;
    private ParkingSpace  parkingSpaceThree;
    private ParkingSpace  parkingSpaceFour;

    @Before
    public void Setup() {
        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        floorOne = new Floor();
        floorOne.setFloorKey(FLOOR_ONE_KEY);
        floorOne.setGarageKey(GARAGE_ONE_KEY);
        floorOne.setNumber(FLOOR_ONE_NUMBER);

        floorStatisticOne = new FloorStatistic();
        floorStatisticOne.setFloorStatisticKey(FLOOR_STATISTIC_ONE_KEY);
        floorStatisticOne.setFloorKey(FLOOR_ONE_KEY);
        floorStatisticOne.setCapacity(FLOOR_STATISTIC_ONE_CAPACITY);
        floorStatisticOne.setTimestamp(FLOOR_STATISTIC_ONE_TIMESTAMP);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceOne.setFloor(PARKING_SPACE_ONE_FLOOR);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);
        parkingSpaceOne.setAvailable(PARKING_SPACE_ONE_AVAILABLE);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);
        parkingSpaceTwo.setAvailable(PARKING_SPACE_TWO_AVAILABLE);

        parkingSpaceThree = new ParkingSpace();
        parkingSpaceThree.setParkingSpaceKey(PARKING_SPACE_THREE_KEY);
        parkingSpaceThree.setFloor(PARKING_SPACE_THREE_FLOOR);
        parkingSpaceThree.setNumber(PARKING_SPACE_THREE_NUMBER);
        parkingSpaceThree.setAvailable(PARKING_SPACE_THREE_AVAILABLE);

        parkingSpaceFour = new ParkingSpace();
        parkingSpaceFour.setParkingSpaceKey(PARKING_SPACE_FOUR_KEY);
        parkingSpaceFour.setFloor(PARKING_SPACE_FOUR_FLOOR);
        parkingSpaceFour.setNumber(PARKING_SPACE_FOUR_NUMBER);
        parkingSpaceFour.setAvailable(PARKING_SPACE_FOUR_AVAILABLE);

        Collection<Garage> garages = new ArrayList<>();
        garages.add(garageOne);

        Collection<Floor> floors = new ArrayList<>();
        floors.add(floorOne);

        List<FloorStatistic> floorStatistics = new ArrayList<>();
        floorStatistics.add(floorStatisticOne);

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);
        parkingSpaces.add(parkingSpaceThree);
        parkingSpaces.add(parkingSpaceFour);


        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findAll()).thenReturn(garages);

        floorRepository = mock(FloorRepository.class);
        when(floorRepository.findByKey(FLOOR_ONE_KEY)).thenReturn(floorOne);
        when(floorRepository.findAll()).thenReturn(floors);

        floorStatisticRepository = mock(FloorStatisticRepository.class);
        when(floorStatisticRepository.findByPredicate(any(Predicate.class))).thenReturn(floorStatistics);

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_ONE_KEY)).thenReturn(parkingSpaceOne);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_TWO_KEY)).thenReturn(parkingSpaceTwo);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_THREE_KEY)).thenReturn(parkingSpaceThree);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_FOUR_KEY)).thenReturn(parkingSpaceFour);
        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);
        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);

        garageService = new GarageService(garageRepository, floorRepository, parkingSpaceRepository);

        garageService = mock(GarageService.class);
        doNothing().when(garageService).refresh(GARAGE_ONE_KEY);
        doNothing().when(garageService).save(any(Garage.class));

       floorStatisticController = new FloorStatisticController(
                garageRepository,
                floorRepository,
                parkingSpaceRepository,
                floorStatisticRepository,
                garageService
        );
    }

    @Test
    public void testIndex(){
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = floorStatisticController.index(model);
        assertEquals("garage/index", returnURL);
        Collection<Garage> garages = (Collection)model.get("garage");
        assertTrue(garages.size() == 1);
    }

    @Test
    public void testDetails_FloorKey() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = floorStatisticController.details(FLOOR_ONE_KEY,model);
        assertEquals("floorstatistic/details", returnURL);

        assertTrue(model.containsKey("garage"));
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("floorStatistics"));
        assertTrue(model.containsKey("dataString"));
        assertTrue(model.containsKey("labelString"));

    }

    @Test
    public void testDetails_GarageKey_FloorKey() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = floorStatisticController.floorDetails(GARAGE_ONE_KEY,FLOOR_ONE_KEY,model);
        assertEquals("floor/details", returnURL);

        assertEquals(model.get("availableParkingSpaces"),"1,2,");

        assertTrue(model.containsKey("garage"));
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("parkingSpaces"));
        assertTrue(model.containsKey("availableParkingSpaces"));
    }
}
