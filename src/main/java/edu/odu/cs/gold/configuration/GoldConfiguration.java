package edu.odu.cs.gold.configuration;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.ParkingSpace;
import edu.odu.cs.gold.mongo.MongoMapStore;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("edu.odu.cs")
public class GoldConfiguration implements ApplicationContextAware {

    public static final String COLLECTION_GARAGE = "Garage";
    public static final String COLLECTION_PARKING_SPACE = "ParkingSpace";

    @Autowired
    public Environment environment;

    @Autowired
    public MongoTemplate mongoTemplate;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Lazy
    public Config hazelcastConfig() {
        Config hazelcastConfig = new Config();

        // Add MapConfigs
        for (MapConfig mapConfig : applicationContext.getBeansOfType(MapConfig.class).values()) {
            hazelcastConfig.addMapConfig(mapConfig);
        }

        // Management Center Configs
        if (environment.getProperty("hazelcast.mancenter.enable", Boolean.class, false)) {

            String managementUrl = environment.getProperty("hazelcast.mancenter.url", "http://localhost:8080/mancenter");

            ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
            managementCenterConfig.setUrl(managementUrl);
            managementCenterConfig.setEnabled(true);

            System.err.println(managementCenterConfig);

            hazelcastConfig.setManagementCenterConfig(managementCenterConfig);
        }

        // Group Configs
        hazelcastConfig.getGroupConfig().setName(environment.getProperty("hazelcast.group.name", "dev"));
        hazelcastConfig.getGroupConfig().setPassword(environment.getProperty("hazelcast.group.password", ""));


        return hazelcastConfig;
    }

    @Bean
    @Lazy
    public HazelcastInstance hazelcastInstance() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig());
        return hazelcastInstance;
    }

    @Bean
    public GarageRepository garageRepository() {
        return new GarageRepository(hazelcastInstance(), COLLECTION_GARAGE);
    }

    @Bean
    public ParkingSpaceRepository parkingSpaceRepository() {
        return new ParkingSpaceRepository(hazelcastInstance(), COLLECTION_PARKING_SPACE);
    }

    @Bean
    public MongoMapStore garageMapStore() {
        return new MongoMapStore(mongoTemplate, COLLECTION_GARAGE, Garage.class);
    }

    @Bean
    public MongoMapStore parkingSpaceMapStore() {
        return new MongoMapStore(mongoTemplate, COLLECTION_PARKING_SPACE, ParkingSpace.class);
    }

    @Bean
    public MapConfig garageRepositoryMapConfig() {
        MapConfig mapConfig = new MapConfig(COLLECTION_GARAGE);

        // MapStore
        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(garageMapStore());
        mapStoreConfig.setEnabled(true);
        mapStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        return mapConfig;
    }

    @Bean
    public MapConfig parkingSpaceRepositoryMapConfig() {
        MapConfig mapConfig = new MapConfig(COLLECTION_PARKING_SPACE);

        // MapStore
        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(parkingSpaceMapStore());
        mapStoreConfig.setEnabled(true);
        mapStoreConfig.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        // Indexed Attributes
        mapConfig.addMapIndexConfig(new MapIndexConfig("garageKey", false));
        mapConfig.addMapIndexConfig(new MapIndexConfig("available", false));
        mapConfig.addMapIndexConfig(new MapIndexConfig("floor", false));

        return mapConfig;
    }
}
