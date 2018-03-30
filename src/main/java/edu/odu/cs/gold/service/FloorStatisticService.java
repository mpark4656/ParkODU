package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FloorStatisticService {
    private FloorStatisticRepository floorStatisticRepository;

    public FloorStatisticService(FloorStatisticRepository floorStatisticRepository) {
        this.floorStatisticRepository = floorStatisticRepository;
    }

    /**
     * Method that finds all FloorStatistics for the floor of the specified floor key
     * and returns a collection of FloorStatistic objects that contains average capacity
     * for each hour of the day (00:00 to 23:00).
     *
     * @param floorKey String
     * @return ArrayList collection of FloorStatistic objects
     */
    public ArrayList<FloorStatistic> findAverageFloorCapacityByHour(String floorKey) {
        ArrayList<FloorStatistic> floorStatisticResult = new ArrayList<> ();

        Predicate predicate = Predicates.equal("floorKey", floorKey);

        List<FloorStatistic> floorStatistics =
                new ArrayList<> (floorStatisticRepository.findByPredicate(predicate));

        Double[] totalCapacities = new Double[24];
        for(int hour = 0; hour < totalCapacities.length; hour++) {
            totalCapacities[hour] = 0.0;
        }

        Integer[] totalCounts = new Integer[24];
        for(int hour = 0; hour < totalCounts.length; hour++) {
            totalCounts[hour] = 0;
        }

        for(FloorStatistic floorStatistic : floorStatistics) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(floorStatistic.getTimestamp());

            for(int hour = 0; hour < 24; hour++) {
                if(calendar.get(Calendar.HOUR_OF_DAY) == hour &&
                        calendar.get(Calendar.MINUTE) == 0) {
                    totalCapacities[hour] += floorStatistic.getCapacity();
                    totalCounts[hour] += 1;
                }
            }
        }

        for(int hour = 0; hour < totalCapacities.length; hour++) {
            FloorStatistic floorStatistic = new FloorStatistic();

            Calendar cal = GregorianCalendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            Date date = cal.getTime();

            floorStatistic.setFloorKey(floorKey);
            floorStatistic.setTimestamp(date);
            floorStatistic.setCapacity(totalCapacities[hour] / totalCounts[hour]);
            floorStatisticResult.add(floorStatistic);
        }

        return floorStatisticResult;
    }
}
