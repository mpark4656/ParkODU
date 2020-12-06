package edu.odu.cs.gold.configuration;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.List;

@Configuration
public class ParkODUMongoConfiguration extends AbstractMongoConfiguration {

    @Autowired
    private ParkODUMongoProperties mongoProperties;

    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    private void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        setDatabaseName(mongoProperties.getDatabase());
        List<ServerAddress> serverAddresses = mongoProperties.getServerAddresses();
        List<MongoCredential> mongoCredentials = mongoProperties.getMongoCredentials();

        if (serverAddresses.size() > 0) {
            if (mongoCredentials.size() > 0) {
                return new MongoClient(serverAddresses, mongoCredentials, new MongoClientOptions.Builder().socketKeepAlive(true).maxConnectionIdleTime(30000).build());
            }
            else {
                return new MongoClient(serverAddresses);
            }
        } else if(mongoProperties.getMongoClientURI() != null) {
            return new MongoClient(mongoProperties.getMongoClientURI());
        } else {
            return new MongoClient("localhost", 27017);
        }
    }
}
