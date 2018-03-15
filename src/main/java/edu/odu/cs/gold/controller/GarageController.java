package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * This controller provides methods for returning html pages that allow users
 * to view Garage details
 */
@Controller
@RequestMapping("/garage")
public class GarageController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private GarageService garageService;

    public GarageController(GarageRepository garageRepository,
                            FloorRepository floorRepository,
                            ParkingSpaceRepository parkingSpaceRepository,
                            FloorStatisticRepository floorStatisticRepository,
                            GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.floorStatisticRepository = floorStatisticRepository;
        this.garageService = garageService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        model.addAttribute("garages", garages);
        return "garage/index";
    }

    @GetMapping("/details/{garageKey}")
    public String details(@PathVariable String garageKey, Model model) {

        // TODO - For testing purposes only. Remove once testing is finished.
        garageService.refresh(garageKey);

        // Retrieve Garage
        Garage garage = garageRepository.findByKey(garageKey);

        // Retrieve Floors
        Predicate predicate = Predicates.equal("garageKey", garageKey);
        List<Floor> floors = floorRepository.findByPredicate(predicate);

        // Sort Floors by Number
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garage", garage);
        model.addAttribute("floors", floors);

        return "garage/details";
    }
}
