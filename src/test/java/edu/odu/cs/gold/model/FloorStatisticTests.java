package edu.odu.cs.gold.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class FloorStatisticTests {

    private static String FLOOR_STATISTIC_KEY = "s208568123207430712730127";
    private static String FLOOR_KEY = "f120893742089142102892";
    private static Double CAPACITY= 35.6;
    private static Date TIMESTAMP = new Date();
    private static FloorStatistic floorStatistic;

    @Before
    public void setUp() throws Exception {
        floorStatistic = new FloorStatistic();
    }

    @Test
    public void testFloorStatistic() {
        FloorStatistic floorStatistic = new FloorStatistic();
        assertNotNull(floorStatistic.getFloorStatisticKey());
        assertFalse(floorStatistic.getFloorStatisticKey().isEmpty());
        assertNull(floorStatistic.getFloorKey());
        assertNull(floorStatistic.getCapacity());
        assertNull(floorStatistic.getTimestamp());
    }

    @Test
    public void testGetFloorStatisticKey() {
        floorStatistic.setFloorStatisticKey(FLOOR_STATISTIC_KEY);
        assertEquals(FLOOR_STATISTIC_KEY, floorStatistic.getFloorStatisticKey());
    }

    @Test
    public void testSetFloorStatisticKey() {
        floorStatistic.setFloorStatisticKey(FLOOR_STATISTIC_KEY);
        assertEquals(FLOOR_STATISTIC_KEY, floorStatistic.getFloorStatisticKey());
    }

    @Test
    public void testGetFloorKey() {
        floorStatistic.setFloorKey(FLOOR_KEY);
        assertEquals(FLOOR_KEY, floorStatistic.getFloorKey());
    }

    @Test
    public void testSetFloorKey() {
        floorStatistic.setFloorKey(FLOOR_KEY);
        assertEquals(FLOOR_KEY, floorStatistic.getFloorKey());
    }

    @Test
    public void testGetCapacity() {
        floorStatistic.setCapacity(CAPACITY);
        assertEquals(CAPACITY, floorStatistic.getCapacity());
    }

    @Test
    public void testSetCapacity() {
        floorStatistic.setCapacity(CAPACITY);
        assertEquals(CAPACITY, floorStatistic.getCapacity());
    }

    @Test
    public void testGetTimestamp() {
        floorStatistic.setTimestamp(TIMESTAMP);
        assertEquals(TIMESTAMP, floorStatistic.getTimestamp());
    }

    @Test
    public void testSetTimestamp() {
        floorStatistic.setTimestamp(TIMESTAMP);
        assertEquals(TIMESTAMP, floorStatistic.getTimestamp());
    }

    @Test
    public void testToString() {
        String string = "FloorStatistic{" +
                " floorStatisticKey='" + FLOOR_STATISTIC_KEY + '\'' +
                ", floorKey='" + FLOOR_KEY + '\'' +
                ", capacity=" + CAPACITY +
                ", timestamp=" + TIMESTAMP +
                " }";

        floorStatistic.setFloorStatisticKey(FLOOR_STATISTIC_KEY);
        floorStatistic.setFloorKey(FLOOR_KEY);
        floorStatistic.setCapacity(CAPACITY);
        floorStatistic.setTimestamp(TIMESTAMP);

        assertEquals(string, floorStatistic.toString());
    }
}