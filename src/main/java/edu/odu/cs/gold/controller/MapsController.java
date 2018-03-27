package edu.odu.cs.gold.controller;

import com.google.gson.JsonArray;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.service.GarageService;
import edu.odu.cs.gold.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hazelcast.query.Predicates;
import com.hazelcast.query.Predicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/maps")
public class MapsController {

    private GarageRepository garageRepository;
    private BuildingRepository buildingRepository;
    private RecommendationRepository recommendationRepository;

    public MapsController(GarageRepository garageRepository,
                          BuildingRepository buildingRepository,
                          RecommendationRepository recommendationRepository) {

        this.garageRepository = garageRepository;
        this.buildingRepository = buildingRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        return "navigate/index";
    }

    @GetMapping("/navigate")
    public String directions(Model model,
                             @RequestParam("latitude") Double latitude,
                             @RequestParam("longitude") Double longitude,
                             @RequestParam("destination") String destination) {

        Garage garage = garageRepository.findByKey(destination);
        GoogleMapService mapService = new GoogleMapService();
        Location startingLocation = new Location();
        startingLocation.setLatitude(latitude);
        startingLocation.setLongitude(longitude);
        String directions = mapService.buildDirectionsWithLatLng(startingLocation,garage.getLocation());

        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("destination", garage.getLocation());
        model.addAttribute("travelMode", TravelMode.DRIVING.toString());
        model.addAttribute("directions", directions);

        System.out.println(directions);

        return "maps/navigate/index";
    }

}
