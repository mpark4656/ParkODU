package edu.odu.cs.gold.configuration;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
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

import java.util.UUID;

@SpringBootApplication
public class GoldApplication implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;
    private boolean dataInitialized = true;

    @Autowired
    private GarageRepository garageRepository;

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

            int count = 1;
            // Create 1st Floor
            for (int i = 0; i < 10; i++) {
                ParkingSpace parkingSpace = new ParkingSpace();
                parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                parkingSpace.setGarageKey(garage1.getGarageKey());
                parkingSpace.setParkingSpaceNumber(count);
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
                parkingSpace.setParkingSpaceNumber(count);
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
                parkingSpace.setParkingSpaceNumber(count);
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