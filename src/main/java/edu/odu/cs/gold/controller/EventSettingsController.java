package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("settings/events")
public class EventSettingsController {

    private EventRepository eventRepository;

    public EventSettingsController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Event> events = new ArrayList<>(eventRepository.findAll());
        events.sort(Comparator.comparing(Event::getEventName));
        model.addAttribute("events", events);

        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/events/index";
    }

    /**
     * Method to return the settings/garage/create.html template page
     * @param model Model
     * @return String "settings/garage/create"
     */
    @GetMapping("/create")
    public String create(Model model) {
        Event event = new Event();
        model.addAttribute("event", event);
        return "settings/event/create";
    }

}
