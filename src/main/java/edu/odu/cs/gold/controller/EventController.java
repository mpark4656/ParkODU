package edu.odu.cs.gold.controller;

import edu.odu.cs.gold.model.Event;
import edu.odu.cs.gold.model.User;
import edu.odu.cs.gold.repository.EventRepository;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.security.AuthenticatedUser;
import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

    public EventController(EventRepository eventRepository,
                           UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping({"","/","/index"})
    public String index(Model model) {
        List<Event> events = new ArrayList<>(eventRepository.findAll());
        events.sort(Comparator.comparing(Event::getEventUpdatedDateTime));
        model.addAttribute("events", events);
        return "events/index";
    }

    @GetMapping("/notification")
    public String notify(Model model) {
        List<Event> allEvents = new ArrayList<>(eventRepository.findAll());
        if(allEvents != null) {
            try {
                AuthenticatedUser authenticatedUser =
                        (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String userKey = authenticatedUser.getUser().getUserKey();
                User user = userRepository.findByKey(userKey);
                model.addAttribute("currentUserKey", user.getUserKey());

                int newEventCount = 0;
                for(Event event : allEvents) {
                    DateTime eventUpdatedDate = DateTime.parse(event.getEventUpdatedDateTime());

                    DateTime userLastNotificationViewedDate
                            = DateTime.parse(user.getLastNotificationViewedDate());
                    if (eventUpdatedDate.compareTo(userLastNotificationViewedDate) > 0) {
                        newEventCount++;
                    }

                }
                model.addAttribute("newEventCount", newEventCount);
            } catch(Exception e) {
                model.addAttribute("newEventCount", allEvents.size());
            }
            allEvents.sort(Comparator.comparing(Event::getEventStartTimeDateTime));
            model.addAttribute("allEvents", allEvents);
        }

        return "events/notification";
    }
}
