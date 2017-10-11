package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.ParkingSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParkingSpaceRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public ParkingSpaceRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public ParkingSpace findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (ParkingSpace)map.get(key);
    }

    public List<ParkingSpace> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<ParkingSpace> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(ParkingSpace entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(entity.getParkingSpaceKey(), entity);
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
