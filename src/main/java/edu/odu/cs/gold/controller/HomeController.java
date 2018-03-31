package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.repository.EventRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private GarageRepository garageRepository;
    private EventRepository eventRepository;

    public HomeController(GarageRepository garageRepository,
                          EventRepository eventRepository) {
        this.garageRepository = garageRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model,
                        @RequestParam(value = "error", required = false) String dangerMessage) {

        List<Event> events = new ArrayList<>(eventRepository.findAll());
        if(events != null) {
            //------------- THIS IS A TEST! REMOVE AFTER TESTING --------------
            Event event1 = new Event();
            event1.setEventDateTime("3-28-2018 4:30PM");
            event1.setEventName("This is a test event!");
            event1.setEventMessage("This is a test event message!");

            Event event2 = new Event();
            event2.setEventDateTime("3-28-2018 5:00PM");
            event2.setEventName("This is a test event2!");
            event2.setEventMessage("This is a test event message2!");

            events.add(event1);
            events.add(event2);
            //------------------------------------------------------------------

            //events.sort(Comparator.comparing(Event::getEventDateTime));
            model.addAttribute("events",events);
        }

        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        garages.sort(Comparator.comparing(Garage::getName));
        StringBuilder currentAvailabilityDataString = new StringBuilder();
        for (Garage garage : garages) {
            currentAvailabilityDataString.append(garage.getAvailableSpaces() + ",");
        }
        model.addAttribute("currentAvailabilityDataString", currentAvailabilityDataString.toString());

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
