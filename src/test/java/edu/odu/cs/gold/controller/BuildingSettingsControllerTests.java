package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.repository.BuildingRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildingSettingsControllerTests {

    private String BUILDING_ONE_KEY = "a000000000000000000000001";
    private String BUILDING_TWO_KEY = "a000000000000000000000002";

    private String BUILDING_ONE_NAME = "BUILDINGONE";
    private String BUILDING_TWO_NAME = "BUILDINGTWO";

    private BuildingSettingsController buildingSettingsController;
    private BuildingRepository buildingRepository;
    private Building buildingOne;
    private Building buildingTwo;

    @Before
    public void setup() {
        buildingOne = new Building();
        buildingOne.setBuildingKey(BUILDING_ONE_KEY);
        buildingOne.setName(BUILDING_ONE_NAME);

        buildingTwo = new Building();
        buildingTwo.setBuildingKey(BUILDING_TWO_KEY);
        buildingTwo.setName(BUILDING_TWO_NAME);

        List<Building> buildings = new ArrayList<>();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        buildingRepository = mock(BuildingRepository.class);
        when(buildingRepository.findAll()).thenReturn(buildings);
        when(buildingRepository.findByKey(BUILDING_ONE_KEY)).thenReturn(buildingOne);
        when(buildingRepository.findByKey(BUILDING_TWO_KEY)).thenReturn(buildingTwo);
        doNothing().when(buildingRepository).save(any(Building.class));
        doNothing().when(buildingRepository).delete(anyString());

        buildingSettingsController = new BuildingSettingsController(buildingRepository);
    }

    @Test
    public void testIndex_NoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = buildingSettingsController.index(
                null,
                null,
                null,
                null,
                model
        );

        List<Building> buildings = new ArrayList<> ();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("settings/building/index", returnURL);
        assertTrue(model.containsKey("buildings"));
        assertEquals(buildings, model.get("buildings"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_SuccessMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String successMessage = "SucCeSs";

        String returnURL = buildingSettingsController.index(
                successMessage,
                null,
                null,
                null,
                model
        );

        List<Building> buildings = new ArrayList<> ();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("settings/building/index", returnURL);
        assertTrue(model.containsKey("buildings"));
        assertEquals(buildings, model.get("buildings"));
        assertTrue(model.containsKey("successMessage"));
        assertEquals(successMessage, model.get("successMessage"));

        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_InfoMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String infoMessage = "iNfOMessage";

        String returnURL = buildingSettingsController.index(
                null,
                infoMessage,
                null,
                null,
                model
        );

        List<Building> buildings = new ArrayList<> ();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("settings/building/index", returnURL);
        assertTrue(model.containsKey("buildings"));
        assertEquals(buildings, model.get("buildings"));
        assertTrue(model.containsKey("infoMessage"));
        assertEquals(infoMessage, model.get("infoMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("warningMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_WarningMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String warningMessage = "WarningMessage";

        String returnURL = buildingSettingsController.index(
                null,
                null,
                warningMessage,
                null,
                model
        );

        List<Building> buildings = new ArrayList<> ();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("settings/building/index", returnURL);
        assertTrue(model.containsKey("buildings"));
        assertEquals(buildings, model.get("buildings"));
        assertTrue(model.containsKey("warningMessage"));
        assertEquals(warningMessage, model.get("warningMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("dangerMessage"));
    }

    @Test
    public void testIndex_DangerMessage() {
        ExtendedModelMap model = new ExtendedModelMap();
        String dangerMessage = "DANGERMessage";

        String returnURL = buildingSettingsController.index(
                null,
                null,
                null,
                dangerMessage,
                model
        );

        List<Building> buildings = new ArrayList<> ();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("settings/building/index", returnURL);
        assertTrue(model.containsKey("buildings"));
        assertEquals(buildings, model.get("buildings"));
        assertTrue(model.containsKey("dangerMessage"));
        assertEquals(dangerMessage, model.get("dangerMessage"));

        assertFalse(model.containsKey("successMessage"));
        assertFalse(model.containsKey("infoMessage"));
        assertFalse(model.containsKey("warningMessage"));
    }

    @Test
    public void testCreate_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL= buildingSettingsController.create(
                model
        );

        assertTrue(model.containsKey("building"));
        assertEquals("settings/building/create", returnURL);

        Building building = (Building) model.get("building");
        assertNotNull(building.getBuildingKey());
        assertFalse(building.getBuildingKey().isEmpty());
    }

    @Test
    public void testCreate_Post_Successful() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(buildingRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        String returnURL = buildingSettingsController.create(
                buildingOne,
                model,
                redirectAttributes
        );

        assertEquals("redirect:/settings/building/index", returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                "The Building " + buildingOne.getName() + " was successfully created.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testCreate_Post_Duplicate() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(buildingRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        String returnURL = buildingSettingsController.create(
                buildingOne,
                model,
                redirectAttributes
        );

        assertEquals("settings/building/create", returnURL);
        assertTrue(model.containsKey("building"));
        assertTrue(model.containsKey("dangerMessage"));

        assertEquals(buildingOne, model.get("building"));
        assertEquals(
                "A Building with the name " + buildingOne.getName() + " already exists.",
                model.get("dangerMessage")
        );
    }

    @Test
    public void testEdit_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = buildingSettingsController.edit(
                buildingOne.getBuildingKey(),
                model
        );

        assertEquals("settings/building/edit", returnURL);
        assertTrue(model.containsKey("building"));

        assertEquals(buildingOne, model.get("building"));
    }

    @Test
    public void testEdit_Post_Successful() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(buildingRepository.countByPredicate(any(Predicate.class))).thenReturn(0);

        String returnURL = buildingSettingsController.edit(
                buildingOne,
                model,
                redirectAttributes
        );

        assertEquals(
                "The Building " + buildingOne.getName() + " was successfully updated.",
                redirectAttributes.get("successMessage")
        );
        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals("redirect:/settings/building/index", returnURL);
    }

    @Test
    public void testEdit_Post_Duplicate() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        when(buildingRepository.countByPredicate(any(Predicate.class))).thenReturn(1);

        String returnURL = buildingSettingsController.edit(
                buildingOne,
                model,
                redirectAttributes
        );

        assertEquals(
                "A Building with the name " + buildingOne.getName() + " already exists.",
                model.get("dangerMessage")
        );
        assertTrue(model.containsKey("dangerMessage"));
        assertTrue(model.containsKey("building"));
        assertEquals(buildingOne, model.get("building"));
        assertEquals("settings/building/edit", returnURL);
    }

    @Test
    public void testDelete_Successful() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = buildingSettingsController.delete(
                buildingOne.getBuildingKey(),
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/building/index",
                returnURL
        );

        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals(
                "The Building " + buildingOne.getName() + " was successfully deleted.",
                redirectAttributes.get("successMessage")
        );
    }

    @Test
    public void testDelete_Unsuccessful() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String returnURL = buildingSettingsController.delete(
                null,
                redirectAttributes
        );

        assertEquals(
                "redirect:/settings/building/index",
                returnURL
        );

        assertTrue(redirectAttributes.containsKey("dangerMessage"));
        assertEquals(
                "An error occurred when attempting to delete a Building.",
                redirectAttributes.get("dangerMessage")
        );
    }
}
