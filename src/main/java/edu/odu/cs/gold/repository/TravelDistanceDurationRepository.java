package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Building;
import edu.odu.cs.gold.model.TravelDistanceDuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TravelDistanceDurationRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public TravelDistanceDurationRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(TravelDistanceDuration entity) {
        return entity.getTravelDistanceDurationKey();
    }

    public Collection<TravelDistanceDuration> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public TravelDistanceDuration findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (TravelDistanceDuration)map.get(key);
    }

    public List<TravelDistanceDuration> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<TravelDistanceDuration> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(TravelDistanceDuration entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<TravelDistanceDuration> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (TravelDistanceDuration entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<TravelDistanceDuration> entities = this.findByPredicate(predicate);
        for (TravelDistanceDuration entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
