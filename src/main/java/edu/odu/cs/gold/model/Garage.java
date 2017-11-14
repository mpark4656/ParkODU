package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Garage implements Serializable {

    private String garageKey;
    private String name;
    private String description;
    private String heightDescription;
    private String addressOne;
    private String addressTwo;
    private String city;
    private String state;
    private String zipCode;
    private Integer availableSpaces;
    private Integer totalSpaces;
    private Double capacity;
    private Double latitude;
    private Double longitude;
    private Date lastUpdated;

    public Garage() {
        this.garageKey = UUID.randomUUID().toString();
    }

    public Garage(String name, Double latitude, Double longitude) {
        this.garageKey = UUID.randomUUID().toString();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getGarageKey() {
        return garageKey;
    }

    public void setGarageKey(String garageKey) {
        this.garageKey = garageKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeightDescription() {
        return heightDescription;
    }

    public void setHeightDescription(String heightDescription) {
        this.heightDescription = heightDescription;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public Double getCapacity() {
        return capacity;
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

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Location getLocation() {
        return new Location(garageKey, name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Garage{" +
                "garageKey='" + garageKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", heightDescription='" + heightDescription + '\'' +
                ", addressOne='" + addressOne + '\'' +
                ", addressTwo='" + addressTwo + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", availableSpaces=" + availableSpaces +
                ", totalSpaces=" + totalSpaces +
                ", capacity=" + capacity +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
