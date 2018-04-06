package edu.odu.cs.gold.model;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

public class GarageTests {

    private static String GARAGE_KEY = "g121278414692432324324";
    private static String GARAGE_NAME = "GAREGE Name";
    private static String GARAGE_DESCRIPTION = "This is a garage description.";
    private static String GARAGE_HEIGHT_DESCRIPTION = "1 ft clearance LOL";
    private static String GARAGE_ADDRESS = "3455 This is a garage, Address, 23455";
    private static Integer GARAGE_AVAILABLE_SPACES = 100;
    private static Integer GARAGE_TOTAL_SPACES = 500;
    private static Double GARAGE_CAPACITY = 80.0;
    private static Double GARAGE_LATITUDE = 36.0003;
    private static Double GARAGE_LONGITUDE = 36.2222;
    private static Date GARAGE_LAST_UPDATED = new Date();

    private static Garage garage;

    @Before
    public void setUp() throws Exception {
        garage = new Garage();
    }

    @Test
    public void testGarage() {
        Garage garage = new Garage();
        assertNotNull(garage.getGarageKey());
        assertFalse(garage.getGarageKey().isEmpty());

        assertNull(garage.getName());
        assertNull(garage.getAddress());
        assertNull(garage.getAvailableSpaces());
        assertNull(garage.getDescription());
        assertNull(garage.getHeightDescription());
        assertNull(garage.getLatitude());
        assertNull(garage.getLongitude());
        assertNull(garage.getCapacity());
        assertNull(garage.getTotalSpaces());
        assertNull(garage.getLastUpdated());
    }

    @Test
    public void testGetGarageKey() {
        garage.setGarageKey(GARAGE_KEY);
        assertEquals(GARAGE_KEY, garage.getGarageKey());
    }

    @Test
    public void testSetGarageKey() {
        garage.setGarageKey(GARAGE_KEY);
        assertEquals(GARAGE_KEY, garage.getGarageKey());
    }

    @Test
    public void testGetName() {
        garage.setName(GARAGE_NAME);
        assertEquals(GARAGE_NAME, garage.getName());
    }

    @Test
    public void testSetName() {
        garage.setName(GARAGE_NAME);
        assertEquals(GARAGE_NAME, garage.getName());
    }

    @Test
    public void testGetDescription() {
        garage.setDescription(GARAGE_DESCRIPTION);
        assertEquals(GARAGE_DESCRIPTION, garage.getDescription());
    }

    @Test
    public void testSetDescription() {
        garage.setDescription(GARAGE_DESCRIPTION);
        assertEquals(GARAGE_DESCRIPTION, garage.getDescription());
    }

    @Test
    public void testGetHeightDescription() {
        garage.setHeightDescription(GARAGE_HEIGHT_DESCRIPTION);
        assertEquals(GARAGE_HEIGHT_DESCRIPTION, garage.getHeightDescription());
    }

    @Test
    public void testSetHeightDescription() {
        garage.setHeightDescription(GARAGE_HEIGHT_DESCRIPTION);
        assertEquals(GARAGE_HEIGHT_DESCRIPTION, garage.getHeightDescription());
    }

    @Test
    public void testGetAddress() {
        garage.setAddress(GARAGE_ADDRESS);
        assertEquals(GARAGE_ADDRESS, garage.getAddress());
    }

    @Test
    public void testSetAddress() {
        garage.setAddress(GARAGE_ADDRESS);
        assertEquals(GARAGE_ADDRESS, garage.getAddress());
    }

    @Test
    public void getAvailableSpaces() {
        garage.setAvailableSpaces(GARAGE_AVAILABLE_SPACES);
        assertEquals(GARAGE_AVAILABLE_SPACES, garage.getAvailableSpaces());
    }

    @Test
    public void setAvailableSpaces() {
        garage.setAvailableSpaces(GARAGE_AVAILABLE_SPACES);
        assertEquals(GARAGE_AVAILABLE_SPACES, garage.getAvailableSpaces());
    }

    @Test
    public void getTotalSpaces() {
        garage.setTotalSpaces(GARAGE_TOTAL_SPACES);
        assertEquals(GARAGE_TOTAL_SPACES, garage.getTotalSpaces());
    }

    @Test
    public void setTotalSpaces() {
        garage.setTotalSpaces(GARAGE_TOTAL_SPACES);
        assertEquals(GARAGE_TOTAL_SPACES, garage.getTotalSpaces());
    }

    @Test
    public void getCapacity() {
        garage.setCapacity(GARAGE_CAPACITY);
        assertEquals(GARAGE_CAPACITY, garage.getCapacity());
    }

    @Test
    public void setCapacity() {
        garage.setCapacity(GARAGE_CAPACITY);
        assertEquals(GARAGE_CAPACITY, garage.getCapacity());
    }

    @Test
    public void calculateCapacity() {
        garage.setAvailableSpaces(GARAGE_AVAILABLE_SPACES);
        garage.setTotalSpaces(GARAGE_TOTAL_SPACES);
        garage.calculateCapacity();
        assertEquals(GARAGE_CAPACITY, garage.getCapacity());
    }

    @Test
    public void getLatitude() {
        garage.setLatitude(GARAGE_LATITUDE);
        assertEquals(GARAGE_LATITUDE, garage.getLatitude());
    }

    @Test
    public void setLatitude() {
        garage.setLatitude(GARAGE_LATITUDE);
        assertEquals(GARAGE_LATITUDE, garage.getLatitude());
    }

    @Test
    public void getLongitude() {
        garage.setLongitude(GARAGE_LONGITUDE);
        assertEquals(GARAGE_LONGITUDE, garage.getLongitude());
    }

    @Test
    public void setLongitude() {
        garage.setLongitude(GARAGE_LONGITUDE);
        assertEquals(GARAGE_LONGITUDE, garage.getLongitude());
    }

    @Test
    public void getLastUpdated() {
        garage.setLastUpdated(GARAGE_LAST_UPDATED);
        assertEquals(GARAGE_LAST_UPDATED, garage.getLastUpdated());
    }

    @Test
    public void setLastUpdated() {
        garage.setLastUpdated(GARAGE_LAST_UPDATED);
        assertEquals(GARAGE_LAST_UPDATED, garage.getLastUpdated());
    }

    @Test
    public void getLocation() {
        garage.setLongitude(GARAGE_LONGITUDE);
        garage.setLatitude(GARAGE_LATITUDE);

        Location location = new Location(GARAGE_LATITUDE, GARAGE_LONGITUDE);
        Location garageLocation = garage.getLocation();

        assertEquals(location.getLongitude(), garageLocation.getLongitude());
        assertEquals(location.getLatitude(), garageLocation.getLatitude());
    }

    @Test
    public void testToString() {
        String string = "Garage{" +
                "garageKey='" + GARAGE_KEY + '\'' +
                ", name='" + GARAGE_NAME + '\'' +
                ", description='" + GARAGE_DESCRIPTION + '\'' +
                ", heightDescription='" + GARAGE_HEIGHT_DESCRIPTION + '\'' +
                ", address='" + GARAGE_ADDRESS + '\'' +
                ", availableSpaces=" + GARAGE_AVAILABLE_SPACES +
                ", totalSpaces=" + GARAGE_TOTAL_SPACES +
                ", capacity=" + GARAGE_CAPACITY +
                ", latitude=" + GARAGE_LATITUDE +
                ", longitude=" + GARAGE_LONGITUDE +
                ", lastUpdated=" + GARAGE_LAST_UPDATED +
                '}';

        garage.setGarageKey(GARAGE_KEY);
        garage.setName(GARAGE_NAME);
        garage.setDescription(GARAGE_DESCRIPTION);
        garage.setHeightDescription(GARAGE_HEIGHT_DESCRIPTION);
        garage.setAddress(GARAGE_ADDRESS);
        garage.setAvailableSpaces(GARAGE_AVAILABLE_SPACES);
        garage.setTotalSpaces(GARAGE_TOTAL_SPACES);
        garage.setCapacity(GARAGE_CAPACITY);
        garage.setLatitude(GARAGE_LATITUDE);
        garage.setLongitude(GARAGE_LONGITUDE);
        garage.setLastUpdated(GARAGE_LAST_UPDATED);

        assertEquals(string, garage.toString());
    }
}