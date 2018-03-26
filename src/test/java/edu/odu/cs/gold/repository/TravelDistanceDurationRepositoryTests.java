package edu.odu.cs.gold.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import edu.odu.cs.gold.model.TravelDistanceDuration;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TravelDistanceDurationRepositoryTests {

    private static final String COLLECTION_NAME = "Collection";
    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";

    private TravelDistanceDurationRepository repository;
    private IMap iMap;
    private HazelcastInstance hazelcastInstance;

    private TravelDistanceDuration entityOne;
    private TravelDistanceDuration entityTwo;

    private List<TravelDistanceDuration> entities;
    private Set<String> keys;

    @Before
    public void setup() {
        entityOne = new TravelDistanceDuration();
        entityOne.setTravelDistanceDurationKey(UUID1);

        entityTwo = new TravelDistanceDuration();
        entityTwo.setTravelDistanceDurationKey(UUID2);

        entities = new ArrayList<>();
        entities.add(entityOne);
        entities.add(entityTwo);

        keys = new HashSet<>();
        keys.add(UUID1);
        keys.add(UUID2);

        Map map = mock(Map.class);
        when(map.values()).thenReturn(entities);
        when(map.size()).thenReturn(2);

        iMap = mock(IMap.class);
        when(iMap.values()).thenReturn(entities);
        when(iMap.values(any(Predicate.class))).thenReturn(entities);
        when(iMap.getAll(anySet())).thenReturn(map);

        hazelcastInstance = mock(HazelcastInstance.class);
        when(hazelcastInstance.getMap(COLLECTION_NAME)).thenReturn(iMap);

        repository = new TravelDistanceDurationRepository(hazelcastInstance, COLLECTION_NAME);
        when(repository.findAll()).thenReturn(entities);
        when(repository.findByKey(UUID1)).thenReturn(entityOne);
        when(repository.findByKeys(keys)).thenReturn(entities);
        when(repository.findByPredicate(any(Predicate.class))).thenReturn(entities);
    }

    @Test
    public void testGetId() {
        assertEquals(UUID1, repository.getId(entityOne));
    }

    @Test
    public void testFindAll() {
        assertTrue(repository.findAll().size() == 2);
    }

    @Test
    public void testFindByKey() {
        assertEquals(entityOne, repository.findByKey(UUID1));
    }

    @Test
    public void testFindByKeys() {
        assertTrue(repository.findByKeys(keys).size() == 2);
    }

    @Test
    public void testFindByPredicate() {
        Predicate predicate = mock(Predicate.class);
        assertTrue(repository.findByPredicate(predicate).size() == 2);
    }

    @Test
    public void testCountByPredicate() {
        Predicate predicate = mock(Predicate.class);
        assertTrue(repository.countByPredicate(predicate) == 2);
    }

    @Test
    public void testSave() {
        repository.save(entityOne);
        repository.save(entities);
    }

    @Test
    public void testDelete() {
        repository.delete(UUID1);
        Predicate predicate = mock(Predicate.class);
        assertTrue(repository.deleteByPredicate(predicate) == 2);
    }

    @Test
    public void testLoadAll() {
        repository.loadAll();
    }
}
