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

    public MapsController(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        return "navigate/index";
    }

    @GetMapping("/navigate")
    public String navigate(Model model,
                             @RequestParam("latitude") Double latitude,
                             @RequestParam("longitude") Double longitude,
                             @RequestParam("destination") String destinationGarageKey) {

        Garage garage = garageRepository.findByKey(destinationGarageKey);
        Location startingLocation = new Location(latitude,longitude);

        model.addAttribute("startingLocation", startingLocation);
        model.addAttribute("destination", garage.getLocation());
        model.addAttribute("travelMode", TravelMode.DRIVING.toString());

        return "maps/navigate/index";
    }

}
