package edu.odu.cs.gold.scheduledtask;

import edu.odu.cs.gold.model.Floor;
import edu.odu.cs.gold.model.FloorStatistic;
import edu.odu.cs.gold.repository.FloorRepository;
import edu.odu.cs.gold.repository.FloorStatisticRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;

@Component
public class FloorStatisticScheduledTasks {

    private FloorRepository floorRepository;
    private FloorStatisticRepository floorStatisticRepository;

    public FloorStatisticScheduledTasks(FloorRepository floorRepository,
                                        FloorStatisticRepository floorStatisticRepository) {

        this.floorRepository = floorRepository;
        this.floorStatisticRepository = floorStatisticRepository;
    }

    /**
     * Every hour from midnight to 5:59AM everyday
     */
    @Scheduled(cron = "0 0 0-5 * * *")
    public void earlyMorningTask() {
        generateFloorStatistics();
    }

    /**
     * Every 15 minutes from 6:00AM to 2:59PM everyday
     */
    @Scheduled(cron = "0 */15 6-14 * * *")
    public void morningTask() {
        generateFloorStatistics();
    }

    /**
     * Every 30 minutes from 3:00PM to 7:59PM everyday
     */
    @Scheduled(cron = "0 */30 15-19 * * *")
    public void afternoonTask() {
        generateFloorStatistics();
    }

    /**
     * Every hour from 8:00PM to 11:59PM everyday
     */
    @Scheduled(cron = "0 0 20-23 * * *")
    public void eveningTask() {
        generateFloorStatistics();
    }

    private void generateFloorStatistics(){
        ArrayList<Floor> floors = new ArrayList<> (floorRepository.findAll());

        for(Floor floor : floors) {
            FloorStatistic floorStatistic = new FloorStatistic();
            floorStatistic.setFloorKey(floor.getFloorKey());
            floorStatistic.setCapacity(floor.getCapacity());
            floorStatistic.setTimestamp(new Date());

            floorStatisticRepository.save(floorStatistic);
        }
    }
}
