package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Event;

import java.util.*;

public class EventRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public EventRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(Event entity) {
        return entity.getEventKey();
    }

    public List<Event> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values());
    }

    public Map findAllMap() {
        IMap map = hazelcastInstance.getMap(collectionName);
        Map<String, Object> entityMap = new HashMap<>();
        for (Object value : map.values()) {
            entityMap.put(getId((Event)value), value);
        }
        return entityMap;
    }

    public Event findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (Event)map.get(key);
    }

    public List<Event> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<Event> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(Event entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<Event> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (Event entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<Event> entities = this.findByPredicate(predicate);
        for (Event entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }
}
