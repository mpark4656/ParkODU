package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;

public class ParkingSpaceService {

    private ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public boolean save(ParkingSpace entity) {
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", entity.getGarageKey()),
                Predicates.equal("parkingSpaceNumber", entity.getNumber())
        );
        if (parkingSpaceRepository.countByPredicate(predicate) > 0) {
            // TODO - Log Error
            System.err.println("Error: A Parking Space with GarageKey " + entity.getGarageKey() + " and Parking Space Number " + entity.getNumber() + " already exists.");
            return false;
        }
        parkingSpaceRepository.save(entity);
        return true;
    }
}
