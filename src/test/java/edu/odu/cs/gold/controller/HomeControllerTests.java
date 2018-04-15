package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import edu.odu.cs.gold.service.GarageStatisticService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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
    private UserRepository userRepository;
    private GarageStatisticService garageStatisticService;
    private GarageStatisticRepository garageStatisticRepository;

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

        ArrayList<GarageStatistic> garageStatistics = new ArrayList<>();
        Date date = new Date();

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findAll()).thenReturn(garages);

        eventRepository = mock(EventRepository.class);
        doNothing().when(eventRepository).save(any(Event.class));
        doNothing().when(eventRepository).delete(anyString());

        userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(anyString());
        doNothing().when(userRepository).save(any(User.class));

        garageStatisticRepository = mock(GarageStatisticRepository.class);
        when(garageStatisticRepository.findAll()).thenReturn(garageStatistics);

        garageStatisticService = mock(GarageStatisticService.class);
        when(garageStatisticService
                .findGarageCapacityByDate(GARAGE_ONE_KEY,date)).thenReturn(garageStatistics);


        homeController = new HomeController(
                garageRepository,
                eventRepository,
                userRepository,
                garageStatisticRepository,
                garageStatisticService
        );
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
