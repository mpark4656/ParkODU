package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.GarageStatistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GarageStatisticRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public GarageStatisticRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(GarageStatistic entity) {
        return entity.getGarageStatisticKey();
    }

    public List<GarageStatistic> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values());
    }

    public GarageStatistic findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (GarageStatistic)map.get(key);
    }

    public List<GarageStatistic> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<GarageStatistic> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(GarageStatistic entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<GarageStatistic> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (GarageStatistic entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<GarageStatistic> entities = this.findByPredicate(predicate);
        for (GarageStatistic entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
