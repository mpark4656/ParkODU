package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.UUID;

public class TravelDistanceDuration implements Serializable {

    private String travelDistanceDurationKey;
    private String garageKey;
    private String buildingKey;
    private String distanceText;
    private Integer distanceValue;
    private String durationText;
    private Integer durationValue;
    private String travelMode;

    private transient String garageName;
    private transient Integer totalSpaces;
    private transient Integer availableSpaces;
    private transient Double capacity;

    public TravelDistanceDuration() {
        this.travelDistanceDurationKey = UUID.randomUUID().toString();
    }

    public TravelDistanceDuration(String garageKey,
                                  String buildingKey,
                                  DistanceDuration distanceDuration,
                                  String travelMode) {
        this.travelDistanceDurationKey = UUID.randomUUID().toString();
        this.garageKey = garageKey;
        this.buildingKey = buildingKey;
        this.distanceText = distanceDuration.rows.get(0).elements.get(0).distance.text;
        this.distanceValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).distance.value);
        this.durationText = distanceDuration.rows.get(0).elements.get(0).duration.text;
        this.durationValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).duration.value);
        this.travelMode = travelMode;
    }

    public TravelDistanceDuration(String garageKey,
                                  String buildingKey,
                                  String distanceText,
                                  Integer distanceValue,
                                  String durationText,
                                  Integer durationValue) {
        this.travelDistanceDurationKey = UUID.randomUUID().toString();
        this.garageKey = garageKey;
        this.buildingKey = buildingKey;
        this.distanceText = distanceText;
        this.distanceValue = distanceValue;
        this.durationText = durationText;
        this.durationValue = durationValue;
    }

    public String getTravelDistanceDurationKey() {
        return travelDistanceDurationKey;
    }

    public void setTravelDistanceDurationKey(String travelDistanceDurationKey) {
        this.travelDistanceDurationKey = travelDistanceDurationKey;
    }

    public String getGarageKey() {
        return garageKey;
    }

    public void setGarageKey(String garageKey) {
        this.garageKey = garageKey;
    }

    public String getBuildingKey() {
        return buildingKey;
    }

    public void setBuildingKey(String buildingKey) {
        this.buildingKey = buildingKey;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public Integer getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(Integer distanceValue) {
        this.distanceValue = distanceValue;
    }

    public String getDurationText() {
        if (durationValue < 3600) {
            return (Math.round(durationValue / 60.00 * 100.00) / 100.00) + " mins";
        }
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public Integer getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(Integer durationValue) {
        this.durationValue = durationValue;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    @Override
    public String toString() {
        return "TravelDistanceDuration{" +
                "travelDistanceDurationKey='" + travelDistanceDurationKey + '\'' +
                ", garageKey='" + garageKey + '\'' +
                ", buildingKey='" + buildingKey + '\'' +
                ", distanceText='" + distanceText + '\'' +
                ", distanceValue=" + distanceValue +
                ", durationText='" + durationText + '\'' +
                ", durationValue=" + durationValue +
                ", travelMode='" + travelMode + '\'' +
                '}';
    }

    // Transient Values

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
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

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
}
