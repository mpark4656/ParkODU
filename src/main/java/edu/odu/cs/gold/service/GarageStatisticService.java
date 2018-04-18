package edu.odu.cs.gold.service;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import edu.odu.cs.gold.model.GarageStatistic;
import edu.odu.cs.gold.repository.GarageStatisticRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GarageStatisticService {
    private GarageStatisticRepository garageStatisticRepository;

    public GarageStatisticService(GarageStatisticRepository garageStatisticRepository) {
        this.garageStatisticRepository = garageStatisticRepository;
    }

    /**
     * Method that finds all GarageStatistics for the garage of the specified garage key
     * and returns a collection of GarageStatistic objects that contains average capacity
     * for each hour of the day (00:00 to 23:00).
     *
     * @param garageKey String
     * @return ArrayList collection of GarageStatistic objects
     */
    public ArrayList<GarageStatistic> findAverageGarageCapacityByHour(String garageKey) {
        ArrayList<GarageStatistic> garageStatisticResult = new ArrayList<> ();

        Predicate predicate = Predicates.equal("garageKey", garageKey);

        List<GarageStatistic> garageStatistics =
                new ArrayList<> (garageStatisticRepository.findByPredicate(predicate));

        Double[] totalCapacities = new Double[24];
        for(int hour = 0; hour < totalCapacities.length; hour++) {
            totalCapacities[hour] = 0.0;
        }

        Integer[] totalCounts = new Integer[24];
        for(int hour = 0; hour < totalCounts.length; hour++) {
            totalCounts[hour] = 0;
        }

        for(GarageStatistic garageStatistic : garageStatistics) {
            Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            calendar.setTime(garageStatistic.getTimestamp());

            for(int hour = 0; hour < 24; hour++) {
                if(calendar.get(Calendar.HOUR_OF_DAY) == hour &&
                        calendar.get(Calendar.MINUTE) == 0) {
                    totalCapacities[hour] += garageStatistic.getCapacity();
                    totalCounts[hour] += 1;
                }
            }
        }

        for(int hour = 0; hour < totalCapacities.length; hour++) {
            GarageStatistic garageStatistic = new GarageStatistic();

            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            cal.set(Calendar.HOUR_OF_DAY, hour);
            Date date = cal.getTime();

            garageStatistic.setGarageKey(garageKey);
            garageStatistic.setTimestamp(date);
            garageStatistic.setCapacity(totalCapacities[hour] / totalCounts[hour]);
            garageStatisticResult.add(garageStatistic);
        }

        return garageStatisticResult;
    }

    public ArrayList<GarageStatistic> findGarageCapacityByDate(String garageKey, Date date) {
        ArrayList<GarageStatistic> garageStatisticsResult = new ArrayList<>();

        Predicate predicate = Predicates.equal("garageKey", garageKey);
        ArrayList<GarageStatistic> garageStatistics = new ArrayList<>(garageStatisticRepository.findByPredicate(predicate));

        for(int hour = 0; hour < 24; hour++) {
            for(GarageStatistic garageStatistic : garageStatistics) {
                Calendar garageStatisticCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York"));
                garageStatisticCalendar.setTime(garageStatistic.getTimestamp());

                Calendar givenDateCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("America/New_York"));
                givenDateCalendar.setTime(date);

                if(garageStatisticCalendar.get(Calendar.YEAR) == givenDateCalendar.get(Calendar.YEAR) &&
                        garageStatisticCalendar.get(Calendar.MONTH) == givenDateCalendar.get(Calendar.MONTH) &&
                        garageStatisticCalendar.get(Calendar.DAY_OF_MONTH) == givenDateCalendar.get(Calendar.DAY_OF_MONTH) &&
                        garageStatisticCalendar.get(Calendar.HOUR_OF_DAY) == hour &&
                        garageStatisticCalendar.get(Calendar.MINUTE) == 0) {

                    garageStatisticsResult.add(garageStatistic);
                }
            }
        }

        return garageStatisticsResult;
    }

}
