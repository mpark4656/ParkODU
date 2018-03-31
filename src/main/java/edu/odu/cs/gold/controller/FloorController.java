package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import edu.odu.cs.gold.service.FloorStatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/floor")
public class FloorController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private ParkingSpaceRepository parkingSpaceRepository;
    private FloorStatisticService floorStatisticService;

    public FloorController(GarageRepository garageRepository,
                           FloorRepository floorRepository,
                           ParkingSpaceRepository parkingSpaceRepository,
                           FloorStatisticService floorStatisticService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.floorStatisticService = floorStatisticService;
    }

    @GetMapping("/details/{floorKey}")
    public String details(@PathVariable("floorKey") String floorKey,
                          Model model) {
        Floor floor = floorRepository.findByKey(floorKey);
        Garage garage = garageRepository.findByKey(floor.getGarageKey());

        model.addAttribute("garage", garage);
        model.addAttribute("floor", floor);
        model.addAttribute("currentTime", new Date());

        /** Space Availability Floor Plan **/

        // Retrieve ParkingSpaces
        Predicate predicate = Predicates.and(
                Predicates.equal("garageKey", garage.getGarageKey()),
                Predicates.equal("floor", floor.getNumber())
        );
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findByPredicate(predicate);

        // Sort ParkingSpaces by Number
        parkingSpaces.sort(Comparator.comparing(ParkingSpace::getNumber));

        StringBuilder availableParkingSpaces = new StringBuilder();
        for (ParkingSpace parkingSpace : parkingSpaces) {
            if (parkingSpace.isAvailable()) {
                availableParkingSpaces.append(parkingSpace.getNumber() + ",");
            }
        }

        model.addAttribute("parkingSpaces", parkingSpaces);
        model.addAttribute("availableParkingSpaces", availableParkingSpaces.toString());

        /** Average Capacity by Hour Chart **/

        // Retrieve FloorStatistics
        /*
        Predicate floorStatisticsPredicate = Predicates.equal("floorKey", floorKey);
        List<FloorStatistic> floorStatistics = floorStatisticRepository.findByPredicate(floorStatisticsPredicate);
        */
        List<FloorStatistic> floorStatistics = floorStatisticService.findAverageFloorCapacityByHour(floorKey);

        // Sort Floors by Number
        floorStatistics.sort(Comparator.comparing(FloorStatistic::getTimestamp));

        // Build Chart Data
        StringBuilder dataString = new StringBuilder();
        StringBuilder labelString = new StringBuilder();
        for (FloorStatistic floorStatistic : floorStatistics) {
            dataString.append(floorStatistic.getCapacity() + ",");

            Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York")); // creates a new calendar instance
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
        model.addAttribute("floorStatistics", floorStatistics);
        model.addAttribute("dataString", dataString.toString());
        model.addAttribute("labelString", labelString.toString());
        return "floor/details";
    }
}
