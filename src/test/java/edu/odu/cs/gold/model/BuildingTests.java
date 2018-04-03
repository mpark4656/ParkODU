package edu.odu.cs.gold.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTests {

    private static String BUILDING_KEY = "a00000000000000002";
    private static String BUILDING_NAME = "BuildingName";
    private static String BUILDING_DESCRIPTION = "Building Description";
    private static Double BUILDING_LATTITUDE = 36.0000001;
    private static Double BUILDING_LONGTITUDE = 36.0000123;
    private static String BUILDING_ADDRESS = "12400 SOME ADDRESS, VA, 12345";
    private static Location BUILDING_LOCATION;
    private static Building building;

    @Before
    public void setUp() throws Exception {
        building = new Building();
        BUILDING_LOCATION = new Location(BUILDING_LATTITUDE, BUILDING_LONGTITUDE);
    }

    @Test
    public void testBuilding() {
        Building building = new Building();
        assertNotNull(building.getBuildingKey());
        assertFalse(building.getBuildingKey().isEmpty());

        assertNull(building.getName());
        assertNull(building.getDescription());
        assertNull(building.getLatitude());
        assertNull(building.getLongitude());
        assertNull(building.getAddress());
    }

    @Test
    public void testBuildingStringDoubleDouble(){
        Building building = new Building(
                BUILDING_NAME,
                BUILDING_LATTITUDE,
                BUILDING_LONGTITUDE
        );

        assertNotNull(building.getBuildingKey());
        assertFalse(building.getBuildingKey().isEmpty());
        assertEquals(BUILDING_NAME, building.getName());
        assertNull(building.getDescription());
        assertEquals(BUILDING_LATTITUDE, building.getLatitude());
        assertEquals(BUILDING_LONGTITUDE, building.getLongitude());
        assertNull(building.getAddress());
    }

    @Test
    public void testGetBuildingKey() {
        building.setBuildingKey(BUILDING_KEY);
        assertEquals(BUILDING_KEY, building.getBuildingKey());
    }

    @Test
    public void testSetBuildingKey() {
        building.setBuildingKey(BUILDING_KEY);
        assertEquals(BUILDING_KEY, building.getBuildingKey());
    }

    @Test
    public void testGetName() {
        building.setName(BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testSetName() {
        building.setName(BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testGetDescription() {
        building.setDescription(BUILDING_DESCRIPTION);
        assertEquals(BUILDING_DESCRIPTION, building.getDescription());
    }

    @Test
    public void testSetDescription() {
        building.setDescription(BUILDING_DESCRIPTION);
        assertEquals(BUILDING_DESCRIPTION, building.getDescription());
    }

    @Test
    public void testGetLatitude() {
        building.setLatitude(BUILDING_LATTITUDE);
        assertEquals(BUILDING_LATTITUDE, building.getLatitude());
    }

    @Test
    public void testSetLatitude() {
        building.setLatitude(BUILDING_LATTITUDE);
        assertEquals(BUILDING_LATTITUDE, building.getLatitude());
    }

    @Test
    public void testGetLongitude() {
        building.setLongitude(BUILDING_LONGTITUDE);
        assertEquals(BUILDING_LONGTITUDE, building.getLongitude());
    }

    @Test
    public void testSetLongitude() {
        building.setLongitude(BUILDING_LONGTITUDE);
        assertEquals(BUILDING_LONGTITUDE, building.getLongitude());
    }

    @Test
    public void testGetAddress() {
        building.setAddress(BUILDING_ADDRESS);
        assertEquals(BUILDING_ADDRESS, building.getAddress());
    }

    @Test
    public void testSetAddress() {
        building.setAddress(BUILDING_ADDRESS);
        assertEquals(BUILDING_ADDRESS, building.getAddress());
    }

    @Test
    public void testGetLocation() {
        building.setLatitude(BUILDING_LATTITUDE);
        building.setLongitude(BUILDING_LONGTITUDE);

        Location location = new Location(BUILDING_LATTITUDE, BUILDING_LONGTITUDE);
        Location buildingLocation = building.getLocation();

        assertEquals(location.getLatitude(), buildingLocation.getLatitude());
        assertEquals(location.getLongitude(), buildingLocation.getLongitude());
    }

    @Test
    public void testToString() {
        String string = "Building{" +
                " buildingKey='" + BUILDING_KEY + '\'' +
                ", name='" + BUILDING_NAME + '\'' +
                ", description='" + BUILDING_DESCRIPTION + '\'' +
                ", latitude=" + BUILDING_LATTITUDE +
                ", longitude=" + BUILDING_LONGTITUDE +
                ", address='" + BUILDING_ADDRESS + '\'' +
                " }";

        building.setBuildingKey(BUILDING_KEY);
        building.setName(BUILDING_NAME);
        building.setDescription(BUILDING_DESCRIPTION);
        building.setLatitude(BUILDING_LATTITUDE);
        building.setLongitude(BUILDING_LONGTITUDE);
        building.setAddress(BUILDING_ADDRESS);

        assertEquals(string, building.toString());
    }
}