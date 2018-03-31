package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        List<Event> events = new ArrayList<>(eventRepository.findAll());
        events.sort(Comparator.comparing(Event::getEventDateTime));
        model.addAttribute("events", events);
        return "events/index";
    }
}
