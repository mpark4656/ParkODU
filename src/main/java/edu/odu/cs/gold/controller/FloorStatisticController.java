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

@Controller
@RequestMapping("/floorstatistics")
public class FloorStatisticController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticRepository floorStatisticRepository;
    private GarageService garageService;

    public FloorStatisticController(GarageRepository garageRepository,
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
        model.addAttribute("garage", garages);
        return "garage/index";
    }

    @GetMapping("/details/{floorKey}")
    public String details(@PathVariable String floorKey, Model model) {

        Floor floor = floorRepository.findByKey(floorKey);
        Garage garage = garageRepository.findByKey(floor.getGarageKey());

        // Retrieve FloorStatistics
        Predicate predicate = Predicates.equal("floorKey", floorKey);
        List<FloorStatistic> floorStatistics = floorStatisticRepository.findByPredicate(predicate);

        // Sort Floors by Number
        floorStatistics.sort(Comparator.comparing(FloorStatistic::getTimestamp));

        // Build Chart Data
        StringBuilder dataString = new StringBuilder();
        StringBuilder labelString = new StringBuilder();
        for (FloorStatistic floorStatistic : floorStatistics) {
            dataString.append(floorStatistic.getCapacity() + ",");

            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(floorStatistic.getTimestamp());   // assigns calendar to given date

            if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                labelString.append("12am,");
            }
            else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                labelString.append("12pm,");
            }
            else if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                labelString.append(calendar.get(Calendar.HOUR_OF_DAY) + "am,");
            }
            else {
                labelString.append((calendar.get(Calendar.HOUR_OF_DAY) - 12) + "pm,");
            }
        }

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("floorStatistics", floorStatistics);
        model.addAttribute("dataString", dataString.toString());
        model.addAttribute("labelString", labelString.toString());

        return "floorstatistic/details";
    }

    @GetMapping("/details/{garageKey}/floor/{floorKey}")
    public String floorDetails(@PathVariable String garageKey, @PathVariable String floorKey, Model model) {
        Floor floor = floorRepository.findByKey(floorKey);
        Garage garage = garageRepository.findByKey(floor.getGarageKey());
        // Retrieve ParkingSpaces
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garageKey),
                Predicates.equal("floor", floor.getNumber())
        );
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);
        System.err.println(parkingSpaces.size());

        // Sort ParkingSpaces by Number
        parkingSpaces.sort(Comparator.comparing(ParkingSpace::getNumber));

        StringBuilder availableParkingSpaces = new StringBuilder();
        for (ParkingSpace parkingSpace : parkingSpaces) {
            if (parkingSpace.isAvailable()) {
                availableParkingSpaces.append(parkingSpace.getNumber() + ",");
            }
        }

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("parkingSpaces", parkingSpaces);
        model.addAttribute("availableParkingSpaces", availableParkingSpaces.toString());
        return "floor/details";
    }
}
