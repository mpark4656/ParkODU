package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Floor implements Serializable {

    private String floorKey;
    private String garageKey;
    private String number;
    private Integer availableSpaces;
    private Integer totalSpaces;
    private String description;
    private Double capacity;
    private Date lastUpdated;

    public Floor() { this.floorKey = UUID.randomUUID().toString(); }

    public Floor(String floorKey, String garageKey, String number, Integer availableSpaces, Integer totalSpaces, String description) {
        this.floorKey = floorKey;
        this.garageKey = garageKey;
        this.number = number;
        this.availableSpaces = availableSpaces;
        this.totalSpaces = totalSpaces;
        this.description = description;
        this.capacity = ((double)availableSpaces / totalSpaces) * 100.00;
        this.lastUpdated = new Date();
    }

    public String getFloorKey() {
        return floorKey;
    }

    public void setFloorKey(String floorKey) {
        this.floorKey = floorKey;
    }

    public String getGarageKey() {
        return garageKey;
    }

    public void setGarageKey(String garageKey) {
        this.garageKey = garageKey;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(Integer availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public Integer getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(Integer totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void calculateCapacity() {
        if (totalSpaces > 0) {
            double total = (double)totalSpaces;
            double unavailableTotal = (double)(totalSpaces - availableSpaces);
            capacity = Math.round((unavailableTotal / total) * 10000.00) / 100.00;
        }
        else {
            capacity = 0.0;
        }
    }

    public Double getCapacity() {
        return capacity;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Floor{" +
                " floorKey='" + floorKey + '\'' +
                ", garageKey='" + garageKey + '\'' +
                ", number='" + number + '\'' +
                ", availableSpaces=" + availableSpaces +
                ", totalSpaces=" + totalSpaces +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", lastUpdated=" + lastUpdated +
                " }";
    }
}
