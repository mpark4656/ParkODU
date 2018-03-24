package edu.odu.cs.gold;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.*;
import edu.odu.cs.gold.model.Location;

import edu.odu.cs.gold.service.GoogleMapService;
import edu.odu.cs.gold.service.PermitTypeService;
import edu.odu.cs.gold.service.SpaceTypeService;
import edu.odu.cs.gold.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.access.method.P;



import com.google.maps.model.*;

import java.util.*;

@SpringBootApplication
public class ParkODUApplication implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;
    private boolean dataInitialized = true;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private TravelDistanceDurationRepository travelDistanceDurationRepository;

    @Autowired
    private FloorStatisticRepository floorStatisticRepository;

    @Autowired
    private PermitTypeRepository permitTypeRepository;

    @Autowired
    private SpaceTypeRepository spaceTypeRepository;

    @Autowired
    private GoogleMapService googleMapService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleTypeRepository roleTypeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PermitTypeService permitTypeService;

    @Autowired
    private SpaceTypeService spaceTypeService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        garageRepository.loadAll();
        floorRepository.loadAll();
        parkingSpaceRepository.loadAll();
        floorStatisticRepository.loadAll();
        buildingRepository.loadAll();
        travelDistanceDurationRepository.loadAll();
        permitTypeRepository.loadAll();
        spaceTypeRepository.loadAll();
        userRepository.loadAll();
        roleTypeRepository.loadAll();

        // Remove all null stored users at startup
        String isEmpty = "";
        Predicate predicatetemp = Predicates.equal("userKey",isEmpty);
        userRepository.deleteByPredicate(predicatetemp);

        System.out.println("# of Garages loaded from Mongo: " + garageRepository.findAll().size());
        System.out.println("# of Floors loaded from Mongo: " + floorRepository.findAll().size());
        System.out.println("# of ParkingSpaces loaded from Mongo: " + parkingSpaceRepository.findAll().size());
        System.out.println("# of FloorStatistics loaded from Mongo: " + floorStatisticRepository.findAll().size());
        System.out.println("# of Buildings loaded from Mongo: " + buildingRepository.findAll().size());
        System.out.println("# of TravelDistanceDurations loaded from Mongo: " + travelDistanceDurationRepository.findAll().size());
        System.out.println("# of PermitTypes loaded from Mongo: " + permitTypeRepository.findAll().size());
        System.out.println("# of SpaceTypes loaded from Mongo: " + spaceTypeRepository.findAll().size());
        System.out.println("# of Users loaded from Mongo: " + userRepository.findAll().size());
        System.out.println("# of Roles loaded from Mongo: " + roleTypeRepository.findAll().size());




        if (false) {
                    /*
            Predicate predicate = Predicates.and(
                    Predicates.equal("garageKey", "80696948-bfe8-42f4-a861-af9e368fd34e"),
                    Predicates.or(
                        Predicates.equal("floor", "2"),
                        Predicates.equal("floor", "3"),
                        Predicates.equal("floor", "5")
                    )
            );
                    */

            Predicate predicate = Predicates.equal("garageKey", "6c913f81-4a1a-488c-825a-abb2348ab4e9");
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);
            for (ParkingSpace parkingSpace : parkingSpaces) {
                switch (parkingSpace.getFloor()) {
                    case "1":
                        parkingSpace.setPermitType("Metered");
                        break;
                    case "2":
                        if (parkingSpace.getNumber() <= 81) {
                            parkingSpace.setPermitType("Metered");
                        }
                        else {
                            parkingSpace.setPermitType("Faculty");
                        }
                        break;
                    case "3":
                        parkingSpace.setPermitType("Faculty");
                        break;
                    case "4":
                        if (parkingSpace.getNumber() <= 81) {
                            parkingSpace.setPermitType("Faculty");
                        }
                        else {
                            parkingSpace.setPermitType("Quad Resident");
                        }
                        break;
                    case "5":
                        parkingSpace.setPermitType("Quad Resident");
                        break;
                }
            }
            parkingSpaceRepository.save(parkingSpaces);
        }

        // Duplicate FloorStatistics to all Levels
        if (false) {
            Predicate floorPredicate = Predicates.notEqual("floorKey", "7545a113-e926-4d89-8a16-13fc00215bd8");
            List<Floor> floors = floorRepository.findByPredicate(floorPredicate);
            Predicate predicate = Predicates.equal("floorKey", "7545a113-e926-4d89-8a16-13fc00215bd8");
            List<FloorStatistic> floorStatistics = floorStatisticRepository.findByPredicate(predicate);
            for (Floor floor : floors) {
                for (FloorStatistic floorStatistic : floorStatistics) {
                    floorStatistic.setFloorStatisticKey(UUID.randomUUID().toString());
                    floorStatistic.setFloorKey(floor.getFloorKey());
                    floorStatisticRepository.save(floorStatistic);
                }
            }
        }
/*
        // Generate Walking Distance Data for all Buildings from all Garages
        if (false) {
            List<Garage> garages = new ArrayList<>(garageRepository.findAll());
            List<Building> buildings = new ArrayList<>(buildingRepository.findAll());

            List<TravelDistanceDuration> travelDistanceDurations = new ArrayList<>();
            for (Building building : buildings) {
                System.out.println("Building: " + building.getName());
                for (Garage garage : garages) {

                    Predicate predicate = Predicates.and(
                            Predicates.equal("garageKey", garage.getGarageKey()),
                            Predicates.equal("buildingKey", building.getBuildingKey())
                    );
                    List<TravelDistanceDuration> existingTravelDistanceDurations = travelDistanceDurationRepository.findByPredicate(predicate);
                    TravelDistanceDuration travelDistanceDuration = googleMapService.getTravelDistanceDuration(garage, building, TravelMode.WALKING);
                    if (!existingTravelDistanceDurations.isEmpty()) {
                        TravelDistanceDuration existingTravelDistanceDuration = existingTravelDistanceDurations.get(0);
                        travelDistanceDuration.setTravelDistanceDurationKey(existingTravelDistanceDuration.getTravelDistanceDurationKey());
                        travelDistanceDurations.add(travelDistanceDuration);
                    }
                    else {
                        travelDistanceDurations.add(travelDistanceDuration);
                    }
                    System.out.println(travelDistanceDuration);
                }
            }
            travelDistanceDurationRepository.save(travelDistanceDurations);
        }
*/
        /*
        if (false) {
            Building collegeOfHealthSciences = new Building("College of Health Sciences", 36.885792, -76.302185);
            Building battenCollegeOfEngineeringAndTechnology = new Building("Batten College of Engineering & Technology", 36.885774, -76.303579);
            Building engineeringSystemsBuilding = new Building("Engineering Systems Building", 36.885774, -76.303579);
            Building alfriendChemistryBuilding = new Building("Alfriend Chemistry Building", 36.885349, -76.305022);
            Building stromeCollegeOfBusiness = new Building("Strome College of Business", 36.88666, -76.306439);

            List<Building> buildings = new ArrayList<>();
            buildings.add(collegeOfHealthSciences);
            buildings.add(battenCollegeOfEngineeringAndTechnology);
            buildings.add(engineeringSystemsBuilding);
            buildings.add(alfriendChemistryBuilding);
            buildings.add(stromeCollegeOfBusiness);
            buildingRepository.save(buildings);
        }
        */

        // Fill all Garage Floors with Parking Spaces
        if (false) {
            List<Garage> garages = new ArrayList<>(garageRepository.findAll());
            for (Garage garage : garages) {
                System.err.println(garage.getName());
                Predicate floorPredicate = Predicates.equal("garageKey", garage.getGarageKey());
                List<Floor> floors = floorRepository.findByPredicate(floorPredicate);
                for (Floor floor : floors) {

                    Random random = new Random();
                    int numAvailable = random.nextInt(5) + 1;
                    Set<Integer> availableParkingSpaceNumbers = new HashSet<>();
                    for (int i = 0; i < numAvailable; i++) {
                        availableParkingSpaceNumbers.add(random.nextInt(162));
                    }

                    List<ParkingSpace> parkingSpaces = new ArrayList<>();
                    for (int i = 1; i <= 162; i++) {
                        ParkingSpace parkingSpace = new ParkingSpace();
                        parkingSpace.setGarageKey(garage.getGarageKey());
                        parkingSpace.setFloor(floor.getNumber());
                        parkingSpace.setNumber(i);

                        if (availableParkingSpaceNumbers.contains(i)) {
                            parkingSpace.setAvailable(true);
                        }
                        else {
                            parkingSpace.setAvailable(false);
                        }

                        parkingSpaces.add(parkingSpace);
                    }

                    System.err.println("Floor " + floor.getNumber() + ": " + parkingSpaces.size() + " with " + numAvailable + " available spaces.");
                    parkingSpaceRepository.save(parkingSpaces);
                }
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkODUApplication.class, args);
    }

}