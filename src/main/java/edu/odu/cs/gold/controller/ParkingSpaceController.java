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
@RequestMapping("/parking_space")
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

    // Modified 3/13/2018 @9:15PM
    // Moved set_availability PostMapping for parking_space to ParkingSpaceRestController.java

    // ParkingSpace does not yet have parking_space/detail.html or parking_space/index.html like Garage and Floor
    // This class will remain as a placeholder for parking_space in case we will decide to add parking_space/detail.html
    // to show individual space information such as sensor status.
}

