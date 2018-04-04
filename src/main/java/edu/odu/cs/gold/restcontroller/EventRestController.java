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
            event.setEventUpdatedDateTime(DateTime.now().toString());
            event.setEventName(event.getEventName());
            event.setEventMessage(event.getEventMessage());
            event.setEventStartDateTime(event.getEventStartDateTime());
            event.setEventEndDateTime(event.getEventEndDateTime());
            event.setEventTags(event.getEventTags());
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
            if (event.getEventUpdatedDateTime() != null) {
                existingEvent.setEventUpdatedDateTime(DateTime.now().toString());
            }
            if (event.getLocationsAffected() != null) {
                existingEvent.setLocationsAffected(event.getLocationsAffected());
            }
            if (event.getEventStartDateTime() != null) {
                existingEvent.setEventStartDateTime(event.getEventStartDateTime());
            }

            if (event.getEventEndDateTime() != null) {
                existingEvent.setEventEndDateTime(event.getEventEndDateTime());
            }

            if(event.getEventTags() != null) {
                existingEvent.setEventTags(event.getEventTags());
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
