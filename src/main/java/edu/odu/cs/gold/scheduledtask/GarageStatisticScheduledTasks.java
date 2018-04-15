package edu.odu.cs.gold.scheduledtask;

import edu.odu.cs.gold.model.Garage;
import edu.odu.cs.gold.model.GarageStatistic;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.GarageStatisticRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class GarageStatisticScheduledTasks {

    private GarageRepository garageRepository;
    private GarageStatisticRepository garageStatisticRepository;

    public GarageStatisticScheduledTasks(GarageRepository garageRepository,
                                         GarageStatisticRepository garageStatisticRepository) {

        this.garageRepository = garageRepository;
        this.garageStatisticRepository = garageStatisticRepository;
    }

    /**
     * Every hour from midnight to 5:59AM everyday
     */
    @Scheduled(cron = "0 0 0-5 * * *")
    public void earlyMorningTask() {
        generateGarageStatistics();
    }

    /**
     * Every 15 minutes from 6:00AM to 2:59PM everyday
     */
    @Scheduled(cron = "0 */15 6-14 * * *")
    public void morningTask() {
        generateGarageStatistics();
    }

    /**
     * Every 30 minutes from 3:00PM to 7:59PM everyday
     */
    @Scheduled(cron = "0 */30 15-19 * * *")
    public void afternoonTask() {
        generateGarageStatistics();
    }

    /**
     * Every hour from 8:00PM to 11:59PM everyday
     */
    @Scheduled(cron = "0 0 20-23 * * *")
    public void eveningTask() {
        generateGarageStatistics();
    }

    private void generateGarageStatistics(){
        ArrayList<Garage> garages = new ArrayList<> (garageRepository.findAll());

        for(Garage garage : garages) {
            GarageStatistic garageStatistic = new GarageStatistic();
            garageStatistic.setGarageKey(garage.getGarageKey());
            garageStatistic.setCapacity(garage.getCapacity());
            garageStatistic.setTimestamp(new Date());

            garageStatisticRepository.save(garageStatistic);
        }
    }
}
