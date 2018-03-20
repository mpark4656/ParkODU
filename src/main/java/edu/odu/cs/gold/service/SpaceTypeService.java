package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.SpaceType;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.SpaceTypeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SpaceTypeService {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private SpaceTypeRepository spaceTypeRepository;

    /**
     * Constructor
     * @param garageRepository GarageRepository
     * @param floorRepository FloorRepository
     * @param parkingSpaceRepository ParkingSpaceRepository
     * @param spaceTypeRepository PermitTypeRepository
     */
    public SpaceTypeService(GarageRepository garageRepository,
                             FloorRepository floorRepository,
                             ParkingSpaceRepository parkingSpaceRepository,
                             SpaceTypeRepository spaceTypeRepository) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.spaceTypeRepository = spaceTypeRepository;
    }

    /**
     * Refresh the space type information on all parking spaces
     */
    public void refresh() {
        System.err.println("Refreshing all space Type Data for");

        // Get a list of all current existing space types
        List<SpaceType> spaceTypes = new ArrayList<>(spaceTypeRepository.findAll());

        // For each space type, find all spaces that have it and update the information
        for(SpaceType spaceType: spaceTypes) {

            // Find all spaces that have the specified spaceTypeKey
            Predicate parkingSpacePredicate = Predicates.equal("spaceTypeKey", spaceType.getSpaceTypeKey());
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

            // Update each parking space with the new space type information
            for(ParkingSpace parkingSpace : parkingSpaces) {
                parkingSpace.setSpaceTypeKey(spaceType.getSpaceTypeKey());
                parkingSpace.setSpaceType(spaceType.getName());
                parkingSpace.setLastUpdated(new Date());
            }

            // Save the updated parking space collection
            parkingSpaceRepository.save(parkingSpaces);
        }

        // Get a list of all current existing parking spaces
        List<ParkingSpace> parkingSpaces = new ArrayList<>(parkingSpaceRepository.findAll());

        // String list of space type keys
        ArrayList<String> spaceTypeKeys = new ArrayList<> ();

        // Store all existing space type keys as String
        for(SpaceType spaceType : spaceTypes) {
            spaceTypeKeys.add(spaceType.getSpaceTypeKey());
        }

        // If any of the existing space does not have the space type key that currently exists, wipe out
        // the space type information.
        for(ParkingSpace parkingSpace : parkingSpaces) {
            if(!spaceTypeKeys.contains(parkingSpace.getSpaceTypeKey())) {
                parkingSpace.setSpaceType(null);
                parkingSpace.setSpaceTypeKey(null);
            }
        }

        parkingSpaceRepository.save(parkingSpaces);

    }

    /**
     * Refresh the space type of the specified key in all parking spaces
     * @param spaceTypeKey String
     */
    public void refresh(String spaceTypeKey) {
        System.err.println("Refreshing Space Type Data for " + spaceTypeKey);

        // First, if the space type key is null or empty, print error and return
        if(spaceTypeKey == null || spaceTypeKey.isEmpty()) {
            System.err.println("spaceTypeKey cannot be null or empty");
            return;
        }

        // Find the space type object with the given key
        SpaceType spaceType = spaceTypeRepository.findByKey(spaceTypeKey);


        // This space type does not exist. Find all spaces that have this space type and set all to null
        if(spaceType == null) {
            Predicate parkingSpacePredicate = Predicates.equal("spaceTypeKey", spaceTypeKey);
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

            for(ParkingSpace parkingSpace : parkingSpaces) {
                parkingSpace.setSpaceType(null);
                parkingSpace.setSpaceTypeKey(null);
            }

            // Save and then exit
            parkingSpaceRepository.save(parkingSpaces);
            return;
        }

        // Find all spaces that have the specified spaceTypeKey
        Predicate parkingSpacePredicate = Predicates.equal("spaceTypeKey", spaceTypeKey);
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

        // Update each parking space with the new space type information
        for(ParkingSpace parkingSpace : parkingSpaces) {
            parkingSpace.setSpaceTypeKey(spaceType.getSpaceTypeKey());
            parkingSpace.setSpaceType(spaceType.getName());
            parkingSpace.setLastUpdated(new Date());
        }

        // Save the updated parking space collection
        parkingSpaceRepository.save(parkingSpaces);
    }
}
