package edu.odu.cs.gold.model;

import java.util.UUID;

public class Location {

    private String key;
    private String name;
    private Double latitude;
    private Double longitude;

    public Location() {
        key = UUID.randomUUID().toString();
    }

    public Location(String name, Double latitude, Double longitude) {
        this.key = UUID.randomUUID().toString();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String key, String name, Double latitude, Double longitude) {
        this.key = key;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    @Override
    public String toString() {
        return "Location{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
