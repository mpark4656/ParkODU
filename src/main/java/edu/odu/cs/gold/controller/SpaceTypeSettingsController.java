package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/settings/space_type")
public class SpaceTypeSettingsController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private PermitTypeRepository permitTypeRepository;
    private SpaceTypeRepository spaceTypeRepository;
    private GarageService garageService;

    /**
     * Constructor
     * @param garageRepository
     * @param floorRepository
     * @param parkingSpaceRepository
     * @param permitTypeRepository
     * @param spaceTypeRepository
     * @param garageService
     */
    public SpaceTypeSettingsController(GarageRepository garageRepository,
                                        FloorRepository floorRepository,
                                        ParkingSpaceRepository parkingSpaceRepository,
                                        PermitTypeRepository permitTypeRepository,
                                        SpaceTypeRepository spaceTypeRepository,
                                        GarageService garageService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.permitTypeRepository = permitTypeRepository;
        this.spaceTypeRepository = spaceTypeRepository;
        this.garageService = garageService;
    }

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {

        List<SpaceType> spaceTypes = new ArrayList<>(spaceTypeRepository.findAll());

        model.addAttribute("spaceTypes", spaceTypes);

        return "settings/space_type/index";
    }

}