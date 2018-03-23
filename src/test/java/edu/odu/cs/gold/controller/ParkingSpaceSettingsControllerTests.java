package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ParkingSpaceSettingsControllerTests {

    private static String GARAGE_ONE_KEY = "00000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "00000000000000000000000000000002";

    private static String FLOOR_ONE_KEY = "00000000000000000000000000000003";
    private static String FLOOR_TWO_KEY = "00000000000000000000000000000004";

    private static String PARKING_SPACE_ONE_KEY = "00000000000000000000000000000005";
    private static String PARKING_SPACE_TWO_KEY = "00000000000000000000000000000006";
    private static String PARKING_SPACE_THREE_KEY = "00000000000000000000000000000007";
    private static String PARKING_SPACE_FOUR_KEY = "00000000000000000000000000000008";

    private static String PERMIT_KEY_ONE = "00000000000000000000000000000009";
    private static String PERMIT_KEY_TWO = "00000000000000000000000000000010";
    private static String PERMIT_KEY_THREE = "00000000000000000000000000000011";
    private static String PERMIT_KEY_FOUR = "00000000000000000000000000000012";

    private static String SPACE_KEY_ONE = "00000000000000000000000000000013";
    private static String SPACE_KEY_TWO = "00000000000000000000000000000014";
    private static String SPACE_KEY_THREE = "00000000000000000000000000000015";
    private static String SPACE_KEY_FOUR = "00000000000000000000000000000016";


    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";
    private static String FLOOR_ONE_NUMBER = "1";
    private static String FLOOR_TWO_NUMBER = "2";
    private static String PERMIT_TYPE_ONE_NAME = "PermitTypeOne";
    private static String PERMIT_TYPE_TWO_NAME = "PermitTypeTwo";
    private static String PERMIT_TYPE_THREE_NAME = "PermitTypeThree";
    private static String PERMIT_TYPE_FOUR_NAME = "PermitTypeFour";
    private static String SPACE_TYPE_ONE_NAME = "SpaceTypeOne";
    private static String SPACE_TYPE_TWO_NAME = "SpaceTypeTwo";
    private static String SPACE_TYPE_THREE_NAME = "SpaceTypeThree";
    private static String SPACE_TYPE_FOUR_NAME = "SpaceTypeFour";

    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "1";
    private static String PARKING_SPACE_THREE_FLOOR = "1";
    private static String PARKING_SPACE_FOUR_FLOOR = "1";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 2;
    private static Integer PARKING_SPACE_THREE_NUMBER = 3;
    private static Integer PARKING_SPACE_FOUR_NUMBER = 4;

    private ParkingSpaceSettingsController parkingSpaceSettingsController;
    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private GarageService garageService;

    private Garage garageOne;
    private Garage garageTwo;
    private Floor floorOne;
    private Floor floorTwo;
    private ParkingSpace  parkingSpaceOne;
    private ParkingSpace  parkingSpaceTwo;
    private ParkingSpace  parkingSpaceThree;
    private ParkingSpace  parkingSpaceFour;
    private PermitType permitTypeOne;
    private PermitType permitTypeTwo;
    private PermitType permitTypeThree;
    private PermitType permitTypeFour;
    private SpaceType spaceTypeOne;
    private SpaceType spaceTypeTwo;
    private SpaceType spaceTypeThree;
    private SpaceType spaceTypeFour;

    @Before
    public void Setup() {
        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        garageTwo = new Garage();
        garageTwo.setGarageKey(GARAGE_TWO_KEY);
        garageTwo.setName(GARAGE_TWO_NAME);

        floorOne = new Floor();
        floorOne.setFloorKey(FLOOR_ONE_KEY);
        floorOne.setGarageKey(GARAGE_ONE_KEY);
        floorOne.setNumber(FLOOR_ONE_NUMBER);

        floorTwo = new Floor();
        floorTwo.setFloorKey(FLOOR_TWO_KEY);
        floorTwo.setGarageKey(GARAGE_TWO_KEY);
        floorTwo.setNumber(FLOOR_TWO_NUMBER);

        parkingSpaceOne = new ParkingSpace();
        parkingSpaceOne.setParkingSpaceKey(PARKING_SPACE_ONE_KEY);
        parkingSpaceOne.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceOne.setFloor(PARKING_SPACE_ONE_FLOOR);
        parkingSpaceOne.setNumber(PARKING_SPACE_ONE_NUMBER);
        parkingSpaceOne.setPermitTypeKey(PERMIT_KEY_ONE);
        parkingSpaceOne.setSpaceTypeKey(SPACE_KEY_ONE);

        parkingSpaceTwo = new ParkingSpace();
        parkingSpaceTwo.setParkingSpaceKey(PARKING_SPACE_TWO_KEY);
        parkingSpaceTwo.setGarageKey(GARAGE_ONE_KEY);
        parkingSpaceTwo.setFloor(PARKING_SPACE_TWO_FLOOR);
        parkingSpaceTwo.setNumber(PARKING_SPACE_TWO_NUMBER);
        parkingSpaceTwo.setPermitTypeKey(PERMIT_KEY_TWO);
        parkingSpaceTwo.setSpaceTypeKey(SPACE_KEY_TWO);

        parkingSpaceThree = new ParkingSpace();
        parkingSpaceThree.setParkingSpaceKey(PARKING_SPACE_THREE_KEY);
        parkingSpaceThree.setGarageKey(GARAGE_TWO_KEY);
        parkingSpaceThree.setFloor(PARKING_SPACE_THREE_FLOOR);
        parkingSpaceThree.setNumber(PARKING_SPACE_THREE_NUMBER);
        parkingSpaceThree.setPermitTypeKey(PERMIT_KEY_THREE);
        parkingSpaceThree.setSpaceTypeKey(SPACE_KEY_THREE);

        parkingSpaceFour = new ParkingSpace();
        parkingSpaceFour.setParkingSpaceKey(PARKING_SPACE_FOUR_KEY);
        parkingSpaceFour.setGarageKey(GARAGE_TWO_KEY);
        parkingSpaceFour.setFloor(PARKING_SPACE_FOUR_FLOOR);
        parkingSpaceFour.setNumber(PARKING_SPACE_FOUR_NUMBER);
        parkingSpaceFour.setPermitTypeKey(PERMIT_KEY_FOUR);
        parkingSpaceFour.setSpaceTypeKey(SPACE_KEY_FOUR);

        permitTypeOne = new PermitType();
        permitTypeOne.setName(PERMIT_TYPE_ONE_NAME);
        permitTypeOne.setPermitTypeKey(PERMIT_KEY_ONE);

        permitTypeTwo = new PermitType();
        permitTypeTwo.setName(PERMIT_TYPE_TWO_NAME);
        permitTypeTwo.setPermitTypeKey(PERMIT_KEY_TWO);

        permitTypeThree = new PermitType();
        permitTypeThree.setName(PERMIT_TYPE_THREE_NAME);
        permitTypeThree.setPermitTypeKey(PERMIT_KEY_THREE);

        permitTypeFour = new PermitType();
        permitTypeFour.setName(PERMIT_TYPE_FOUR_NAME);
        permitTypeFour.setPermitTypeKey(PERMIT_KEY_FOUR);

        spaceTypeOne = new SpaceType();
        spaceTypeOne.setName(SPACE_TYPE_ONE_NAME);
        spaceTypeOne.setSpaceTypeKey(SPACE_KEY_ONE);

        spaceTypeTwo = new SpaceType();
        spaceTypeTwo.setName(SPACE_TYPE_TWO_NAME);
        spaceTypeTwo.setSpaceTypeKey(SPACE_KEY_TWO);

        spaceTypeThree = new SpaceType();
        spaceTypeThree.setName(SPACE_TYPE_THREE_NAME);
        spaceTypeThree.setSpaceTypeKey(SPACE_KEY_THREE);

        spaceTypeFour = new SpaceType();
        spaceTypeFour.setName(SPACE_TYPE_FOUR_NAME);
        spaceTypeFour.setSpaceTypeKey(SPACE_KEY_FOUR);

        Collection<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        Collection<Floor> floors = new ArrayList<>();
        floors.add(floorOne);
        floors.add(floorTwo);

        List<ParkingSpace> parkingSpaces = new ArrayList<> ();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);
        parkingSpaces.add(parkingSpaceThree);
        parkingSpaces.add(parkingSpaceFour);

        Collection<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);
        permitTypes.add(permitTypeThree);
        permitTypes.add(permitTypeFour);

        Collection<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);
        spaceTypes.add(spaceTypeThree);
        spaceTypes.add(spaceTypeFour);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findByKey(GARAGE_TWO_KEY)).thenReturn(garageTwo);
        when(garageRepository.findAll()).thenReturn(garages);
        doNothing().when(garageRepository).save(any(Garage.class));
        doNothing().when(garageRepository).delete(anyString());

        floorRepository = mock(FloorRepository.class);
        when(floorRepository.findByKey(FLOOR_ONE_KEY)).thenReturn(floorOne);
        when(floorRepository.findByKey(FLOOR_TWO_KEY)).thenReturn(floorTwo);
        when(floorRepository.findAll()).thenReturn(floors);
        doNothing().when(floorRepository).save(any(Floor.class));
        doNothing().when(floorRepository).delete(anyString());

        parkingSpaceRepository = mock(ParkingSpaceRepository.class);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_ONE_KEY)).thenReturn(parkingSpaceOne);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_TWO_KEY)).thenReturn(parkingSpaceTwo);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_THREE_KEY)).thenReturn(parkingSpaceThree);
        when(parkingSpaceRepository.findByKey(PARKING_SPACE_FOUR_KEY)).thenReturn(parkingSpaceFour);
        when(parkingSpaceRepository.findAll()).thenReturn(parkingSpaces);
        doNothing().when(parkingSpaceRepository).save(any(ParkingSpace.class));
        doNothing().when(parkingSpaceRepository).delete(anyString());

        permitTypeRepository = mock(PermitTypeRepository.class);
        when(permitTypeRepository.findByKey(PERMIT_KEY_ONE)).thenReturn(permitTypeOne);
        when(permitTypeRepository.findByKey(PERMIT_KEY_TWO)).thenReturn(permitTypeTwo);
        when(permitTypeRepository.findByKey(PERMIT_KEY_THREE)).thenReturn(permitTypeThree);
        when(permitTypeRepository.findByKey(PERMIT_KEY_FOUR)).thenReturn(permitTypeFour);
        when(permitTypeRepository.findAll()).thenReturn(permitTypes);
        doNothing().when(permitTypeRepository).save(any(PermitType.class));
        doNothing().when(permitTypeRepository).delete(anyString());

        spaceTypeRepository = mock(SpaceTypeRepository.class);
        when(spaceTypeRepository.findByKey(SPACE_KEY_ONE)).thenReturn(spaceTypeOne);
        when(spaceTypeRepository.findByKey(SPACE_KEY_TWO)).thenReturn(spaceTypeTwo);
        when(spaceTypeRepository.findByKey(SPACE_KEY_THREE)).thenReturn(spaceTypeThree);
        when(spaceTypeRepository.findByKey(SPACE_KEY_FOUR)).thenReturn(spaceTypeFour);
        when(spaceTypeRepository.findAll()).thenReturn(spaceTypes);
        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(parkingSpaces);
        doNothing().when(spaceTypeRepository).save(any(SpaceType.class));
        doNothing().when(spaceTypeRepository).delete(anyString());

        garageService = new GarageService(garageRepository, floorRepository, parkingSpaceRepository);

        garageService = mock(GarageService.class);
        doNothing().when(garageService).refresh(GARAGE_ONE_KEY);
        doNothing().when(garageService).refresh(GARAGE_TWO_KEY);
        doNothing().when(garageService).save(any(Garage.class));

        parkingSpaceSettingsController = new ParkingSpaceSettingsController(
                garageRepository,
                floorRepository,
                parkingSpaceRepository,
                permitTypeRepository,
                spaceTypeRepository,
                garageService
        );
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = parkingSpaceSettingsController.index(null, null, null, null, model);

        assertEquals("settings/parking_space/index", returnURL);
        Collection<Garage> garages = (Collection)model.get("garages");
        assertTrue(garages.size() == 2);

        Collection<Floor> floors = (Collection)model.get("floors");
        assertTrue(floors.size() == 2);
    }

    @Test
    public void testFloor() {
        List<ParkingSpace> floorOneParkingSpaces = new ArrayList<>();
        floorOneParkingSpaces.add(parkingSpaceOne);
        floorOneParkingSpaces.add(parkingSpaceTwo);
        when(parkingSpaceRepository.findByPredicate(any(Predicate.class))).thenReturn(floorOneParkingSpaces);

        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = parkingSpaceSettingsController.floor(null,null,null,null, FLOOR_ONE_KEY, model);

        assertEquals("settings/parking_space/floor", returnURL);
        assertTrue(model.get("floor").equals(floorOne));
        assertTrue(model.get("garage").equals(garageOne));

        Collection<ParkingSpace> parkingSpaces = (Collection)model.get("parkingSpaces");
        assertTrue(parkingSpaces.size() == 2);
        Collection<PermitType> permitTypes = (Collection)model.get("permitTypes");
        assertTrue(permitTypes.size() == 4);
        Collection<SpaceType> spaceTypes = (Collection)model.get("spaceTypes");
        assertTrue(spaceTypes.size() == 4);
    }
    @Test
    public void testCreate_get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = parkingSpaceSettingsController.create(null,null,null,
                                                                null, GARAGE_ONE_KEY, FLOOR_ONE_KEY, model);
        assertEquals("settings/parking_space/create", returnURL);
        assertTrue(model.get("floor").equals(floorOne));
        assertTrue(model.containsKey("floor"));
        assertTrue(model.containsKey("parkingSpace"));
        assertTrue(model.containsKey("permitTypes"));
        assertTrue(model.containsKey("spaceTypes"));

        Collection<ParkingSpace> permitTypes = (Collection)model.get("permitTypes");
        assertTrue(permitTypes.size() == 4);

        Collection<ParkingSpace> spaceTypes = (Collection)model.get("spaceTypes");
        assertTrue(permitTypes.size() == 4);
    }

    @Test
    public void testCreate_post_success() {
        ParkingSpace newParkingSpace = new ParkingSpace();
        newParkingSpace.setParkingSpaceKey("00000000000000000000000000000020");
        newParkingSpace.setNumber(5);
        newParkingSpace.setFloor("1");
        newParkingSpace.setGarageKey("00000000000000000000000000000001");
        newParkingSpace.setPermitTypeKey("00000000000000000000000000000009");
        newParkingSpace.setSpaceTypeKey("00000000000000000000000000000013");

        String floorKey = "00000000000000000000000000000003";
        when(parkingSpaceRepository.findByKey("00000000000000000000000000000020")).thenReturn(null);

        List<Floor> matchingFloor = new ArrayList<>();
        matchingFloor.add(floorOne);
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(matchingFloor);


        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = parkingSpaceSettingsController.create(newParkingSpace, redirectAttributes);

        assertEquals("redirect:/settings/parking_space/floor/" + FLOOR_ONE_KEY, returnURL);
        assertTrue(redirectAttributes.containsKey("successMessage"));
    }

    @Test
    public void testCreate_post_duplicate() {
        ParkingSpace newParkingSpace = new ParkingSpace();
        newParkingSpace.setParkingSpaceKey("0000000000000000000000000000005");
        newParkingSpace.setNumber(1);
        newParkingSpace.setFloor("1");
        newParkingSpace.setGarageKey("00000000000000000000000000000001");

        String floorKey = "00000000000000000000000000000003";

        List<Floor> matchingFloor = new ArrayList<>();
        matchingFloor.add(floorOne);
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(matchingFloor);

        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        String returnURL = parkingSpaceSettingsController.create(newParkingSpace, redirectAttributes);

        assertEquals("redirect:/settings/parking_space/create/" + GARAGE_ONE_KEY + "/" + floorKey, returnURL);
        assertTrue(redirectAttributes.containsKey("dangerMessage"));
    }


    @Test
    public void testSetSpaceNumber_success() {
        String returnString = parkingSpaceSettingsController.setSpaceNumber(PARKING_SPACE_ONE_KEY,5);

        assertEquals("The space number was set to 5", returnString);
    }

    @Test
    public void testSetSpaceNumber_duplicate() {
        String returnString = parkingSpaceSettingsController.setSpaceNumber(PARKING_SPACE_ONE_KEY,2);

        assertEquals("The number, 2, already exists.", returnString);
    }

    @Test
    public void testSetSpaceType() {
        String returnString = parkingSpaceSettingsController.setSpaceType(PARKING_SPACE_ONE_KEY, SPACE_KEY_TWO);

        assertEquals("The space type of the space number " + PARKING_SPACE_ONE_NUMBER + " was set to " + SPACE_TYPE_TWO_NAME,
                returnString);
    }

    @Test
    public void testSetPermitType() {
        String returnString = parkingSpaceSettingsController.setPermitType(PARKING_SPACE_ONE_KEY, PERMIT_KEY_TWO);

        assertEquals("The permit type of the space number " + PARKING_SPACE_ONE_NUMBER + " was set to " + PERMIT_TYPE_TWO_NAME,
                returnString);
    }

    @Test
    public void testSetAvailability_available() {
        String returnString = parkingSpaceSettingsController.setAvailability(PARKING_SPACE_ONE_KEY, true);

        assertEquals("The space number " + PARKING_SPACE_ONE_NUMBER + " was set to available",
                returnString);
    }

    @Test
    public void testSetAvailability_unavailable() {
        String returnString = parkingSpaceSettingsController.setAvailability(PARKING_SPACE_ONE_KEY, false);

        assertEquals("The space number " + PARKING_SPACE_ONE_NUMBER + " was set to unavailable",
                returnString);
    }

    @Test
    public void testDelete_success(){
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        List<Floor> sameFloor = new ArrayList<>();
        sameFloor.add(floorOne);
        when(floorRepository.findByPredicate(any(Predicate.class))).thenReturn(sameFloor);

        String returnURL = parkingSpaceSettingsController.delete(PARKING_SPACE_ONE_KEY, redirectAttributes);

        assertTrue(redirectAttributes.containsKey("successMessage"));
        assertEquals("redirect:/settings/parking_space/floor/" + FLOOR_ONE_KEY, returnURL);
    }
}
