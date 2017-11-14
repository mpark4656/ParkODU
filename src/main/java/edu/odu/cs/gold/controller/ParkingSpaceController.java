package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/settings/parking_space")
public class ParkingSpaceController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private GarageService garageService;

    public ParkingSpaceController(GarageRepository garageRepository,
                                  FloorRepository floorRepository,
                                  ParkingSpaceRepository parkingSpaceRepository,
                                  GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.garageService = garageService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        List<Floor> floors = new ArrayList<>(floorRepository.findAll());
        floors.sort(Comparator.comparing(Floor::getNumber));

        model.addAttribute("garages", garages);
        model.addAttribute("floors", floors);

        return "settings/parking_space/index";
    }

    @GetMapping("floor/{floorKey}")
    public String floor(@PathVariable("floorKey") String floorKey,
                        Model model) {

        Floor floor = floorRepository.findByKey(floorKey);
        Garage garage = garageRepository.findByKey(floor.getGarageKey());

        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", floor.getGarageKey()),
                Predicates.equal("floor", floor.getNumber())
        );

        List<ParkingSpace> parkingSpaces = new ArrayList<>(parkingSpaceRepository.findByPredicate(predicate));
        parkingSpaces.sort(Comparator.comparing(ParkingSpace::getNumber));

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("parkingSpaces", parkingSpaces);

        return "settings/parking_space/floor";
    }

    @PostMapping("/set_availability")
    @ResponseBody
    public String setAvailability(@RequestParam("parkingSpaceKey") String parkingSpaceKey,
                                  @RequestParam("available") Boolean available) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByKey(parkingSpaceKey);
        parkingSpace.setAvailable(available);
        parkingSpace.setLastUpdated(new Date());
        parkingSpaceRepository.save(parkingSpace);

        garageService.refresh(parkingSpace.getGarageKey());

        return parkingSpaceKey + "'s availability was set to " + available;
    }
}
