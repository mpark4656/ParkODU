package edu.odu.cs.gold;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private FloorStatisticRepository floorStatisticRepository;

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

        if (false) {
            for (int j = 2; j <= 4; j++) {
                Random random = new Random();
                int numAvailable = random.nextInt(4);
                Predicate predicate = Predicates.and(
                        Predicates.equal("garageKey", "8774fdbb-a70e-4e0b-94b1-cad60f08af55"),
                        Predicates.equal("floor", j + "")
                );
                List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);
                System.err.println(parkingSpaces.size());
                List<Integer> nums = new ArrayList<>();
                for (int i = 0; i < numAvailable; i++) {
                    nums.add(random.nextInt(parkingSpaces.size()));
                }
                for (ParkingSpace parkingSpace : parkingSpaces) {
                    if (nums.contains(parkingSpace.getNumber())) {
                        parkingSpace.setAvailable(true);
                    }
                    else {
                        parkingSpace.setAvailable(false);
                    }
                    parkingSpaceRepository.save(parkingSpace);
                }
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkODUApplication.class, args);
    }

}