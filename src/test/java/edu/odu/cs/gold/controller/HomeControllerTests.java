package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.EventRepository;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.service.GarageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeControllerTests {

    private static String GARAGE_ONE_KEY = "a0000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "a0000000000000000000000000000002";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";

    private HomeController homeController;

    private GarageRepository garageRepository;
    private EventRepository eventRepository;

    private Garage garageOne;
    private Garage garageTwo;

    @Before
    public void setup() {

        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        garageTwo = new Garage();
        garageTwo.setGarageKey(GARAGE_TWO_KEY);
        garageTwo.setName(GARAGE_TWO_NAME);

        List<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findAll()).thenReturn(garages);

        eventRepository = mock(EventRepository.class);
        doNothing().when(eventRepository).save(any(Event.class));
        doNothing().when(eventRepository).delete(anyString());

        homeController = new HomeController(garageRepository, eventRepository);
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = homeController.index(model, null);
        assertEquals("home/index", returnURL);
    }

    @Test
    public void testSettings() {
        String returnURL = homeController.settings();
        assertEquals("settings/index", returnURL);
    }

    @Test
    public void testLogin() {
        String returnURL = homeController.login();
        assertEquals("home/login", returnURL);
    }

    @Test
    public void testLoginError() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = homeController.loginError(model);
        assertEquals("home/login", returnURL);
    }

    @Test
    public void testLogout() {
        String returnURL = homeController.logout();
        assertEquals("redirect:/", returnURL);
    }
}
