package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.EventNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class EventNotificationRepository {
    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public EventNotificationRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(EventNotification entity) {
        return entity.getEventKey();
    }

    public Collection<EventNotification> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public EventNotification findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (EventNotification) map.get(key);
    }

    public List<EventNotification> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<EventNotification> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(EventNotification entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<EventNotification> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (EventNotification entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<EventNotification> entities = this.findByPredicate(predicate);
        for (EventNotification entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }
}
