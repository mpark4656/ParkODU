package edu.odu.cs.gold.restcontroller;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GarageRestControllerTests {

    private static final String GARAGE_ONE_KEY = "a0000000000000000000000000000001";

    private Garage garageOne;

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    private GarageRestController garageRestController;

    @Before
    public void setup() {
        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);

        floorRepository = mock(FloorRepository.class);

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);

        garageRestController = new GarageRestController(garageRepository,
                floorRepository,
                parkingSpaceRepository);
    }

    @Test
    public void testGet() {
        Garage garage = garageRestController.get(GARAGE_ONE_KEY);
        assertEquals(garageOne, garage);
    }
}
