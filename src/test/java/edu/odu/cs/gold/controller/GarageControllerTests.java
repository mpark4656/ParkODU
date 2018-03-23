package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
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
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class GarageControllerTests {

    private static String GARAGE_ONE_KEY = "a0000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "a0000000000000000000000000000002";
    private static String FLOOR_ONE_KEY = "b0000000000000000000000000000001";
    private static String FLOOR_TWO_KEY = "b0000000000000000000000000000002";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";
    private static String FLOOR_ONE_NUMBER = "1";
    private static String FLOOR_TWO_NUMBER = "2";

    private GarageController garageController;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private GarageService garageService;

    private Garage garageOne;
    private Garage garageTwo;
    private Floor floorOne;
    private Floor floorTwo;

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

        Collection<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        List<Floor> floors = new ArrayList<>();
        floors.add(floorOne);
        floors.add(floorTwo);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findAll()).thenReturn(garages);

        floorRepository = mock(FloorRepository.class);
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(floors);

        garageService = mock(GarageService.class);
        doNothing().when(garageService).refresh(anyString());

        garageController = new GarageController(
                garageRepository,
                floorRepository,
                garageService);
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageController.index(model);

        assertEquals("garage/index", returnURL);
        Collection<Garage> garages = (Collection)model.get("garages");
        assertTrue(garages.size() == 2); // Asserts that the number of Garages to be displayed in index.html is 2
    }

    @Test
    public void testDetails() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = garageController.details(GARAGE_ONE_KEY, model);

        assertEquals("garage/details", returnURL);
        assertTrue(model.get("garage").equals(garageOne));
        Collection<Floor> floors = (Collection)model.get("floors");
        assertTrue(floors.size() == 2);
    }
}
