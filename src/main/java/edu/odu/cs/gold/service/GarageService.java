package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GarageService {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    public GarageService(GarageRepository garageRepository,
                         FloorRepository floorRepository,
                         ParkingSpaceRepository parkingSpaceRepository) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public void refresh(String garageKey) {
        System.err.println("Refreshing Garage Data");

        Garage garage = garageRepository.findByKey(garageKey);
        Predicate floorPredicate = Predicates.equal("garageKey", garageKey);
        List<Floor> floors = floorRepository.findByPredicate(floorPredicate);
        int garageTotalAvailableSpaces = 0;
        int garageTotalSpaces = 0;
        for (Floor floor : floors) {
            Predicate availableParkingSpacePredicate = Predicates.and(
                    Predicates.equal("garageKey", garageKey),
                    Predicates.equal("floor", floor.getNumber()),
                    Predicates.equal("available", true)
            );
            int floorTotalAvailableSpaces = parkingSpaceRepository.countByPredicate(availableParkingSpacePredicate);

            Predicate totalSpacePredicate = Predicates.and(
                    Predicates.equal("garageKey", garageKey),
                    Predicates.equal("floor", floor.getNumber())
            );
            int floorTotalSpaces = parkingSpaceRepository.countByPredicate(totalSpacePredicate);

            floor.setAvailableSpaces(floorTotalAvailableSpaces);
            floor.setTotalSpaces(floorTotalSpaces);
            floor.calculateCapacity();
            floor.setLastUpdated(new Date());
            garageTotalAvailableSpaces += floorTotalAvailableSpaces;
            garageTotalSpaces += floorTotalSpaces;
        }
        garage.setAvailableSpaces(garageTotalAvailableSpaces);
        garage.setTotalSpaces(garageTotalSpaces);
        garage.calculateCapacity();
        garage.setLastUpdated(new Date());
        garageRepository.save(garage);
        floorRepository.save(floors);
    }

    public void save(Garage entity) {
        garageRepository.save(entity);
    }
}
