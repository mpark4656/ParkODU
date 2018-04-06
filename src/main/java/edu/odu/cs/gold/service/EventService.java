package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import org.springframework.stereotype.Service;

import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.*;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void save(Event entity) {
        eventRepository.save(entity);
    }
}
