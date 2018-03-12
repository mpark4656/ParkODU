package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.repository.TravelDistanceDurationRepository;
import edu.odu.cs.gold.service.GoogleMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private GarageRepository garageRepository;
    private BuildingRepository buildingRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private TravelDistanceDurationRepository travelDistanceDurationRepository;
    private GoogleMapService googleMapService;

    public AnalyticsController(GarageRepository garageRepository,
                               BuildingRepository buildingRepository,
                               ParkingSpaceRepository parkingSpaceRepository,
                               TravelDistanceDurationRepository travelDistanceDurationRepository,
                               GoogleMapService googleMapService) {
        this.garageRepository = garageRepository;
        this.buildingRepository = buildingRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.travelDistanceDurationRepository = travelDistanceDurationRepository;
        this.googleMapService = googleMapService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        List<Building> buildings = new ArrayList<>(buildingRepository.findAll());
        buildings.sort(Comparator.comparing(Building::getName));
        model.addAttribute("buildings", buildings);
        return "analytics/index";
    }

    @GetMapping("/locate")
    public String locate(Model model) {
        List<Building> buildings = new ArrayList<>(buildingRepository.findAll());
        buildings.sort(Comparator.comparing(Building::getName));
        List<String> permitTypes = new ArrayList<>();
        permitTypes.add("Commuter");
        permitTypes.add("Faculty");
        permitTypes.add("Metered");
        permitTypes.add("Quad Resident");
        model.addAttribute("buildings", buildings);

        model.addAttribute("permitTypes", permitTypes);
        return "analytics/locate";
    }

    @PostMapping("/locate")
    public String locate(@RequestParam(name = "startingAddress", required = false) String startingAddress,
                         @RequestParam(name = "latitude", required = false) Double startingLatitude,
                         @RequestParam(name = "longitude", required = false) Double startingLongitude,
                         @RequestParam(name = "permitTypes", required = false) List<String> permitTypes,
                         @RequestParam(name = "destinationBuildingId", required = false) String destinationBuildingId,
                         @RequestParam(name = "minSpaces", required = false) Integer minSpaces,
                         Model model) {
        Location startingLocation = new Location(startingAddress, startingLatitude, startingLongitude);
        Building destinationBuilding = buildingRepository.findByKey(destinationBuildingId);

        Predicate permitPredicate = null;
        if (permitTypes != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (String permitType : permitTypes) {
                predicates.add(Predicates.equal("permitType", permitType));
            }
            permitPredicate = Predicates.or(predicates.toArray(new Predicate[0]));
        }

        List<Recommendation> recommendations = new ArrayList<>();

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());

        for (Garage garage : garages) {
            int availabilityCount = 0;
            int totalCount = 0;
            if (permitPredicate != null) {

                // Availability Count
                Predicate availabilityCountPredicate = Predicates.and(
                        Predicates.equal("garageKey", garage.getGarageKey()),
                        Predicates.equal("available", true),
                        permitPredicate
                );
                availabilityCount = parkingSpaceRepository.countByPredicate(availabilityCountPredicate);

                // Total Count
                Predicate totalCountPredicate = Predicates.and(
                    Predicates.equal("garageKey", garage.getGarageKey()),
                    permitPredicate
                );
                totalCount = parkingSpaceRepository.countByPredicate(totalCountPredicate);
            }
            else {
                availabilityCount = garage.getAvailableSpaces();
            }
            if (minSpaces <= availabilityCount) {
                Recommendation recommendation = new Recommendation();
                recommendation.setStartingAddress(startingAddress);
                recommendation.setGarage(garage);
                recommendation.setDestinationBuilding(destinationBuilding);

                recommendation.setAvailabilityCount(availabilityCount);
                recommendation.setTotalCount(totalCount);

                DistanceDuration startingAddressToGarage = googleMapService.getDistanceDuration(startingLocation, garage.getLocation(), GoogleMapService.TravelMode.DRIVING);
                recommendation.setStartingAddressToGarage(startingAddressToGarage);

                DistanceDuration garageToDestinationBuilding = googleMapService.getDistanceDuration(garage.getLocation(), destinationBuilding.getLocation(), GoogleMapService.TravelMode.WALKING);
                recommendation.setGarageToDestinationBuilding(garageToDestinationBuilding);

                // Set Total Distance
                recommendation.setTotalDistanceValue(recommendation.getStartingAddressToGarageDistanceValue() + recommendation.getGarageToDestinationBuildingDistanceValue());

                // Set Total Duration
                recommendation.setTotalDurationValue(recommendation.getStartingAddressToGarageDurationValue() + recommendation.getGarageToDestinationBuildingDurationValue());

                recommendations.add(recommendation);
            }
        }


        // Default sort by closest Garage to Destination Building
        recommendations.sort(Comparator.comparing(Recommendation::getGarageToDestinationBuildingDistanceValue));

        model.addAttribute("startingAddress", startingAddress);
        model.addAttribute("permitTypes", permitTypes);
        model.addAttribute("destinationBuilding", destinationBuilding);
        model.addAttribute("recommendations", recommendations);

        return "analytics/recommendation";
    }

    @GetMapping({"/building/{buildingKey}"})
    public String building(@PathVariable String buildingKey, Model model) {
        Building building = buildingRepository.findByKey(buildingKey);
        Map<String, Garage> garageMap = garageRepository.findAllMap();

        Predicate predicate = Predicates.equal("buildingKey", buildingKey);
        List<TravelDistanceDuration> travelDistanceDurations = travelDistanceDurationRepository.findByPredicate(predicate);
        travelDistanceDurations.sort(Comparator.comparing(TravelDistanceDuration::getDurationValue));
        for (TravelDistanceDuration travelDistanceDuration : travelDistanceDurations) {
            travelDistanceDuration.setGarageName(garageMap.get(travelDistanceDuration.getGarageKey()).getName());
            travelDistanceDuration.setAvailableSpaces(garageMap.get(travelDistanceDuration.getGarageKey()).getAvailableSpaces());
            travelDistanceDuration.setTotalSpaces(garageMap.get(travelDistanceDuration.getGarageKey()).getTotalSpaces());
            travelDistanceDuration.setCapacity(garageMap.get(travelDistanceDuration.getGarageKey()).getCapacity());
        }

        model.addAttribute("building", building);
        model.addAttribute("travelDistanceDurations", travelDistanceDurations);
        return "analytics/building";
    }
}
