package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.model.PermitType;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.PermitTypeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PermitTypeService {

    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;

    /**
     * Constructor
     * @param parkingSpaceRepository ParkingSpaceRepository
     * @param permitTypeRepository PermitTypeRepository
     */
    public PermitTypeService(ParkingSpaceRepository parkingSpaceRepository,
                            PermitTypeRepository permitTypeRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.permitTypeRepository = permitTypeRepository;
    }

    /**
     * Refresh the permit type information on all parking spaces
     */
    public void refresh() {
        System.err.println("Refreshing all Permit Type Data for");

        // Get a list of all current existing permit types
        List<PermitType> permitTypes = new ArrayList<>(permitTypeRepository.findAll());

        // For each permit type, find all spaces that have it and update the information
        for(PermitType permitType: permitTypes) {

            // Find all spaces that have the specified permitTypeKey
            Predicate parkingSpacePredicate = Predicates.equal("permitTypeKey", permitType.getPermitTypeKey());
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

            // Update each parking space with the new permit type information
            for(ParkingSpace parkingSpace : parkingSpaces) {
                parkingSpace.setPermitTypeKey(permitType.getPermitTypeKey());
                parkingSpace.setPermitType(permitType.getName());
                parkingSpace.setLastUpdated(new Date());
            }

            // Save the updated parking space collection
            parkingSpaceRepository.save(parkingSpaces);
        }

        // Get a list of all current existing parking spaces
        List<ParkingSpace> parkingSpaces = new ArrayList<>(parkingSpaceRepository.findAll());

        // String list of permit type keys
        ArrayList<String> permitTypeKeys = new ArrayList<> ();

        // Store all existing permit type keys as String
        for(PermitType permitType : permitTypes) {
            permitTypeKeys.add(permitType.getPermitTypeKey());
        }

        // If any of the existing space does not have the permit type key that currently exists, wipe out
        // the permit type information.
        for(ParkingSpace parkingSpace : parkingSpaces) {
            if(!permitTypeKeys.contains(parkingSpace.getPermitTypeKey())) {
                parkingSpace.setPermitType(null);
                parkingSpace.setPermitTypeKey(null);
            }
        }

        parkingSpaceRepository.save(parkingSpaces);

    }

    /**
     * Refresh the permit type of the specified key in all parking spaces
     * @param permitTypeKey String
     */
    public void refresh(String permitTypeKey) {
        System.err.println("Refreshing Permit Type Data for " + permitTypeKey);

        // First, if the permit type key is null or empty, print error and return
        if(permitTypeKey == null || permitTypeKey.isEmpty()) {
            System.err.println("permitTypeKey cannot be null or empty");
            return;
        }

        // Find the permit type object with the given key
        PermitType permitType = permitTypeRepository.findByKey(permitTypeKey);


        // This permit type does not exist. Find all spaces that have this permit type and set all to null
        if(permitType == null) {
            Predicate parkingSpacePredicate = Predicates.equal("permitTypeKey", permitTypeKey);
            List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

            for(ParkingSpace parkingSpace : parkingSpaces) {
                parkingSpace.setPermitType(null);
                parkingSpace.setPermitTypeKey(null);
            }

            // Save and then exit
            parkingSpaceRepository.save(parkingSpaces);
            return;
        }

        // Find all spaces that have the specified permitTypeKey
        Predicate parkingSpacePredicate = Predicates.equal("permitTypeKey", permitTypeKey);
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(parkingSpacePredicate);

        // Update each parking space with the new permit type information
        for(ParkingSpace parkingSpace : parkingSpaces) {
            parkingSpace.setPermitTypeKey(permitType.getPermitTypeKey());
            parkingSpace.setPermitType(permitType.getName());
            parkingSpace.setLastUpdated(new Date());
        }

        // Save the updated parking space collection
        parkingSpaceRepository.save(parkingSpaces);
    }
}
