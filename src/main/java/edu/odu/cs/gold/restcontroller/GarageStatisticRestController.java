package edu.odu.cs.gold.restcontroller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.GarageStatistic;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.GarageStatisticRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rest/garage_statistic/")
public class GarageStatisticRestController {

    private GarageStatisticRepository garageStatisticRepository;
    private GarageRepository garageRepository;

    public GarageStatisticRestController(GarageStatisticRepository garageStatisticRepository,
                                         GarageRepository garageRepository) {
        this.garageStatisticRepository = garageStatisticRepository;
        this.garageRepository = garageRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody GarageStatistic garageStatistic) {
        if (garageStatistic.getGarageKey() != null && garageStatistic.getCapacity() != null && garageStatistic.getTimestamp() != null) {
            Garage existingGarage = garageRepository.findByKey(garageStatistic.getGarageKey());
            if (existingGarage != null) {
                garageStatistic.setGarageStatisticKey(UUID.randomUUID().toString());
                garageStatisticRepository.save(garageStatistic);
                System.out.println("Saved GarageStatistic: " + garageStatistic);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody GarageStatistic garageStatistic) {
        if (garageStatistic.getGarageStatisticKey() != null) {
            GarageStatistic existingGarageStatistic = garageStatisticRepository.findByKey(garageStatistic.getGarageStatisticKey());
            if (existingGarageStatistic != null) {
                garageStatisticRepository.delete(garageStatistic.getGarageStatisticKey());
                System.out.println("Deleted GarageStatistic with garageStatisticKey: " + garageStatistic.getGarageStatisticKey());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/deleteby/{garageKey}")
    public ResponseEntity<?> deleteByGarageKey(@RequestBody GarageStatistic garageStatistic) {
        if (garageStatistic.getGarageKey() != null) {
            Predicate predicate = Predicates.equal("garageKey", garageStatistic.getGarageKey());
            int numDeleted = garageStatisticRepository.deleteByPredicate(predicate);
            System.out.println("Deleted " + numDeleted + " GarageStatitics with garageKey: " + garageStatistic.getGarageKey());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
