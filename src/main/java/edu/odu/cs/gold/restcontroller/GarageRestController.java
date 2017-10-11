package edu.odu.cs.gold.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/garage/")
public class GarageRestController {

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @RequestMapping("/total/{garageKey}")
    public String totalCount(@PathVariable String garageKey) {
        System.out.println("Called totalCount for garageKey: " + garageKey);
        Predicate predicate = Predicates.equal("garageKey", garageKey);
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Parking Spaces: " + count);
        return count + "";
    }

    @RequestMapping("/available/{garageKey}")
    public String availableCount(@PathVariable String garageKey) {
        System.out.println("Called availableCount for garageKey: " + garageKey);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("available", "true"));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Available Parking Spaces: " + count);
        return count + "";
    }

    @RequestMapping("/total/{garageKey}/floor/{floor}")
    public String totalCountByFloor(@PathVariable("garageKey") String garageKey,
                                    @PathVariable("floor") String floor) {
        System.out.println("Called totalCountByFloor for garageKey: " + garageKey + ", floor: " + floor);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("floor", floor));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Parking Spaces on Floor " + floor + ": " + count);
        return count + "";
    }

    @RequestMapping("/available/{garageKey}/floor/{floor}")
    public String availableCountByFloor(@PathVariable("garageKey") String garageKey,
                                    @PathVariable("floor") String floor) {
        System.out.println("Called availableCountByFloor for garageKey: " + garageKey + ", floor: " + floor);
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("floor", floor),
                Predicates.equal("available", "true"));
        int count = parkingSpaceRepository.countByPredicate(predicate);
        System.out.println("Total Available Parking Spaces on Floor " + floor + ": " + count);
        return count + "";
    }

    @RequestMapping("/get/{garageKey}")
    public String get(@PathVariable String garageKey) throws JsonProcessingException {
        Garage garage = garageRepository.findByKey(garageKey);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(garage);
    }
}
