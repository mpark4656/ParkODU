package edu.odu.cs.gold.restcontroller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/rest/parkingspace/")
public class ParkingSpaceRestController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private GarageService garageService;

    public ParkingSpaceRestController(GarageRepository garageRepository,
                               FloorRepository floorRepository,
                               ParkingSpaceRepository parkingSpaceRepository,
                               GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.garageService = garageService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ParkingSpace parkingSpace) {
        if (parkingSpace.getGarageKey() != null && parkingSpace.getFloor() != null && parkingSpace.getNumber() != null) {
            parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
            parkingSpace.setAvailable(true);
            parkingSpaceRepository.save(parkingSpace);
            garageService.refresh(parkingSpace.getGarageKey());
            System.out.println("Saved ParkingSpace: " + parkingSpace);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody ParkingSpace parkingSpace) {
        ParkingSpace existingParkingSpace = parkingSpaceRepository.findByKey(parkingSpace.getParkingSpaceKey());
        if (existingParkingSpace != null) {
            // Upsert - if null, don't update the field
            if (parkingSpace.getNumber() != null) {
                existingParkingSpace.setNumber(parkingSpace.getNumber());
            }
            existingParkingSpace.setAvailable(parkingSpace.isAvailable());
            parkingSpaceRepository.save(existingParkingSpace);
            System.out.println("Updated ParkingSpace: " + existingParkingSpace);
            garageService.refresh(existingParkingSpace.getGarageKey());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/clean")
    public ResponseEntity<?> clean() {
        Predicate predicate = Predicates.or(
                Predicates.equal("garageKey", null),
                Predicates.equal("floor", null)
        );
        int numDeleted = parkingSpaceRepository.deleteByPredicate(predicate);
        System.out.println("Deleted " + numDeleted + " ParkingSpaces with null values");
        return ResponseEntity.ok().build();
    }
}
