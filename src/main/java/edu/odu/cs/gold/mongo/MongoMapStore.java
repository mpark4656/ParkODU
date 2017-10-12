package edu.odu.cs.gold.mongo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoaderLifecycleSupport;
import com.hazelcast.core.MapStore;
import com.mongodb.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

public class MongoMapStore implements MapStore, MapLoaderLifecycleSupport {

    private MongoTemplate mongoTemplate;
    private DBCollection dbCollection;
    private MongoDBConverter mongoDBConverter;
    private Class clazz;

    public MongoMapStore(MongoTemplate mongoTemplate, String collectionName, Class clazz) {
        this.mongoTemplate = mongoTemplate;
        this.dbCollection = mongoTemplate.getCollection(collectionName);
        this.mongoDBConverter = new GoldDBConverter(mongoTemplate);
        this.clazz = clazz;
    }

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Object load(Object key) {
        DBObject dbObject = dbCollection.findOne(key);
        return mongoDBConverter.toObject(clazz, dbObject);
    }

    @Override
    public Map<String, Object> loadAll(Collection keys) {
        Map map = new HashMap<>();
        BasicDBList dbObjects = new BasicDBList();
        for (Object key : keys) {
            dbObjects.add(new BasicDBObject("_id", key));
        }
        BasicDBObject query = new BasicDBObject("$or", dbObjects);
        DBCursor cursor = dbCollection.find(query);
        while (cursor.hasNext()) {
            try {
                DBObject value = cursor.next();
                map.put(value.get("_id"), mongoDBConverter.toObject(clazz, value));
            }
            catch (Exception e) {

            }
        }
        return map;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        Set keys = new HashSet();
        DBCursor dbCursor = dbCollection.find();
        while (dbCursor.hasNext()) {
            keys.add(dbCursor.next().get("_id"));
        }
        return keys;
    }

    @Override
    public void store(Object key, Object value) {
        DBObject dbObject = (BasicDBObject)mongoTemplate.getConverter().convertToMongoType(value);
        dbObject.put("_id", key);
        dbCollection.save(dbObject);
    }

    @Override
    public void storeAll(Map map) {
        for (Map.Entry entry: (Set<Map.Entry>)map.entrySet()){
            Object key = entry.getKey();
            Object value = entry.getValue();
            store(key, value);
        }
    }

    @Override
    public void delete(Object key) {
        DBObject dbObject = new BasicDBObject("_id", key);
        dbCollection.remove(dbObject);
    }

    @Override
    public void deleteAll(Collection keys) {
        BasicDBList dbObjects = new BasicDBList();
        for (Object key : keys) {
            dbObjects.add(new BasicDBObject("_id", key));
        }
        DBObject dbObject = new BasicDBObject("$or", dbObjects);
        dbCollection.remove(dbObject);
    }

}