package edu.odu.cs.gold.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import edu.odu.cs.gold.model.Garage;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ParkODUDBConverterTests {

    public static final String DATABASE_NAME = "Test";

    @Test
    public void testToDBObject() {
        Mongo mongo = mock(Mongo.class);
        ParkODUDBConverter dbConverter = new ParkODUDBConverter(mongo, DATABASE_NAME);
        Garage garage = new Garage();
        garage.setGarageKey(UUID.randomUUID().toString());
        garage.setName("Garage A");
        DBObject newGarage = dbConverter.toDBObject(garage);
        assertEquals(newGarage.get("_class"), garage.getClass().getName());
        assertEquals(newGarage.get("garageKey"), garage.getGarageKey());
        assertEquals(newGarage.get("name"), garage.getName());
        Integer i = new Integer(1);
        DBObject newI = dbConverter.toDBObject(i);
        assertEquals(newI.get("_class"), new ValueWrapper(i).getClass().getName());
        assertEquals(newI.get("value"), i.intValue());
    }

    @Test
    public void testToObject() {
        Mongo mongo = mock(Mongo.class);
        ParkODUDBConverter dbConverter = new ParkODUDBConverter(mongo, DATABASE_NAME);
        Garage garage = new Garage();
        garage.setGarageKey(UUID.randomUUID().toString());
        garage.setName("Garage A");
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_class", garage.getClass().getName());
        dbObject.put("garageKey", garage.getGarageKey());
        dbObject.put("name", garage.getName());
        Garage newGarage = (Garage)dbConverter.toObject(Garage.class, dbObject);
        assertEquals(garage.getClass(), newGarage.getClass());
        assertEquals(garage.getGarageKey(), newGarage.getGarageKey());
        assertEquals(garage.getName(), newGarage.getName());
        DBObject dbObject2 = new BasicDBObject();
        dbObject2.put("_class", ValueWrapper.class.getName());
        dbObject2.put("value", 1);
        Integer newI = (Integer)dbConverter.toObject(ValueWrapper.class, dbObject2);
        assertEquals(1, newI.intValue());
    }

    @Test
    public void testIsStandardClass() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        ParkODUDBConverter dbConverter = new ParkODUDBConverter(mongoTemplate);
        Date date = new Date();
        String string = "test";
        ObjectId objectId = new ObjectId();
        BSONObject bsonObject = mock(BSONObject.class);
        Boolean bool = true;
        Double d = new Double(1.0);
        Integer i = new Integer(1);
        Long l = new Long(1L);
        Pattern pattern = Pattern.compile("^([A-Za-z0-9]+)$");
        UUID uuid = UUID.randomUUID();
        Garage garage = new Garage();
        assertTrue(dbConverter.isStandardClass(date.getClass()));
        assertTrue(dbConverter.isStandardClass(string.getClass()));
        assertTrue(dbConverter.isStandardClass(objectId.getClass()));
        assertTrue(dbConverter.isStandardClass(date.getClass()));
        //assertTrue(dbConverter.isStandardClass(bsonObject.getClass()));
        assertTrue(dbConverter.isStandardClass(bool.getClass()));
        assertTrue(dbConverter.isStandardClass(d.getClass()));
        assertTrue(dbConverter.isStandardClass(i.getClass()));
        assertTrue(dbConverter.isStandardClass(l.getClass()));
        assertTrue(dbConverter.isStandardClass(pattern.getClass()));
        assertTrue(dbConverter.isStandardClass(uuid.getClass()));
        assertFalse(dbConverter.isStandardClass(garage.getClass()));
    }
}
