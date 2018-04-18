package edu.odu.cs.gold.configuration;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties(prefix = "mongo")
public class ParkODUMongoProperties {

    String username;
    String password;
    String database;
    List<String> servers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public List<String> getServers() {
        if (servers == null) {
            servers = new ArrayList<>();
        }
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public List<ServerAddress> getServerAddresses() {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String server : servers) {
            Pattern pattern = Pattern.compile("^([A-Za-z0-9.]+):([0-9]+)$");
            Matcher matcher = pattern.matcher(server);
            if (matcher.find()) {
                String address = matcher.group(1);
                String port = matcher.group(2);
                serverAddresses.add(new ServerAddress(address, Integer.parseInt(port)));
            }
        }
        return serverAddresses;
    }

    public List<MongoCredential> getMongoCredentials() {
        List<MongoCredential> mongoCredentials = new ArrayList<>();
        if (username != null
                && username.length() > 0
                && database != null
                && database.length() > 0
                && password != null
                && password.length() > 0) {
            mongoCredentials.add(MongoCredential.createCredential(username, database, password.toCharArray()));
        }
        return mongoCredentials;
    }
}
