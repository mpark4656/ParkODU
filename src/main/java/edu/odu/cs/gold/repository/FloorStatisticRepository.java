package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FloorStatisticRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public FloorStatisticRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(FloorStatistic entity) {
        return entity.getFloorStatisticKey();
    }

    public Collection<FloorStatistic> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public FloorStatistic findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (FloorStatistic)map.get(key);
    }

    public List<FloorStatistic> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<FloorStatistic> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public void save(FloorStatistic entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<FloorStatistic> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (FloorStatistic entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<FloorStatistic> entities = this.findByPredicate(predicate);
        for (FloorStatistic entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }

}
