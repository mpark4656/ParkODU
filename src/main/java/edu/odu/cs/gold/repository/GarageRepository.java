package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Garage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GarageRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public GarageRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
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

    public void save(Garage garage) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(garage.getGarageKey(), garage);
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public long getCollectionSize() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.getLocalMapStats().getOwnedEntryCount();
    }
}
