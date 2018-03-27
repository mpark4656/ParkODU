package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.RoleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class RoleTypeRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public RoleTypeRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(RoleType entity) {
        return entity.getRoleKey();
    }

    public Collection<RoleType> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public RoleType findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (RoleType)map.get(key);
    }

    public List<RoleType> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<RoleType> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public void save(RoleType entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<RoleType> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (RoleType entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values(predicate).size();
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<RoleType> entities = this.findByPredicate(predicate);
        for (RoleType entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }
}
