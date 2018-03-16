package edu.odu.cs.gold.restcontroller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/rest/floor/")
public class FloorRestController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;

    public FloorRestController(GarageRepository garageRepository,
                                FloorRepository floorRepository,
                                ParkingSpaceRepository parkingSpaceRepository) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Floor floor) {
        if (floor.getGarageKey() != null && floor.getNumber() != null && floor.getTotalSpaces() != null && floor.getDescription() != null) {
            // Check if a Floor already exists with the given garageKey and number
            Predicate predicate = Predicates.and(
                    Predicates.equal("garageKey", floor.getGarageKey()),
                    Predicates.equal("number", floor.getNumber())
            );
            int existingCount = floorRepository.countByPredicate(predicate);
            if (existingCount == 0) {
                // Check if the Garage with the given garageKey exists
                Garage garage = garageRepository.findByKey(floor.getGarageKey());
                if (garage != null) {

                    // Create Floor
                    floor.setFloorKey(UUID.randomUUID().toString());
                    floorRepository.save(floor);
                    System.out.println("Saved Floor: " + floor);

                    // Create ParkingSpaces
                    List<ParkingSpace> parkingSpaces = new ArrayList<>();
                    for (int i = 1; i <= floor.getTotalSpaces(); i++) {
                        ParkingSpace parkingSpace = new ParkingSpace();
                        parkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                        parkingSpace.setFloor(floor.getNumber());
                        parkingSpace.setGarageKey(floor.getGarageKey());
                        parkingSpace.setNumber(i);
                        parkingSpace.setAvailable(true);
                        parkingSpaces.add(parkingSpace);
                    }
                    parkingSpaceRepository.save(parkingSpaces);
                    System.out.println("Added " + parkingSpaces.size() + " ParkingSpaces with garageKey " + floor.getGarageKey() + " for floor " + floor.getNumber());

                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Floor floor) {
        if (floor.getFloorKey() != null) {
            Floor existingFloor = floorRepository.findByKey(floor.getFloorKey());
            if (existingFloor != null) {

                // Upsert - the only editable field is description
                if (floor.getDescription() != null) {
                    existingFloor.setDescription(floor.getDescription());
                }
                if (floor.getTotalSpaces() != null && floor.getTotalSpaces() != existingFloor.getTotalSpaces()) {

                    Predicate predicate = Predicates.and(
                            Predicates.equal("garageKey", existingFloor.getGarageKey()),
                            Predicates.equal("floor", existingFloor.getNumber())
                    );
                    List<ParkingSpace> existingParkingSpaces = parkingSpaceRepository.findByPredicate(predicate);
                    Map<Integer, ParkingSpace> existingParkingSpaceMap = new HashMap<>();
                    for (ParkingSpace existingParkingSpace : existingParkingSpaces) {
                        existingParkingSpaceMap.put(existingParkingSpace.getNumber(), existingParkingSpace);
                    }

                    // Add
                    List<ParkingSpace> newParkingSpaces = new ArrayList<>();
                    int numDeleted = 0;

                    if (floor.getTotalSpaces() > existingFloor.getTotalSpaces()) {
                        for (int i = 1; i <= floor.getTotalSpaces(); i++) {
                            if (!existingParkingSpaceMap.containsKey(i)) {
                                ParkingSpace newParkingSpace = new ParkingSpace();
                                newParkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                                newParkingSpace.setFloor(existingFloor.getNumber());
                                newParkingSpace.setGarageKey(existingFloor.getGarageKey());
                                newParkingSpace.setNumber(i);
                                newParkingSpace.setAvailable(true);
                                newParkingSpaces.add(newParkingSpace);
                            }
                        }
                    }
                    else {
                        for (int i = 1; i <= floor.getTotalSpaces(); i++) {
                            if (i > floor.getTotalSpaces()) {
                                parkingSpaceRepository.delete(existingParkingSpaceMap.get(i).getParkingSpaceKey());
                                numDeleted++;
                            }
                            else if (!existingParkingSpaceMap.containsKey(i)) {
                                ParkingSpace newParkingSpace = new ParkingSpace();
                                newParkingSpace.setParkingSpaceKey(UUID.randomUUID().toString());
                                newParkingSpace.setFloor(existingFloor.getNumber());
                                newParkingSpace.setGarageKey(existingFloor.getGarageKey());
                                newParkingSpace.setNumber(i);
                                newParkingSpace.setAvailable(true);
                                newParkingSpaces.add(newParkingSpace);
                            }
                        }
                    }

                    if (numDeleted > 0) {
                        System.out.println("Deleted " + numDeleted + " ParkingSpaces with garageKey " + existingFloor.getGarageKey() + " for floor " + existingFloor.getNumber());
                    }
                    if (!newParkingSpaces.isEmpty()) {
                        parkingSpaceRepository.save(newParkingSpaces);
                        System.out.println("Saved " + newParkingSpaces.size() + " ParkingSpaces with garageKey " + floor.getGarageKey() + " for floor " + floor.getNumber());
                    }
                }

                floorRepository.save(existingFloor);
                System.out.println("Updated Floor: " + existingFloor);

                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/updateavailability")
    public ResponseEntity<?> updateAvailability(@RequestBody Floor floor) {
        // TODO Validate Floor object prior to saving
        System.out.println(floor);
        floorRepository.save(floor);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{floorKey}")
    public ResponseEntity<?> delete(@PathVariable String floorKey) {
        // TODO Validate prior to deleting
        floorRepository.delete(floorKey);
        System.out.println("Deleted Floor with floorKey: " + floorKey);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clean")
    public ResponseEntity<?> clean() {
        Predicate predicate = Predicates.or(
                Predicates.equal("garageKey", null),
                Predicates.equal("floor", null)
        );
        floorRepository.deleteByPredicate(predicate);
        return ResponseEntity.ok().build();
    }

    /**
     * Simulator will use this to get a list of all floors. The client needs to provide the unique subscription key
     * that matches.
     *
     * @param subscriptionKey String
     * @return
     */
    @GetMapping("/floors/{subscriptionKey}")
    public List<Floor> getCollection(@PathVariable String subscriptionKey) {

        if(subscriptionKey.equals("2093af49-30d2-4ba3-873b-29970e012656")) {
            ArrayList<Floor> floorList = new ArrayList<> (floorRepository.findAll());
            return floorList;
        }
        else {
            return null;
        }
    }
}
