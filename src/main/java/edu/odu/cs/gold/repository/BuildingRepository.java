package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Building;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BuildingRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public BuildingRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(Building entity) {
        return entity.getBuildingKey();
    }

    public Collection<Building> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public Building findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (Building)map.get(key);
    }

    public List<Building> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<Building> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(Building entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<Building> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (Building entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<Building> entities = this.findByPredicate(predicate);
        for (Building entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
