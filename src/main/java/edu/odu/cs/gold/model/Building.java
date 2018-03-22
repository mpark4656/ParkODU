package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.UUID;

public class Building implements Serializable {

    private String buildingKey;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;

    public Building() {
        buildingKey = UUID.randomUUID().toString();
    }

    public Building(String name, Double latitude, Double longitude) {
        buildingKey = UUID.randomUUID().toString();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getBuildingKey() {
        return buildingKey;
    }

    public void setBuildingKey(String buildingKey) {
        this.buildingKey = buildingKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String getAddress() {return address;}

    public void setAddress() {this.address = address;}

    public Location getLocation() {
        return new Location(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Building{" +
                "buildingKey='" + buildingKey + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
