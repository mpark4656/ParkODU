package edu.odu.cs.gold.restcontroller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rest/floorstatistic/")
public class FloorStatisticRestController {

    private FloorStatisticRepository floorStatisticRepository;
    private FloorRepository floorRepository;

    public FloorStatisticRestController(FloorStatisticRepository floorStatisticRepository,
                                        FloorRepository floorRepository) {
        this.floorStatisticRepository = floorStatisticRepository;
        this.floorRepository = floorRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody FloorStatistic floorStatistic) {
        if (floorStatistic.getFloorKey() != null && floorStatistic.getCapacity() != null && floorStatistic.getTimestamp() != null) {
            Floor existingFloor = floorRepository.findByKey(floorStatistic.getFloorKey());
            if (existingFloor != null) {
                floorStatistic.setFloorStatisticKey(UUID.randomUUID().toString());
                floorStatisticRepository.save(floorStatistic);
                System.out.println("Saved FloorStatistic: " + floorStatistic);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody FloorStatistic floorStatistic) {
        if (floorStatistic.getFloorStatisticKey() != null) {
            FloorStatistic existingFloorStatistic = floorStatisticRepository.findByKey(floorStatistic.getFloorStatisticKey());
            if (existingFloorStatistic != null) {
                floorStatisticRepository.delete(floorStatistic.getFloorStatisticKey());
                System.out.println("Deleted FloorStatistic with floorStatisticKey: " + floorStatistic.getFloorStatisticKey());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/deletebyfloorkey")
    public ResponseEntity<?> deleteByFloorKey(@RequestBody FloorStatistic floorStatistic) {
        if (floorStatistic.getFloorKey() != null) {
            Predicate predicate = Predicates.equal("floorKey", floorStatistic.getFloorKey());
            int numDeleted = floorStatisticRepository.deleteByPredicate(predicate);
            System.out.println("Deleted " + numDeleted + " FloorStatitics with floorKey: " + floorStatistic.getFloorKey());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
