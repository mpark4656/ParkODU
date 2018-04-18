package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GoogleMapService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalyticsControllerTests {

    private static String GARAGE_ONE_KEY = "00000000000000000000000000000001";
    private static String GARAGE_TWO_KEY = "00000000000000000000000000000002";

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

    private String BUILDING_ONE_KEY = "a000000000000000000000001";
    private String BUILDING_TWO_KEY = "a000000000000000000000002";

    private String BUILDING_ONE_NAME = "BUILDINGONE";
    private String BUILDING_TWO_NAME = "BUILDINGTWO";

    private static String GARAGE_ONE_NAME = "Garage1";
    private static String GARAGE_TWO_NAME = "Garage2";

    private static String PERMIT_TYPE_ONE_NAME = "Permit1";
    private static String PERMIT_TYPE_TWO_NAME = "Permit2";
    private static String PERMIT_TYPE_THREE_NAME = "Permit3";
    private static String PERMIT_TYPE_FOUR_NAME = "Permit4";
    private static String SPACE_TYPE_ONE_NAME = "Space1";
    private static String SPACE_TYPE_TWO_NAME = "Space2";
    private static String SPACE_TYPE_THREE_NAME = "Space3";
    private static String SPACE_TYPE_FOUR_NAME = "Space4";

    private static String PARKING_SPACE_ONE_FLOOR = "1";
    private static String PARKING_SPACE_TWO_FLOOR = "1";
    private static String PARKING_SPACE_THREE_FLOOR = "1";
    private static String PARKING_SPACE_FOUR_FLOOR = "1";

    private static Integer PARKING_SPACE_ONE_NUMBER = 1;
    private static Integer PARKING_SPACE_TWO_NUMBER = 2;
    private static Integer PARKING_SPACE_THREE_NUMBER = 3;
    private static Integer PARKING_SPACE_FOUR_NUMBER = 4;

    private Garage garageOne;
    private Garage garageTwo;
    private ParkingSpace parkingSpaceOne;
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
    private Building buildingOne;
    private Building buildingTwo;

    private GarageRepository garageRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private BuildingRepository buildingRepository;
    private UserRepository userRepository;

    private GoogleMapService googleMapService;
    private TravelDistanceDurationRepository travelDistanceDurationRepository;
    private AnalyticsController analyticsController;

    @Before
    public void setup() {
        garageOne = new Garage();
        garageOne.setGarageKey(GARAGE_ONE_KEY);
        garageOne.setName(GARAGE_ONE_NAME);

        garageTwo = new Garage();
        garageTwo.setGarageKey(GARAGE_TWO_KEY);
        garageTwo.setName(GARAGE_TWO_NAME);

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

        buildingOne = new Building();
        buildingOne.setBuildingKey(BUILDING_ONE_KEY);
        buildingOne.setName(BUILDING_ONE_NAME);

        buildingTwo = new Building();
        buildingTwo.setBuildingKey(BUILDING_TWO_KEY);
        buildingTwo.setName(BUILDING_TWO_NAME);

        List<Garage> garages = new ArrayList<>();
        garages.add(garageOne);
        garages.add(garageTwo);

        List<ParkingSpace> parkingSpaces = new ArrayList<>();
        parkingSpaces.add(parkingSpaceOne);
        parkingSpaces.add(parkingSpaceTwo);
        parkingSpaces.add(parkingSpaceThree);
        parkingSpaces.add(parkingSpaceFour);

        List<PermitType> permitTypes = new ArrayList<> ();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);
        permitTypes.add(permitTypeThree);
        permitTypes.add(permitTypeFour);

        List<SpaceType> spaceTypes = new ArrayList<> ();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);
        spaceTypes.add(spaceTypeThree);
        spaceTypes.add(spaceTypeFour);


        List<Building> buildings = new ArrayList<>();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        garageRepository = mock(GarageRepository.class);
        when(garageRepository.findByKey(GARAGE_ONE_KEY)).thenReturn(garageOne);
        when(garageRepository.findByKey(GARAGE_TWO_KEY)).thenReturn(garageTwo);
        when(garageRepository.findAll()).thenReturn(garages);
        doNothing().when(garageRepository).save(any(Garage.class));
        doNothing().when(garageRepository).delete(anyString());

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

        buildingRepository = mock(BuildingRepository.class);
        when(buildingRepository.findAll()).thenReturn(buildings);
        when(buildingRepository.findByKey(BUILDING_ONE_KEY)).thenReturn(buildingOne);
        when(buildingRepository.findByKey(BUILDING_TWO_KEY)).thenReturn(buildingTwo);
        doNothing().when(buildingRepository).save(any(Building.class));
        doNothing().when(buildingRepository).delete(anyString());

        userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(anyString());
        doNothing().when(userRepository).save(any(User.class));


        googleMapService = new GoogleMapService();
        travelDistanceDurationRepository = mock(TravelDistanceDurationRepository.class);

        analyticsController = new AnalyticsController(
                garageRepository,
                buildingRepository,
                parkingSpaceRepository,
                travelDistanceDurationRepository,
                googleMapService,
                permitTypeRepository,
                spaceTypeRepository,
                userRepository
        );
    }

    @Test
    public void testIndex() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = analyticsController.index(model);

        List<Building> buildings = new ArrayList<>();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        assertEquals("analytics/index", returnURL);
        assertEquals(buildings, model.get("buildings"));
    }

    @Test
    public void testLocate_Get() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = analyticsController.locate(model);

        List<Building> buildings = new ArrayList<>();
        buildings.add(buildingOne);
        buildings.add(buildingTwo);

        List<PermitType> permitTypes = new ArrayList<>();
        permitTypes.add(permitTypeOne);
        permitTypes.add(permitTypeTwo);
        permitTypes.add(permitTypeThree);
        permitTypes.add(permitTypeFour);

        List<SpaceType> spaceTypes = new ArrayList<>();
        spaceTypes.add(spaceTypeOne);
        spaceTypes.add(spaceTypeTwo);
        spaceTypes.add(spaceTypeThree);
        spaceTypes.add(spaceTypeFour);

        assertEquals(buildings, model.get("buildings"));
        assertEquals(permitTypes, model.get("permitTypes"));
        assertEquals(spaceTypes, model.get("spaceTypes"));
        assertEquals("analytics/locate", returnURL);
    }

    @Test
    public void testLocate_Post() {
        //TODO
    }

    @Test
    public void testBuilding() {
        ExtendedModelMap model = new ExtendedModelMap();

        String returnURL = analyticsController.building(
                BUILDING_ONE_KEY,
                model
        );

        assertTrue(model.containsKey("building"));
        assertTrue(model.containsKey("travelDistanceDurations"));
        assertEquals("analytics/building", returnURL);
    }
}
