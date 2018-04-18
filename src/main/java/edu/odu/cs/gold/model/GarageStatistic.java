package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class GarageStatistic implements Serializable {

    private String garageStatisticKey;
    private String garageKey;
    private Double capacity;
    private Date timestamp;

    public GarageStatistic() {
        garageStatisticKey = UUID.randomUUID().toString();
    }

    public String getGarageStatisticKey() {
        return garageStatisticKey;
    }

    public void setGarageStatisticKey(String garageStatisticKey) {
        this.garageStatisticKey = garageStatisticKey;
    }

    public String getGarageKey() {
        return garageKey;
    }

    public void setGarageKey(String garageKey) {
        this.garageKey = garageKey;
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
        return "garageStatistic{" +
                " garageStatisticKey='" + garageStatisticKey + '\'' +
                ", garageKey='" + garageKey + '\'' +
                ", capacity=" + capacity +
                ", timestamp=" + timestamp +
                " }";
    }
}
