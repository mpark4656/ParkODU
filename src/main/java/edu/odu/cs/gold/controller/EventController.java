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

import java.util.*;

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
        try {
            List<Event> events = new ArrayList<>(eventRepository.findAll());
            events.sort(Comparator.comparing(Event::getEventStartTimeDateTime));
            model.addAttribute("events", events);

            User user = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                    && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                AuthenticatedUser authenticatedUser = (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String userKey = authenticatedUser.getUser().getUserKey();
                user = userRepository.findByKey(userKey);
                model.addAttribute("userKey", user.getUserKey());
            }

            Set<String> newEventKeys = new HashSet<>();
            for (Event event : events) {
                if (user != null && user.getLastNotificationViewedDate() != null) {
                    DateTime eventUpdatedDate = DateTime.parse(event.getEventUpdatedDateTime());
                    DateTime userLastViewedDate = DateTime.parse(user.getLastNotificationViewedDate());
                    if (eventUpdatedDate.toDate().getTime() > userLastViewedDate.toDate().getTime()) {
                        newEventKeys.add(event.getEventKey());
                    }
                }
            }
            model.addAttribute("newEventKeys", newEventKeys);
            model.addAttribute("newEventCount", newEventKeys.size());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "events/notification";
    }
}
