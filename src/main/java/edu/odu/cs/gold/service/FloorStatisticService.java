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
            Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
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

            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.set(Calendar.HOUR_OF_DAY, hour);
            Date date = cal.getTime();

            floorStatistic.setFloorKey(floorKey);
            floorStatistic.setTimestamp(date);
            floorStatistic.setCapacity(totalCapacities[hour] / totalCounts[hour]);
            floorStatisticResult.add(floorStatistic);
        }

        return floorStatisticResult;
    }

    public ArrayList<FloorStatistic> findFloorCapacityByDate(String floorKey, Date date) {
        ArrayList<FloorStatistic> floorStatisticsResult = new ArrayList<>();

        Predicate predicate = Predicates.equal("floorKey", floorKey);
        ArrayList<FloorStatistic> floorStatistics = new ArrayList<>(floorStatisticRepository.findByPredicate(predicate));

        System.err.println("The floorKey I received is " + floorKey);
        System.err.println("The date I received is " + date.toString());
        System.err.println("The number of floorstatistics I found with the predicate is " + floorStatistics.size());

        for(FloorStatistic floorStatistic : floorStatistics) {
            Calendar floorStatisticCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            floorStatisticCalendar.setTime(floorStatistic.getTimestamp());
            System.err.println("Outer Iteration");
            System.err.println("floorStatisticKey: " + floorStatistic.getFloorStatisticKey());
            System.err.println("floorKey: " + floorStatistic.getFloorKey());
            System.err.println("Year: " + floorStatisticCalendar.get(Calendar.YEAR));
            System.err.println("Month: " + floorStatisticCalendar.get(Calendar.MONTH));
            System.err.println("Day: " + floorStatisticCalendar.get(Calendar.DAY_OF_MONTH));
            System.err.println("Hour: " + floorStatisticCalendar.get(Calendar.HOUR_OF_DAY));
            System.err.println();
        }

        for(int hour = 0; hour < 24; hour++) {
            for(FloorStatistic floorStatistic : floorStatistics) {
                Calendar floorStatisticCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
                floorStatisticCalendar.setTime(floorStatistic.getTimestamp());

                Calendar givenDateCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
                givenDateCalendar.setTime(date);

                if(floorStatisticCalendar.get(Calendar.YEAR) == givenDateCalendar.get(Calendar.YEAR) &&
                        floorStatisticCalendar.get(Calendar.MONTH) == givenDateCalendar.get(Calendar.MONTH) &&
                        floorStatisticCalendar.get(Calendar.DAY_OF_MONTH) == givenDateCalendar.get(Calendar.DAY_OF_MONTH) &&
                        floorStatisticCalendar.get(Calendar.HOUR_OF_DAY) == hour &&
                        floorStatisticCalendar.get(Calendar.MINUTE) == 0) {

                    System.err.println("Inner Iteration");
                    System.err.println("Year: " + floorStatisticCalendar.get(Calendar.YEAR));
                    System.err.println("Month: " + floorStatisticCalendar.get(Calendar.MONTH));
                    System.err.println("Day: " + floorStatisticCalendar.get(Calendar.DAY_OF_MONTH));
                    System.err.println("Hour: " + floorStatisticCalendar.get(Calendar.HOUR_OF_DAY));
                    System.err.println();

                    //System.err.println(floorStatistic);
                    floorStatisticsResult.add(floorStatistic);
                }
            }
        }

        return floorStatisticsResult;
    }

}
