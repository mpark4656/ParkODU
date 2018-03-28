package edu.odu.cs.gold.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParkODUConfigurationTests {

    public static final String HAZELCAST_MANAGEMENT_CENTER_URL = "http://localhost:8080/mancenter";

    private Environment environment;
    private ParkODUConfiguration parkODUConfiguration;
    private Config hazelcastConfig;
    private HazelcastInstance hazelcastInstance;

    @Before
    public void setup() {
        environment = mock(Environment.class);
        when(environment.getProperty("hazelcast.mancenter.enable", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("hazelcast.mancenter.url", "http://localhost:8080/mancenter")).thenReturn(HAZELCAST_MANAGEMENT_CENTER_URL);
        when(environment.getProperty("hazelcast.group.name", "parkodu")).thenReturn("parkodu");
        when(environment.getProperty("hazelcast.group.password", "")).thenReturn("");
        parkODUConfiguration = new ParkODUConfiguration();
        parkODUConfiguration.environment = environment;
        parkODUConfiguration.setApplicationContext(mock(ApplicationContext.class));

        hazelcastConfig = parkODUConfiguration.hazelcastConfig();
        hazelcastInstance = parkODUConfiguration.hazelcastInstance();
    }

    @Test
    public void hazelcastConfig() {
        assertNotNull(hazelcastConfig.getManagementCenterConfig());
        assertEquals(HAZELCAST_MANAGEMENT_CENTER_URL, hazelcastConfig.getManagementCenterConfig().getUrl());
        assertTrue(hazelcastConfig.getManagementCenterConfig().isEnabled());
    }

    @Test
    public void hazelcastInstance() {
        assertNotNull(hazelcastInstance);
    }

    @Test
    public void garageRepository() {
        GarageRepository garageRepository = parkODUConfiguration.garageRepository();
        assertNotNull(garageRepository);
    }

    @Test
    public void floorRepository() {
        FloorRepository floorRepository = parkODUConfiguration.floorRepository();
        assertNotNull(floorRepository);
    }
}
