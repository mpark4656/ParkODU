package edu.odu.cs.gold.controller;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.EventRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("settings/events")
public class EventSettingsController {

    private EventRepository eventRepository;
    private GarageRepository garageRepository;
    private BuildingRepository buildingRepository;

    public EventSettingsController(EventRepository eventRepository,
                                   GarageRepository garageRepository,
                                   BuildingRepository buildingRepository) {
        this.eventRepository = eventRepository;
        this.garageRepository = garageRepository;
        this.buildingRepository = buildingRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(@RequestParam(value = "successMessage", required = false) String successMessage,
                        @RequestParam(value = "infoMessage", required = false) String infoMessage,
                        @RequestParam(value = "warningMessage", required = false) String warningMessage,
                        @RequestParam(value = "dangerMessage", required = false) String dangerMessage,
                        Model model) {

        List<Event> events = new ArrayList<>(eventRepository.findAll());
        if(events != null && !events.isEmpty()) {
            events.sort(Comparator.comparing(Event::getEventName));
            model.addAttribute("events", events);
        }
        // Alerts
        if (successMessage != null) { model.addAttribute("successMessage", successMessage); }
        if (infoMessage != null) { model.addAttribute("infoMessage", infoMessage); }
        if (warningMessage != null) { model.addAttribute("warningMessage", warningMessage); }
        if (dangerMessage != null) { model.addAttribute("dangerMessage", dangerMessage); }

        return "settings/events/index";
    }

    /**
     * Method to return the settings/events/create.html template page
     * @param model Model
     * @return String "settings/events/create"
     */
    @GetMapping("/create")
    public String create(Model model) {
        Event event = new Event();
        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        List<Building> buildings = new ArrayList<>(buildingRepository.findAll());
        List<String> locations = new ArrayList<>();

        for(Garage garage: garages) {
            locations.add(garage.getName());
        }

        for(Building building : buildings) {
            locations.add(building.getName());
        }

        model.addAttribute("locations", locations);
        model.addAttribute("event", event);

        return "settings/events/create";
    }

    @PostMapping("/create")
    public String create(Event event,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;
        try {
            Predicate predicate = Predicates.or(
                    Predicates.equal("eventKey", event.getEventKey()),
                    Predicates.equal("eventName", event.getEventName())
            );
            int existingCount = eventRepository.countByPredicate(predicate);
            if (existingCount == 0) {
                eventRepository.save(event);
                isSuccessful = true;
            }
            else {
                isDuplicate = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The Event " + event.getEventName() + " was successfully created.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Event with the name " + event.getEventName() + " already exists.");
            model.addAttribute("event", event);
            return "settings/events/create";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to create a Event.");
        }
        return "redirect:/settings/events/index";
    }

    @GetMapping("/edit/{eventKey}")
    public String edit(@PathVariable("eventKey") String eventKey,
                       Model model) {
        Event event = eventRepository.findByKey(eventKey);
        List<Garage> garages = new ArrayList<>(garageRepository.findAll());
        List<Building> buildings = new ArrayList<>(buildingRepository.findAll());
        List<String> locations = new ArrayList<>();
        List<String> locationsAffected;
        for(Garage garage: garages) {
            locations.add(garage.getName());
        }

        for(Building building : buildings) {
            locations.add(building.getName());
        }

        model.addAttribute("locations", locations);
        model.addAttribute("event", event);
        return "settings/events/edit";
    }

    @PostMapping("/edit")
    public String edit(Event event,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        boolean isDuplicate = false;

        try {
            Predicate predicate = Predicates.and(
                    Predicates.notEqual("eventKey", event.getEventKey()),
                    Predicates.equal("eventName", event.getEventName())
            );
            int duplicateCount = eventRepository.countByPredicate(predicate);
            if (duplicateCount == 0) {
                Event existingEvent = eventRepository.findByKey(event.getEventKey());
                existingEvent.setEventUpdatedDateTime(DateTime.now().toString());
                existingEvent.setEventName(event.getEventName());
                existingEvent.setEventMessage(event.getEventMessage());
                existingEvent.setEventUpdatedDateTime(DateTime.now().toString());
                existingEvent.setEventStartDateTime(event.getEventStartDateTime());
                existingEvent.setEventEndDateTime(event.getEventEndDateTime());
                existingEvent.setLocationsAffected(event.getLocationsAffected());
                existingEvent.setEventTags(event.getEventTags());
                eventRepository.save(existingEvent);
                isSuccessful = true;
            }
            else {
                isDuplicate = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The Event " + event.getEventName() + " was successfully updated.");
        }
        else if (isDuplicate) {
            model.addAttribute("dangerMessage", "A Event with the name " + event.getEventName() + " already exists.");
            model.addAttribute("event", event);
            return "settings/events/edit";
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to update a Event.");
        }
        return "redirect:/settings/events/index";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("eventKey") String eventKey,
                         RedirectAttributes redirectAttributes) {
        boolean isSuccessful = false;
        Event event = null;
        try {
            event = eventRepository.findByKey(eventKey);
            if (event != null) {
                Predicate predicate = Predicates.equal("eventKey", eventKey);

                eventRepository.delete(eventKey);
                isSuccessful = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Alerts
        if (isSuccessful) {
            redirectAttributes.addAttribute("successMessage", "The Event " + event.getEventName() + " was successfully deleted.");
        }
        else {
            redirectAttributes.addAttribute("dangerMessage", "An error occurred when attempting to delete a Event.");
        }
        return "redirect:/settings/events/index";
    }

}
