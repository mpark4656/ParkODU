package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This controller provides methods for returning html pages that allow
 * admins to configure garages.
 *
 * For actual manipulation of Garage models such as update, add, and delete,
 * see GarageRestController.java
 */
@Controller
@RequestMapping("/settings/garage")
public class GarageSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private GarageService garageService;

    public GarageSettingsController(GarageRepository garageRepository,
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

    /**
     * This method returns "settings/garage/index" with a collection of all garage objects
     * added to the model.
     *
     * Usage in settings/garage/index.html to access the collection of all garages
     * ${garages}
     *
     * @param model Model
     * @return String "settings/garage/index"
     */
    @GetMapping({"","/","/index"})
    public String index(Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        model.addAttribute("garages", garages);

        return "settings/garage/index";
    }
}
