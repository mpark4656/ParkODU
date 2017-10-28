package edu.odu.cs.gold;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class GoldApplication implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;
    private boolean dataInitialized = true;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        garageRepository.loadAll();
        parkingSpaceRepository.loadAll();

        /*
        // Manually Create ParkingSpaces
        for (int i = 1; i <= 73; i++) {
            ParkingSpace parkingSpace = new ParkingSpace();
            parkingSpace.setGarageKey("8774fdbb-a70e-4e0b-94b1-cad60f08af55");
            parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
            parkingSpace.setFloor("1");
            parkingSpace.setAvailable(true);
            parkingSpace.setNumber(i);
            parkingSpaceRepository.save(parkingSpace);
            System.err.println("Saved Parking Space: " + parkingSpace);
        }
        */

        /*
        // Manually Delete ParkingSpaces
        Predicate predicate = Predicates.equal("garageKey", "8774fdbb-a70e-4e0b-94b1-cad60f08af55");
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);
        for (ParkingSpace parkingSpace : parkingSpaces) {
            System.err.println("Deleting ParkingSpace: " + parkingSpace.getParkingSpaceKey());
            parkingSpaceRepository.delete(parkingSpace.getParkingSpaceKey());
        }
        */

        if (!dataInitialized) {
            System.out.println("*** Initializing Dummy Data ***");

            Garage garage1 = new Garage();
            garage1.setGarageKey("8774fdbb-a70e-4e0b-94b1-cad60f08af55");
            garage1.setName("Garage A");
            garage1.setAddressOne("123 Parking Ave");
            garage1.setCity("Parkville");
            garage1.setState("VA");
            garage1.setZipCode("12345");
            garageRepository.save(garage1);
            System.out.println("Garage 1: " + garage1);

            Floor floor1 = new Floor();
            floor1.setFloorKey(UUID.randomUUID().toString());
            floor1.setGarageKey(garage1.getGarageKey());
            floor1.setNumber("1");
            floorRepository.save(floor1);

            Floor floor2 = new Floor();
            floor2.setFloorKey(UUID.randomUUID().toString());
            floor2.setGarageKey(garage1.getGarageKey());
            floor2.setNumber("2");
            floorRepository.save(floor2);

            Floor floor3 = new Floor();
            floor3.setFloorKey(UUID.randomUUID().toString());
            floor3.setGarageKey(garage1.getGarageKey());
            floor3.setNumber("3");
            floorRepository.save(floor3);

            int count = 1;
            // Create 1st Floor
            for (int i = 0; i < 10; i++) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                parkingSpace.setGarageKey(garage1.getGarageKey());
                parkingSpace.setNumber(count);
                parkingSpace.setAvailable(true);
                parkingSpace.setFloor("1");
                parkingSpaceRepository.save(parkingSpace);
                count++;
            }
            // Create 2nd Floor
            for (int i = 0; i < 10; i++) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                parkingSpace.setGarageKey(garage1.getGarageKey());
                parkingSpace.setNumber(count);
                parkingSpace.setAvailable(false);
                parkingSpace.setFloor("2");
                parkingSpaceRepository.save(parkingSpace);
                count++;
            }
            // Create 3rd Floor
            for (int i = 0; i < 10; i++) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                parkingSpace.setGarageKey(garage1.getGarageKey());
                parkingSpace.setNumber(count);
                parkingSpace.setAvailable(true);
                parkingSpace.setFloor("3");
                parkingSpaceRepository.save(parkingSpace);
                count++;
            }
            System.out.println("Created 3 Floors for Garage 1 with 10 Parking Spaces each.");

            System.out.println("*** Finished Initializing Dummy Data ***");
            dataInitialized = false;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GoldApplication.class, args);
    }

}