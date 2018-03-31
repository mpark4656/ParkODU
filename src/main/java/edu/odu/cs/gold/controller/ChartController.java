package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.service.FloorStatisticService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequestMapping("/charts")
public class ChartController {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private FloorStatisticService floorStatisticService;

    public ChartController(GarageRepository garageRepository,
                           FloorRepository floorRepository,
                           FloorStatisticService floorStatisticService) {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.floorStatisticService = floorStatisticService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model){

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        model.addAttribute("garages", garages);
        return "charts/index";
    }

    @GetMapping("/chart/{garageKey}/{floorNumber}/{date}")
    public String chart(@PathVariable("garageKey") String garageKey,
                        @PathVariable("floorNumber") String floorNumber,
                        @PathVariable("date")
                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                            Date date,
                        Model model) {
        Predicate predicate = Predicates.and(
                Predicates.equal("number", floorNumber),
                Predicates.equal("garageKey", garageKey)
        );

        List<Floor> floors= floorRepository.findByPredicate(predicate);
        String floorKey = floors.get(0).getFloorKey();


        ArrayList<FloorStatistic> floorStatistics
                = floorStatisticService.findFloorCapacityByDate(floorKey, date);

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

        model.addAttribute("dataString", dataString.toString());
        model.addAttribute("labelString", labelString.toString());
        return "charts/chart";
    }
}

