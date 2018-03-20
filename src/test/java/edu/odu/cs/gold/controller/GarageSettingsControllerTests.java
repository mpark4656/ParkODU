package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.GarageRepository;
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

    private static String GARAGE_ONE_KEY = "00000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "00000000000000000000000000000002";

    private static String GARAGE_ONE_NAME = "Test1";
    private static String GARAGE_TWO_NAME = "Test2";

    private GarageSettingsController garageSettingsController;
    private GarageRepository garageRepository;

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

        Collection<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findByKey(GARAGE_TWO_KEY)).thenReturn(garageTwo);
        when(garageRepository.findAll()).thenReturn(garages);
        doNothing().when(garageRepository).save(any(Garage.class));
        doNothing().when(garageRepository).delete(anyString());

        garageSettingsController = new GarageSettingsController(garageRepository);
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
