package edu.odu.cs.gold.mongo;

import com.mongodb.*;
import edu.odu.cs.gold.model.Garage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MongoMapStoreTests {

    private static final String COLLECTION_NAME = "TestGarage";

    private MongoTemplate mongoTemplate;
    private DBCollection dbCollection;

    private MongoMapStore mongoMapStore;

    private Garage garageOne;
    private DBObject garageOneDBObject;

    private Garage garageTwo;
    private DBObject garageTwoDBObject;

    @Before
    public void setup() {
        garageOne = new Garage();
        garageOne.setGarageKey(UUID.randomUUID().toString());

        garageOneDBObject = new BasicDBObject();
        garageOneDBObject.put("_class", Garage.class.getName());
        garageOneDBObject.put("garageKey", garageOne.getGarageKey());

        garageTwo = new Garage();
        garageTwo.setGarageKey(UUID.randomUUID().toString());

        garageTwoDBObject = new BasicDBObject();
        garageTwoDBObject.put("_class", Garage.class.getName());
        garageTwoDBObject.put("garageKey", garageOne.getGarageKey());

        dbCollection = mock(DBCollection.class);
        when(dbCollection.findOne(garageOne.getGarageKey())).thenReturn(garageOneDBObject);
        when(dbCollection.find(any(BasicDBObject.class))).thenReturn(mock(DBCursor.class));
        when(dbCollection.find()).thenReturn(mock(DBCursor.class));
        when(dbCollection.save(any())).thenReturn(mock(WriteResult.class));

        mongoTemplate = mock(MongoTemplate.class);
        when(mongoTemplate.getCollection(COLLECTION_NAME)).thenReturn(dbCollection);

        mongoMapStore = new MongoMapStore(mongoTemplate, COLLECTION_NAME, Garage.class);
    }

    @Test
    public void init() {
        mongoMapStore.init(null, null, null);
    }

    @Test
    public void destroy() {
        mongoMapStore.destroy();
    }

    @Test
    public void load() {
        // TODO
        //mongoMapStore.load(garageOne.getGarageKey());
    }

    @Test
    public void loadAll() {
        List<String> keys = new ArrayList<>();
        keys.add(garageOne.getGarageKey());
        keys.add(garageTwo.getGarageKey());
        mongoMapStore.loadAll(keys);
    }

    @Test
    public void loadAllKeys() {
        mongoMapStore.loadAllKeys();
    }

    @Test
    public void store() {
        // TODO
        //mongoMapStore.store(garageOne.getGarageKey(), garageOne);
    }

    @Test
    public void storeAll() {
        // TODO
        //Map<String, Garage> garageMap = new HashMap<>();
        //garageMap.put(garageOne.getGarageKey(), garageOne);
        //garageMap.put(garageTwo.getGarageKey(), garageTwo);
        //mongoMapStore.storeAll(garageMap);
    }

    @Test
    public void delete() {
        mongoMapStore.delete(garageOne.getGarageKey());
    }

    @Test
    public void deleteAll() {
        List<String> keys = new ArrayList<>();
        keys.add(garageOne.getGarageKey());
        keys.add(garageTwo.getGarageKey());
        mongoMapStore.deleteAll(keys);
    }
}

