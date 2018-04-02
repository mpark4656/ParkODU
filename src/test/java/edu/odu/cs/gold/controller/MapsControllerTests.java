package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.Location;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
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

public class MapsControllerTests {

    private MapsController mapsController;
    private GarageRepository garageRepository;

    private Location locationOne;
    private Location locationTwo;
    private Garage garageOne;
    private Garage garageTwo;

    private GarageService garageService;

    private static final Double LOCATION_ONE_LATITUDE = 50.0;
    private static final Double LOCATION_ONE_LONGITUDE = 50.0;

    private static final Double LOCATION_TWO_LATITUDE = 51.0;
    private static final Double LOCATION_TWO_LONGITUDE = 51.0;

    private static String GARAGE_ONE_KEY = "a0000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "a0000000000000000000000000000002";


    @Before
    public void setup() {
        mapsController = new MapsController(garageRepository);

        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setLatitude(LOCATION_ONE_LATITUDE);
        garageOne.setLongitude(LOCATION_ONE_LONGITUDE);

        List<Garage> garages = new ArrayList<>();
        garages.add(garageOne);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findAll()).thenReturn(garages);

        locationOne = new Location(LOCATION_ONE_LATITUDE,LOCATION_ONE_LONGITUDE);

        mapsController = new MapsController(garageRepository);

        garageService = mock(GarageService.class);
        doNothing().when(garageService).refresh(anyString());
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = mapsController.index(model);
        assertEquals("navigate/index", returnURL);
    }

    @Test
    public void testNavigate_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = mapsController.navigate(model,LOCATION_TWO_LATITUDE,LOCATION_TWO_LONGITUDE,GARAGE_ONE_KEY);
        assertEquals("maps/navigate/index",returnURL);

        assertTrue(model.containsAttribute("travelMode"));
        assertTrue(model.containsAttribute("startingLocation"));
        assertTrue(model.containsAttribute("destination"));


    }
}