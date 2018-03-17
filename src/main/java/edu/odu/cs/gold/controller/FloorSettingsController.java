package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This controller provides methods for returning html pages that allow
 * admins to configure floors.
 *
 * For actual manipulation of Floor models such as update, add, and delete,
 * see FloorRestController.java
 */
@Controller
@RequestMapping("/settings/floor")
public class FloorSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;

    public FloorSettingsController(GarageRepository garageRepository,
                           FloorRepository floorRepository,
                           ParkingSpaceRepository parkingSpaceRepository,
                           FloorStatisticRepository floorStatisticRepository) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.floorStatisticRepository = floorStatisticRepository;
    }

    /**
     * This method returns "settings/floor/index" with a collection of all garage objects
     * added to the model.
     *
     * Usage in settings/floor/index.html to access the collection of all garage
     * ${garage}

     * @param model Model
     * @return String "settings/floor/index"
     */
    @GetMapping({"","/","/index"})
    public String index(Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        model.addAttribute("garages", garages);

        return "settings/floor/index";
    }

    /**
     * This method returns "settings/floor/garage" with a Garage object and
     * a collection of Floor objects found in the garage.
     *
     * Uage in settings/floor/garage.html to access the collection of floors and the garage
     * ${garage}
     * ${floors}
     *
     * @param garageKey String unique identifier of the garage being queried
     * @param model Model
     * @return String "settings/floor/garage"
     */
    @GetMapping("garage/{garageKey}")
    public String floor(@PathVariable("garageKey") String garageKey,
                        Model model) {

        Garage garage = garageRepository.findByKey(garageKey);

        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garage.getGarageKey())
        );

        List<Floor> floors = new ArrayList<>(floorRepository.findByPredicate(predicate));
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garage", garage);
        model.addAttribute("floors", floors);

        return "settings/floor/garage";
    }

}
