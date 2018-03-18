package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.Location;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GoogleMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/maps")
public class MapsController {

    private GarageRepository garageRepository;
    private BuildingRepository buildingRepository;
    private GoogleMapService googleMapService;

    public MapsController(GarageRepository garageRepository, BuildingRepository buildingRepository, GoogleMapService googleMapService) {
        this.garageRepository = garageRepository;
        this.buildingRepository = buildingRepository;
        this.googleMapService = googleMapService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {

        return "settings/garage/index";
    }

    @GetMapping("/navigate")
    public String navigate() {

        return "/navigate";
    }
}
