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
    }

    @Test
    public void testSetFloorKey() {
    }

    @Test
    public void testGetGarageKey() {
    }

    @Test
    public void testSetGarageKey() {
    }

    @Test
    public void testGetNumber() {
    }

    @Test
    public void testSetNumber() {
    }

    @Test
    public void testGetAvailableSpaces() {
    }

    @Test
    public void testSetAvailableSpaces() {
    }

    @Test
    public void testGetTotalSpaces() {
    }

    @Test
    public void testSetTotalSpaces() {
    }

    @Test
    public void testGetDescription() {
    }

    @Test
    public void testSetDescription() {
    }

    @Test
    public void testCalculateCapacity() {
    }

    @Test
    public void testGetCapacity() {
    }

    @Test
    public void testGetLastUpdated() {
    }

    @Test
    public void testSetLastUpdated() {
    }

    @Test
    public void testToString() {
    }
}