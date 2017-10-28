package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.ParkingSpace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FloorRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public FloorRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(Floor entity) {
        return entity.getFloorKey();
    }

    public Collection<Floor> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public Floor findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (Floor)map.get(key);
    }

    public List<Floor> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<Floor> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(Floor entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<Floor> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (Floor entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<Floor> entities = this.findByPredicate(predicate);
        for (Floor entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
