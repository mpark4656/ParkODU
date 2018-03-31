package edu.odu.cs.gold.restcontroller;

import com.hazelcast.spi.EventService;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/rest/events/")
public class EventRestController {

    private EventRepository eventRepository;

    public EventRestController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Event event) {
        if (event.getEventKey() != null)  {
            event.setEventKey(UUID.randomUUID().toString());
            event.setEventDateTime(DateTime.now().toString());
            event.setEventName(event.getEventName());
            event.setEventMessage(event.getEventMessage());
            event.setScheduledDateTime(event.getScheduledDateTime());
            event.setEventTags();
            eventRepository.save(event);
            System.out.println("Saved event: " + event);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> edit(@RequestBody Event event) {
        Event existingEvent = eventRepository.findByKey(event.getEventKey());
        if (existingEvent != null) {
            // Upsert - if null, don't update the field
            if (event.getEventName() != null) {
                existingEvent.setEventName(event.getEventName());
            }
            if (event.getEventMessage() != null) {
                existingEvent.setEventMessage(event.getEventMessage());
            }
            if (event.getEventDateTime() != null) {
                existingEvent.setEventDateTime(DateTime.now().toString());
            }
            if (event.getLocationsEffected() != null) {
                existingEvent.setLocationsEffected(event.getLocationsEffected());
            }
            if (event.getScheduledDateTime() != null) {
                existingEvent.setScheduledDateTime(event.getScheduledDateTime());
            }
            if(event.getEventTags() != null) {
                // TODO
                existingEvent.setEventTags();
            }


            eventRepository.save(existingEvent);
            System.out.println("Updated Event: " + existingEvent);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Event event) {
        if (event.getEventKey() != null) {
            Event existingEvent = eventRepository.findByKey(event.getEventKey());
            if (existingEvent != null) {
                eventRepository.delete(event.getEventKey());
                System.out.println("Deleted Event with eventKey: " + event.getEventKey());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{eventKey}")
    public Event get(@PathVariable String eventKey) {
        Event event = eventRepository.findByKey(eventKey);
        return event;
    }
}
