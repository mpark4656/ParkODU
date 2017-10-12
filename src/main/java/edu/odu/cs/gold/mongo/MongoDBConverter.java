package edu.odu.cs.gold.mongo;

import com.mongodb.DBObject;

public interface MongoDBConverter {
    DBObject toDBObject(Object obj);
    Object toObject(Class clazz, DBObject dbObject);
}
