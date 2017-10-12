package edu.odu.cs.gold.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

public class GoldMongoConfiguration extends AbstractMongoConfiguration {

    public static final String DATABASE_NAME = "gold";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost", 27017);
    }
}
