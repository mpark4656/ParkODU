package edu.odu.cs.gold.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class FloorTests {

    private static String FLOOR_KEY = "f123423482647264823124";
    private static String GARAGE_KEY= "g342432432432432324232";
    private static String FLOOR_NUMBER = "12";
    private static Integer AVAILABLE_SPACES = 100;
    private static Integer TOTAL_SPACES = 200;
    private static String FLOOR_DESCRIPTION = "Floor Description Here";
    private static Double FLOOR_CAPACITY = 50.00;
    private static Date UPDATED_DATE = new Date();
    private static Floor floor;

    @Before
    public void setup() {
        floor = new Floor();
    }

    @Test
    public void testFloor() {
        Floor floor = new Floor();
        assertNotNull(floor.getFloorKey());
        assertFalse(floor.getFloorKey().isEmpty());

        assertNull(floor.getGarageKey());
        assertNull(floor.getNumber());
        assertNull(floor.getTotalSpaces());
        assertNull(floor.getDescription());
        assertNull(floor.getAvailableSpaces());
        assertNull(floor.getCapacity());
        assertNull(floor.getLastUpdated());
    }

    @Test
    public void testFloorStringStringStringIntegerIntegerString() {
        Floor floor = new Floor(
            FLOOR_KEY,
            GARAGE_KEY,
            FLOOR_NUMBER,
            AVAILABLE_SPACES,
            TOTAL_SPACES,
            FLOOR_DESCRIPTION
        );

        assertEquals(FLOOR_KEY, floor.getFloorKey());
        assertEquals(GARAGE_KEY, floor.getGarageKey());
        assertEquals(FLOOR_NUMBER, floor.getNumber());
        assertEquals(TOTAL_SPACES,floor.getTotalSpaces());
        assertEquals(FLOOR_DESCRIPTION, floor.getDescription());
        assertEquals(AVAILABLE_SPACES, floor.getAvailableSpaces());
        assertEquals(FLOOR_CAPACITY,floor.getCapacity());
    }

    @Test
    public void testGetFloorKey() {
        floor.setFloorKey(FLOOR_KEY);
        assertEquals(FLOOR_KEY, floor.getFloorKey());
    }

    @Test
    public void testSetFloorKey() {
        floor.setFloorKey(FLOOR_KEY);
        assertEquals(FLOOR_KEY, floor.getFloorKey());
    }

    @Test
    public void testGetGarageKey() {
        floor.setGarageKey(GARAGE_KEY);
        assertEquals(GARAGE_KEY, floor.getGarageKey());
    }

    @Test
    public void testSetGarageKey() {
        floor.setGarageKey(GARAGE_KEY);
        assertEquals(GARAGE_KEY, floor.getGarageKey());
    }

    @Test
    public void testGetNumber() {
        floor.setNumber(FLOOR_NUMBER);
        assertEquals(FLOOR_NUMBER, floor.getNumber());
    }

    @Test
    public void testSetNumber() {
        floor.setNumber(FLOOR_NUMBER);
        assertEquals(FLOOR_NUMBER, floor.getNumber());
    }

    @Test
    public void testGetAvailableSpaces() {
        floor.setAvailableSpaces(AVAILABLE_SPACES);
        assertEquals(AVAILABLE_SPACES, floor.getAvailableSpaces());
    }

    @Test
    public void testSetAvailableSpaces() {
        floor.setAvailableSpaces(AVAILABLE_SPACES);
        assertEquals(AVAILABLE_SPACES, floor.getAvailableSpaces());
    }

    @Test
    public void testGetTotalSpaces() {
        floor.setTotalSpaces(TOTAL_SPACES);
        assertEquals(TOTAL_SPACES, floor.getTotalSpaces());
    }

    @Test
    public void testSetTotalSpaces() {
        floor.setTotalSpaces(TOTAL_SPACES);
        assertEquals(TOTAL_SPACES, floor.getTotalSpaces());
    }

    @Test
    public void testGetDescription() {
        floor.setDescription(FLOOR_DESCRIPTION);
        assertEquals(FLOOR_DESCRIPTION, floor.getDescription());
    }

    @Test
    public void testSetDescription() {
        floor.setDescription(FLOOR_DESCRIPTION);
        assertEquals(FLOOR_DESCRIPTION, floor.getDescription());
    }

    @Test
    public void testCalculateCapacity() {
        floor.setAvailableSpaces(AVAILABLE_SPACES);
        floor.setTotalSpaces(TOTAL_SPACES);
        floor.calculateCapacity();
        assertEquals(FLOOR_CAPACITY, floor.getCapacity());
    }

    @Test
    public void testGetCapacity() {
        floor.setAvailableSpaces(AVAILABLE_SPACES);
        floor.setTotalSpaces(TOTAL_SPACES);
        floor.calculateCapacity();
        assertEquals(FLOOR_CAPACITY, floor.getCapacity());
    }

    @Test
    public void testGetLastUpdated() {
        floor.setLastUpdated(UPDATED_DATE);
        assertEquals(UPDATED_DATE, floor.getLastUpdated());
    }

    @Test
    public void testSetLastUpdated() {
        floor.setLastUpdated(UPDATED_DATE);
        assertEquals(UPDATED_DATE, floor.getLastUpdated());
    }

    @Test
    public void testToString() {
        String string = "Floor{" +
                " floorKey='" + FLOOR_KEY + '\'' +
                ", garageKey='" + GARAGE_KEY + '\'' +
                ", number='" + FLOOR_NUMBER + '\'' +
                ", availableSpaces=" + AVAILABLE_SPACES +
                ", totalSpaces=" + TOTAL_SPACES +
                ", description='" + FLOOR_DESCRIPTION + '\'' +
                ", capacity=" + FLOOR_CAPACITY +
                ", lastUpdated=" + UPDATED_DATE +
                " }";

        floor.setFloorKey(FLOOR_KEY);
        floor.setGarageKey(GARAGE_KEY);
        floor.setNumber(FLOOR_NUMBER);
        floor.setAvailableSpaces(AVAILABLE_SPACES);
        floor.setTotalSpaces(TOTAL_SPACES);
        floor.setDescription(FLOOR_DESCRIPTION);
        floor.calculateCapacity();
        floor.setLastUpdated(UPDATED_DATE);

        assertEquals(string, floor.toString());
    }
}