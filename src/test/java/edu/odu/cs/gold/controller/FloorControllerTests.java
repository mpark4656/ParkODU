package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.FloorStatisticService;
import edu.odu.cs.gold.service.GarageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class FloorControllerTests {

    private static final String GARAGE_ONE_KEY = "a0000000000000000000000000000001";
    private static final String GARAGE_ONE_NAME = "Garage1";

    private static final String FLOOR_ONE_KEY = "b0000000000000000000000000000001";
    private static final String FLOOR_ONE_NUMBER = "1";

    private static final String PARKING_SPACE_ONE_KEY = "c0000000000000000000000000000001";
    private static final String PARKING_SPACE_TWO_KEY = "c0000000000000000000000000000002";

    private static final Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static final Integer PARKING_SPACE_TWO_NUMBER = 1;

    private static final Double FLOOR_STATISTIC_ONE_CAPACITY = 50.0;
    private static final Double FLOOR_STATISTIC_TWO_CAPACITY = 50.0;

    private FloorController floorController;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private FloorStatisticService floorStatisticService;

    private Garage garageOne;
    private Floor floorOne;
    private ParkingSpace parkingSpaceOne;
    private ParkingSpace parkingSpaceTwo;
    private FloorStatistic floorStatisticOne;
    private FloorStatistic floorStatisticTwo;

    @Before
    public void setup() {

        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        floorOne = new Floor();
        floorOne.setFloorKey(FLOOR_ONE_KEY);
        floorOne.setGarageKey(GARAGE_ONE_KEY);
        floorOne.setNumber(FLOOR_ONE_NUMBER);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);

        floorStatisticOne = new FloorStatistic();
        floorStatisticOne.setFloorStatisticKey(UUID.randomUUID().toString());
        floorStatisticOne.setFloorKey(FLOOR_ONE_KEY);
        floorStatisticOne.setCapacity(FLOOR_STATISTIC_ONE_CAPACITY);
        floorStatisticOne.setTimestamp(new Date());

        floorStatisticTwo = new FloorStatistic();
        floorStatisticTwo.setFloorStatisticKey(UUID.randomUUID().toString());
        floorStatisticTwo.setFloorKey(FLOOR_ONE_KEY);
        floorStatisticTwo.setCapacity(FLOOR_STATISTIC_TWO_CAPACITY);
        floorStatisticTwo.setTimestamp(new Date());

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);

        floorRepository = mock(FloorRepository.class);
        when(floorRepository.findByKey(FLOOR_ONE_KEY)).thenReturn(floorOne);

        List<ParkingSpace> parkingSpaces = new ArrayList<>();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);
        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);

        ArrayList<FloorStatistic> floorStatistics = new ArrayList<>();
        floorStatistics.add(floorStatisticOne);
        floorStatistics.add(floorStatisticTwo);
        floorStatisticRepository = mock(FloorStatisticRepository.class);
        when(floorStatisticRepository.findByPredicate(any(Predicate.class))).thenReturn(floorStatistics);

        floorStatisticService = new FloorStatisticService(floorStatisticRepository);
        floorStatisticService = mock(FloorStatisticService.class);
        when(floorStatisticService.findAverageFloorCapacityByHour(anyString())).thenReturn(floorStatistics);

        floorController = new FloorController(
                garageRepository,
                floorRepository,
                parkingSpaceRepository,
                floorStatisticService);
    }

    @Test
    public void testDetails() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = floorController.details(FLOOR_ONE_KEY, model);

        assertEquals("floor/details", returnURL);
        assertTrue(model.get("garage").equals(garageOne));
        assertTrue(model.get("floor").equals(floorOne));
        assertTrue(model.containsKey("currentTime"));

        Collection<ParkingSpace> parkingSpaces = (Collection)model.get("parkingSpaces");
        assertTrue(parkingSpaces.size() == 2);

        assertTrue(model.containsKey("availableParkingSpaces"));

        Collection<FloorStatistic> floorStatistics = (Collection)model.get("floorStatistics");
        assertTrue(floorStatistics.size() == 2);

        assertEquals(FLOOR_STATISTIC_ONE_CAPACITY + "," + FLOOR_STATISTIC_TWO_CAPACITY + ",", model.get("dataString"));
        assertTrue(model.containsKey("labelString"));
    }
}
