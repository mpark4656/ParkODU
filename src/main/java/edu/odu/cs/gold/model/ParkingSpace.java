package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ParkingSpace implements Serializable {

    private String parkingSpaceKey;
    private String garageKey;
    private Integer number;
    private boolean available;
    private String floor;
    private String permitType;
    private String permitTypeKey;
    private String spaceType;
    private String spaceTypeKey;
    private Date lastUpdated;

    public ParkingSpace() {
        parkingSpaceKey = UUID.randomUUID().toString();
    }

    public String getParkingSpaceKey() {
        return parkingSpaceKey;
    }

    public void setParkingSpaceKey(String parkingSpaceKey) {
        this.parkingSpaceKey = parkingSpaceKey;
    }

    public String getGarageKey() {
        return garageKey;
    }

    public void setGarageKey(String garageKey) {
        this.garageKey = garageKey;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPermitType() { return permitType; }

    public void setPermitType(String permitType) { this.permitType = permitType; }

    public String getPermitTypeKey() {
        return permitTypeKey;
    }

    public void setPermitTypeKey(String permitTypeKey) {
        this.permitTypeKey = permitTypeKey;
    }

    public String getSpaceType() { return spaceType; }

    public void setSpaceType(String spaceType) { this.spaceType = spaceType; }

    public String getSpaceTypeKey() { return spaceTypeKey; }

    public void setSpaceTypeKey(String spaceTypeKey) { this.spaceTypeKey = spaceTypeKey; }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "ParkingSpace{" +
                "parkingSpaceKey='" + parkingSpaceKey + '\'' +
                ", garageKey='" + garageKey + '\'' +
                ", number=" + number +
                ", available=" + available +
                ", floor='" + floor + '\'' +
                ", permitType='" + permitType + '\'' +
                ", permitTypeKey='" + permitTypeKey + '\'' +
                ", spaceType='" + spaceType + '\'' +
                ", spaceTypeKey='" + spaceTypeKey + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
