package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.repository.BuildingRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    }
}
