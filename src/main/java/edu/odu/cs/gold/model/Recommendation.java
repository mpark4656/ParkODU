package edu.odu.cs.gold.model;

import java.util.Date;

// Transient POJO built dynamically
public class Recommendation {

    private String startingAddress;
    private Garage garage;
    private Building destinationBuilding;

    private Integer availabilityCount;
    private Integer totalCount;

    private String startingAddressToGarageDistanceText;
    private Integer startingAddressToGarageDistanceValue;
    private String startingAddressToGarageDurationText;
    private Integer startingAddressToGarageDurationValue;

    private String garageToDestinationBuildingDistanceText;
    private Integer garageToDestinationBuildingDistanceValue;
    private String garageToDestinationBuildingDurationText;
    private Integer garageToDestinationBuildingDurationValue;

    private String totalDistanceText;
    private Integer totalDistanceValue;
    private String totalDurationText;
    private Integer totalDurationValue;

    private Date arrivalTime;

    public String getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public Building getDestinationBuilding() {
        return destinationBuilding;
    }

    public void setDestinationBuilding(Building destinationBuilding) {
        this.destinationBuilding = destinationBuilding;
    }

    public Integer getAvailabilityCount() {
        return availabilityCount;
    }

    public void setAvailabilityCount(Integer availabilityCount) {
        this.availabilityCount = availabilityCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getStartingAddressToGarageDistanceText() {
        return (Math.round(startingAddressToGarageDistanceValue * 0.000621371 * 10.0) / 10.0) + " mi";
    }

    public void setStartingAddressToGarageDistanceText(String startingAddressToGarageDistanceText) {
        this.startingAddressToGarageDistanceText = startingAddressToGarageDistanceText;
    }

    public Integer getStartingAddressToGarageDistanceValue() {
        return startingAddressToGarageDistanceValue;
    }

    public void setStartingAddressToGarageDistanceValue(Integer startingAddressToGarageDistanceValue) {
        this.startingAddressToGarageDistanceValue = startingAddressToGarageDistanceValue;
    }

    public String getStartingAddressToGarageDurationText() {
        return (Math.round(startingAddressToGarageDurationValue / 60.0 * 10.0) / 10.0) + " min";
    }

    public void setStartingAddressToGarageDurationText(String startingAddressToGarageDurationText) {
        this.startingAddressToGarageDurationText = startingAddressToGarageDurationText;
    }

    public Integer getStartingAddressToGarageDurationValue() {
        return startingAddressToGarageDurationValue;
    }

    public void setStartingAddressToGarageDurationValue(Integer startingAddressToGarageDurationValue) {
        this.startingAddressToGarageDurationValue = startingAddressToGarageDurationValue;
    }

    public String getGarageToDestinationBuildingDistanceText() {
        return (Math.round(garageToDestinationBuildingDistanceValue * 0.000621371 * 10.0) / 10.0) + " mi";
    }

    public void setGarageToDestinationBuildingDistanceText(String garageToDestinationBuildingDistanceText) {
        this.garageToDestinationBuildingDistanceText = garageToDestinationBuildingDistanceText;
    }

    public Integer getGarageToDestinationBuildingDistanceValue() {
        return garageToDestinationBuildingDistanceValue;
    }

    public void setGarageToDestinationBuildingDistanceValue(Integer garageToDestinationBuildingDistanceValue) {
        this.garageToDestinationBuildingDistanceValue = garageToDestinationBuildingDistanceValue;
    }

    public String getGarageToDestinationBuildingDurationText() {
        return (Math.round(garageToDestinationBuildingDurationValue / 60.0 * 10.0) / 10.0) + " min";
    }

    public void setGarageToDestinationBuildingDurationText(String garageToDestinationBuildingDurationText) {
        this.garageToDestinationBuildingDurationText = garageToDestinationBuildingDurationText;
    }

    public Integer getGarageToDestinationBuildingDurationValue() {
        return garageToDestinationBuildingDurationValue;
    }

    public void setGarageToDestinationBuildingDurationValue(Integer garageToDestinationBuildingDurationValue) {
        this.garageToDestinationBuildingDurationValue = garageToDestinationBuildingDurationValue;
    }

    public String getTotalDistanceText() {
        return (Math.round(totalDistanceValue * 0.000621371 * 10.0) / 10.0) + " mi";
    }

    public void setTotalDistanceText() {
        this.totalDistanceText = (totalDistanceValue * 0.000621371192) + " miles.";
    }

    public Integer getTotalDistanceValue() {
        return totalDistanceValue;
    }

    public void setTotalDistanceValue(Integer totalDistanceValue) {
        this.totalDistanceValue = totalDistanceValue;
    }

    public String getTotalDurationText() {
        return (Math.round(totalDurationValue / 60.0 * 10.0) / 10.0) + " min";
    }

    public void setTotalDurationText() {
        this.totalDurationText = (totalDurationValue / 60) + " minutes.";
    }

    public Integer getTotalDurationValue() {
        return totalDurationValue;
    }

    public void setTotalDurationValue(Integer totalDurationValue) {
        this.totalDurationValue = totalDurationValue;
    }

    public void setStartingAddressToGarage(DistanceDuration distanceDuration) {
        this.startingAddressToGarageDistanceText = distanceDuration.rows.get(0).elements.get(0).distance.text;
        this.startingAddressToGarageDistanceValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).distance.value);
        this.startingAddressToGarageDurationText = distanceDuration.rows.get(0).elements.get(0).duration.text;
        this.startingAddressToGarageDurationValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).duration.value);
    }

    public void setGarageToDestinationBuilding(DistanceDuration distanceDuration) {
        this.garageToDestinationBuildingDistanceText = distanceDuration.rows.get(0).elements.get(0).distance.text;
        this.garageToDestinationBuildingDistanceValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).distance.value);
        this.garageToDestinationBuildingDurationText = distanceDuration.rows.get(0).elements.get(0).duration.text;
        this.garageToDestinationBuildingDurationValue = Integer.parseInt(distanceDuration.rows.get(0).elements.get(0).duration.value);
    }

    public Date getArrivalTime() {
        return new Date(new Date().getTime() + (totalDurationValue * 1000));
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "startingAddress='" + startingAddress + '\'' +
                ", garage=" + garage +
                ", destinationBuilding=" + destinationBuilding +
                ", availabilityCount=" + availabilityCount +
                ", totalCount=" + totalCount +
                ", startingAddressToGarageDistanceText='" + startingAddressToGarageDistanceText + '\'' +
                ", startingAddressToGarageDistanceValue=" + startingAddressToGarageDistanceValue +
                ", startingAddressToGarageDurationText='" + startingAddressToGarageDurationText + '\'' +
                ", startingAddressToGarageDurationValue=" + startingAddressToGarageDurationValue +
                ", garageToDestinationBuildingDistanceText='" + garageToDestinationBuildingDistanceText + '\'' +
                ", garageToDestinationBuildingDistanceValue=" + garageToDestinationBuildingDistanceValue +
                ", garageToDestinationBuildingDurationText='" + garageToDestinationBuildingDurationText + '\'' +
                ", garageToDestinationBuildingDurationValue=" + garageToDestinationBuildingDurationValue +
                ", totalDistanceText='" + totalDistanceText + '\'' +
                ", totalDistanceValue=" + totalDistanceValue +
                ", totalDurationText='" + totalDurationText + '\'' +
                ", totalDurationValue=" + totalDurationValue +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
