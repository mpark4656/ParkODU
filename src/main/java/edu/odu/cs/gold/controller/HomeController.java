package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import edu.odu.cs.gold.security.AuthenticatedUser;
import edu.odu.cs.gold.service.GarageStatisticService;
import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.*;
import java.text.SimpleDateFormat;

@Controller
public class HomeController {

    private GarageRepository garageRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private GarageStatistic garageStatistic;
    private FloorStatisticRepository floorStatisticRepository;
    private FloorRepository floorRepository;
    private GarageStatisticRepository garageStatisticRepository;
    private GarageStatisticService garageStatisticService;

    public HomeController(GarageRepository garageRepository,
                          EventRepository eventRepository,
                          UserRepository userRepository,
                          GarageStatisticRepository garageStatisticRepository,
                          GarageStatisticService garageStatisticService) {
        this.garageRepository = garageRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.garageStatisticRepository = garageStatisticRepository;
        this.garageStatisticService = garageStatisticService;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model,
                        @RequestParam(value = "error", required = false) String dangerMessage) {

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        StringBuilder currentAvailabilityDataString = new StringBuilder();

        List<List<Double>> datas = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        Date date = new Date();

        for (Garage garage : garages) {
            currentAvailabilityDataString.append(garage.getAvailableSpaces() + ",");

            List<GarageStatistic> garageStatistics = garageStatisticService.findGarageCapacityByDate(garage.getGarageKey(), date);

            StringBuilder labelString = new StringBuilder();
            List<Double> data = new ArrayList<>();
            for (GarageStatistic garageStatistic : garageStatistics) {
                data.add(garageStatistic.getCapacity());

                Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York")); // creates a new calendar instance
                calendar.setTime(garageStatistic.getTimestamp());   // assigns calendar to given date

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
            datas.add(data);
            labels.add(labelString.toString());
        }

        model.addAttribute("currentAvailabilityDataString", currentAvailabilityDataString.toString());
        model.addAttribute("datas", datas);
        model.addAttribute("labels", labels);

        // Alerts
        if (dangerMessage != null) {
            model.addAttribute("dangerMessage", dangerMessage);
        }
        return "home/index";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings/index";
    }

    @GetMapping("/login")
    public String login() {
        return "home/login";
    }

    // Login form with error
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "home/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}
