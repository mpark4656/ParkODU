package edu.odu.cs.gold.controller;



import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping ("/settings/floor_statistics")


public class FloorStatisticSettingsController   {

    private GarageRepository garageRepository;
    private FloorRepository floorRepository;
    private FloorStatisticRepository floorStatisticRepository;

    public FloorStatisticSettingsController (GarageRepository garageRepository,
                                            FloorRepository floorRepository,
                                            FloorStatisticRepository floorStatisticRepository)
    {
        this.garageRepository = garageRepository;
        this.floorRepository = floorRepository;
        this.floorStatisticRepository =floorStatisticRepository;

    }

    @GetMapping({"", "/", "/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));

        List<Floor> floors = new ArrayList<>(floorRepository.findAll());
        floors.sort(Comparator.comparing(Floor::getNumber));


        model.addAttribute("garages", garages);
        model.addAttribute("floors", floors);

        // Alerts
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (infoMessage != null) {
            model.addAttribute("infoMessage", infoMessage);
        }
        if (warningMessage != null) {
            model.addAttribute("warningMessage", warningMessage);
        }
        if (dangerMessage != null) {
            model.addAttribute("dangerMessage", dangerMessage);
        }


        return "settings/floor_statistics/index";


    }
@GetMapping ("/floor/{floorKey}")
public String floor (@PathVariable("floorKey") String floorKey,
                     Model model){


        Floor floor = floorRepository.findByKey(floorKey);
    Predicate predicate = Predicates.equal("floorKey",floorKey);

        List<FloorStatistic> floorStatistics = (floorStatisticRepository.findByPredicate(predicate));
        Garage garage = garageRepository.findByKey(floor.getGarageKey());

        model.addAttribute("floor", floor);
        model.addAttribute("garage", garage);
        model.addAttribute("floorStatistics", floorStatistics);

    return "settings/floor_statistics/floor";


    }
}



