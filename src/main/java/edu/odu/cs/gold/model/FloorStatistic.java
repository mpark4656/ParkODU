package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class FloorStatistic implements Serializable {

    private String floorStatisticKey;
    private String floorKey;
    private Double capacity;
    private Date timestamp;

    public FloorStatistic() {
        floorStatisticKey = UUID.randomUUID().toString();
    }

    public String getFloorStatisticKey() {
        return floorStatisticKey;
    }

    public void setFloorStatisticKey(String floorStatisticKey) {
        this.floorStatisticKey = floorStatisticKey;
    }

    public String getFloorKey() {
        return floorKey;
    }

    public void setFloorKey(String floorKey) {
        this.floorKey = floorKey;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FloorStatistic{" +
                " floorStatisticKey='" + floorStatisticKey + '\'' +
                ", floorKey='" + floorKey + '\'' +
                ", capacity=" + capacity +
                ", timestamp=" + timestamp +
                " }";
    }
}
