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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.method.P;



import com.google.maps.model.*;

import java.util.*;

@SpringBootApplication
@EnableScheduling
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
    private GarageStatisticRepository garageStatisticRepository;

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
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PermitTypeService permitTypeService;

    @Autowired
    private SpaceTypeService spaceTypeService;

    @Autowired
    private FloorStatisticService floorStatisticService;

    @Autowired
    private GarageStatisticService garageStatisticService;

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
        eventRepository.loadAll();
        garageStatisticRepository.loadAll();

        // Remove all null stored users at startup
        String isEmpty = "";
        Predicate predicatetemp = Predicates.equal("userKey",isEmpty);
        userRepository.deleteByPredicate(predicatetemp);

        System.out.println("# of Garages loaded from Mongo: " + garageRepository.findAll().size());
        System.out.println("# of Floors loaded from Mongo: " + floorRepository.findAll().size());
        System.out.println("# of ParkingSpaces loaded from Mongo: " + parkingSpaceRepository.findAll().size());
        System.out.println("# of FloorStatistics loaded from Mongo: " + floorStatisticRepository.findAll().size());
        System.out.println("# of GarageStatistics loaded from Mongo: " + garageStatisticRepository.findAll().size());
        System.out.println("# of Buildings loaded from Mongo: " + buildingRepository.findAll().size());
        System.out.println("# of TravelDistanceDurations loaded from Mongo: " + travelDistanceDurationRepository.findAll().size());
        System.out.println("# of PermitTypes loaded from Mongo: " + permitTypeRepository.findAll().size());
        System.out.println("# of SpaceTypes loaded from Mongo: " + spaceTypeRepository.findAll().size());
        System.out.println("# of Users loaded from Mongo: " + userRepository.findAll().size());
        System.out.println("# of Roles loaded from Mongo: " + roleTypeRepository.findAll().size());
        System.out.println("# of Events loaded from Mongo: " + eventRepository.findAll().size());

        User user = new User();
        user.setUserKey(UUID.randomUUID().toString());
        user.setConfirmationToken(UUID.randomUUID().toString());
        user.setFirstName("Usman");
        user.setLastName("Sermello");
        user.setUsername("user");
        user.setEmail("user@odu.edu");
        user.setPassword("password");
        user.getPermissions().add("USER");
        user.setEnabled(true);
        userRepository.save(user);

        User admin = new User();
        admin.setUserKey(UUID.randomUUID().toString());
        admin.setConfirmationToken(UUID.randomUUID().toString());
        admin.setFirstName("Adriana");
        admin.setLastName("Minunoz");
        admin.setUsername("admin");
        admin.setEmail("root@odu.edu");
        admin.setPassword("password");
        admin.getPermissions().add("USER");
        admin.getPermissions().add("ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkODUApplication.class, args);
    }

}