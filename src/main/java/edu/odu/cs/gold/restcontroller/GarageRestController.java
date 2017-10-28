package edu.odu.cs.gold.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/garage/")
public class GarageRestController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    public GarageRestController(GarageRepository garageRepository,
                                FloorRepository floorRepository,
                                ParkingSpaceRepository parkingSpaceRepository) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @GetMapping("/refresh/{garageKey}")
    public int refresh(@PathVariable String garageKey) {
        Garage garage = garageRepository.findByKey(garageKey);
        Predicate floorPredicate = Predicates.equal("garageKey", garageKey);
        List<Floor> floors = floorRepository.findByPredicate(floorPredicate);
        for (Floor floor : floors) {
            Predicate parkingSpacePredicate = Predicates.and(
                    Predicates.equal("garageKey", garageKey),
                    Predicates.equal("floor", floor.getNumber()),
                    Predicates.equal("available", true)
            );
            floor.setAvailableSpaces(parkingSpaceRepository.countByPredicate(parkingSpacePredicate));
        }
        garageRepository.save(garage);
        return garage.getAvailableSpaces();
    }

    @GetMapping("/total/{garageKey}")
    public int totalCount(@PathVariable String garageKey) {
        System.out.println("Called totalCount for garageKey: " + garageKey);
        Predicate predicate = Predicates.equal("garageKey", garageKey);
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Parking Spaces: " + count);
        return count;
    }

    @GetMapping("/available/{garageKey}")
    public int availableCount(@PathVariable String garageKey) {
        System.out.println("Called availableCount for garageKey: " + garageKey);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("available", "true"));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Available Parking Spaces: " + count);
        return count;
    }

    @GetMapping("/total/{garageKey}/number/{number}")
    public int totalCountByFloor(@PathVariable("garageKey") String garageKey,
                                    @PathVariable("number") String floor) {
        System.out.println("Called totalCountByFloor for garageKey: " + garageKey + ", number: " + floor);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("number", floor));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Parking Spaces on Floor " + floor + ": " + count);
        return count;
    }

    @GetMapping("/available/{garageKey}/number/{number}")
    public int availableCountByFloor(@PathVariable("garageKey") String garageKey,
                                    @PathVariable("number") String floor) {
        System.out.println("Called availableCountByFloor for garageKey: " + garageKey + ", number: " + floor);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("number", floor),
                Predicates.equal("available", "true"));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Available Parking Spaces on Floor " + floor + ": " + count);
        return count;
    }

    @GetMapping("/get/{garageKey}")
    public Garage get(@PathVariable String garageKey) {
        Garage garage = garageRepository.findByKey(garageKey);
        return garage;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Garage garage) {
        if (garage.getName() != null) {
            garage.setGarageKey(UUID.randomUUID().toString());
            garageRepository.save(garage);
            System.out.println("Added Garage: " + garage);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Garage garage) {
        Garage existingGarage = garageRepository.findByKey(garage.getGarageKey());
        if (existingGarage != null) {
            // Upsert - if null, don't update the field
            if (garage.getName() != null) {
                existingGarage.setName(garage.getName());
            }
            if (garage.getDescription() != null) {
                existingGarage.setDescription(garage.getDescription());
            }
            if (garage.getHeightDescription() != null) {
                existingGarage.setHeightDescription(garage.getHeightDescription());
            }
            if (garage.getAddressOne() != null) {
                existingGarage.setAddressOne(garage.getAddressOne());
            }
            if (garage.getAddressTwo() != null) {
                existingGarage.setAddressTwo(garage.getAddressTwo());
            }
            if (garage.getCity() != null) {
                existingGarage.setCity(garage.getCity());
            }
            if (garage.getState() != null) {
                existingGarage.setState(garage.getState());
            }
            if (garage.getZipCode() != null) {
                existingGarage.setZipCode(garage.getZipCode());
            }
            existingGarage.setLastUpdated(new Date());

            garageRepository.save(existingGarage);
            System.out.println("Updated Garage: " + existingGarage);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Garage garage) {
        if (garage.getGarageKey() != null) {
            Garage existingGarage = garageRepository.findByKey(garage.getGarageKey());
            if (existingGarage != null) {
                garageRepository.delete(garage.getGarageKey());
                System.out.println("Deleted Garage with garageKey: " + garage.getGarageKey());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
