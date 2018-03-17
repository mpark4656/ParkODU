package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;

import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserRepository {

    private HazelcastInstance hazelcastInstance;
    private String collectionName;

    public UserRepository(HazelcastInstance hazelcastInstance, String collectionName) {
        this.hazelcastInstance = hazelcastInstance;
        this.collectionName = collectionName;
    }

    public String getId(User entity) {
        return entity.getId();
    }

    public Collection<User> findAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        return map.values();
    }

    public User findByKey(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (User)map.get(key);
    }

    public User findByConfirmationToken(String confirmationToken) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return (User)map.get(confirmationToken);
    }

    public List<User> findByKeys(Set<String> keys) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.getAll(keys).values());
    }

    public List<User> findByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        return new ArrayList<>(map.values(predicate));
    }

    public int countByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        System.out.println(map.getName());
        return map.values(predicate).size();
    }

    public void save(User entity) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.set(getId(entity), entity);
    }

    public void save(Collection<User> entities) {
        IMap map = hazelcastInstance.getMap(collectionName);
        for (User entity : entities) {
            map.set(getId(entity), entity);
        }
    }

    public void delete(String key) {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.delete(key);
    }

    public int deleteByPredicate(Predicate predicate) {
        IMap map = hazelcastInstance.getMap(collectionName);
        List<User> entities = this.findByPredicate(predicate);
        for (User entity : entities) {
            map.delete(getId(entity));
        }
        return entities.size();
    }

    public void loadAll() {
        IMap map = hazelcastInstance.getMap(collectionName);
        map.loadAll(false);
    }
}