package edu.odu.cs.gold.model;

import java.io.Serializable;

public class ParkingSpace implements Serializable {

    private String parkingSpaceKey;
    private String garageKey;
    private int parkingSpaceNumber;
    private boolean available;
    private String floor;

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

    public int getParkingSpaceNumber() {
        return parkingSpaceNumber;
    }

    public void setParkingSpaceNumber(int parkingSpaceNumber) {
        this.parkingSpaceNumber = parkingSpaceNumber;
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

    @Override
    public String toString() {
        return "ParkingSpace{" +
                "parkingSpaceKey='" + parkingSpaceKey + '\'' +
                ", garageKey='" + garageKey + '\'' +
                ", parkingSpaceNumber=" + parkingSpaceNumber +
                ", available=" + available +
                ", floor='" + floor + '\'' +
                '}';
    }
}
