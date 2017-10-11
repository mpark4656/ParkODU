package edu.odu.cs.gold.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.ParkingSpaceRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan("edu.odu.cs")
public class GoldConfiguration implements ApplicationContextAware {

    public static final String COLLECTION_GARAGE = "Gold.Garage";
    public static final String COLLECTION_PARKING_SPACES = "Gold.ParkingSpaces";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Lazy
    public Config hazelcastConfig() {
        Config hazelcastConfig = new Config();
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
        return new ParkingSpaceRepository(hazelcastInstance(), COLLECTION_PARKING_SPACES);
    }
}
