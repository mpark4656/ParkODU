package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.model.Garage;

import java.util.*;

public class GarageRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public GarageRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(Garage entity) {
        return entity.getGarageKey();
    }

    public List<Garage> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values());
    }

    public Map findAllMap() {
        IMap map = hazelcastInstance.getMap(collectionName);
        Map<String, Object> entityMap = new HashMap<>();
        for (Object value : map.values()) {
            entityMap.put(getId((Garage)value), value);
        }
        return entityMap;
    }

    public Garage findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (Garage)map.get(key);
    }

    public List<Garage> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<Garage> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(Garage entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<Garage> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (Garage entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<Garage> entities = this.findByPredicate(predicate);
        for (Garage entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
