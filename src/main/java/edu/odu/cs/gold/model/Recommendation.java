package edu.odu.cs.gold.model;

import com.google.maps.model.DistanceMatrix;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import edu.odu.cs.gold.repository.RecommendationRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

// Transient POJO built dynamically
public class Recommendation implements Serializable{

    private String startingAddress;
    private Garage garage;
    private Building destinationBuilding;

    private Integer availabilityCount;
    private Integer totalCount;

    private String startingAddressToGarageDistanceText;
    private long startingAddressToGarageDistanceValue;
    private String startingAddressToGarageDurationText;
    private long startingAddressToGarageDurationValue;

    private String garageToDestinationBuildingDistanceText;
    private long garageToDestinationBuildingDistanceValue;
    private String garageToDestinationBuildingDurationText;
    private long garageToDestinationBuildingDurationValue;

    private String totalDistanceText;
    private long totalDistanceValue;
    private String totalDurationText;
    private long totalDurationValue;

    private String recommendationKey;

    private BuildingRepository buildingRepository;
    private GarageRepository garageRepository;

    private Date arrivalTime;

    public String getRecommendationKey() {return recommendationKey;}

    public void setRecommendationKey(String recommendationKey) {this.recommendationKey = recommendationKey;}

    public String getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public void generateRecommendationKey() {
        this.recommendationKey = UUID.randomUUID().toString();
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

    public long getStartingAddressToGarageDistanceValue() {
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

    public long getStartingAddressToGarageDurationValue() {
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

    public long getGarageToDestinationBuildingDistanceValue() {
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

    public long getGarageToDestinationBuildingDurationValue() {
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

    public long getTotalDistanceValue() {
        return totalDistanceValue;
    }

    public void setTotalDistanceValue(long totalDistanceValue) {
        this.totalDistanceValue = totalDistanceValue;
    }

    public String getTotalDurationText() {
        return (Math.round(totalDurationValue / 60.0 * 10.0) / 10.0) + " min";
    }

    public void setTotalDurationText() {
        this.totalDurationText = (totalDurationValue / 60) + " minutes.";
    }

    public long getTotalDurationValue() {
        return totalDurationValue;
    }

    public void setTotalDurationValue(long totalDurationValue) {
        this.totalDurationValue = totalDurationValue;
    }

    public void setStartingAddressToGarage(DistanceMatrix distanceDuration) {
        this.startingAddressToGarageDistanceText = distanceDuration.rows[0].elements[0].distance.humanReadable;
        this.startingAddressToGarageDistanceValue = distanceDuration.rows[0].elements[0].distance.inMeters;
        this.startingAddressToGarageDurationText = distanceDuration.rows[0].elements[0].duration.humanReadable;
        this.startingAddressToGarageDurationValue = distanceDuration.rows[0].elements[0].duration.inSeconds;
    }

    public void setGarageToDestinationBuilding(DistanceMatrix distanceDuration) {
        this.garageToDestinationBuildingDistanceText = distanceDuration.rows[0].elements[0].distance.humanReadable;
        this.garageToDestinationBuildingDistanceValue = distanceDuration.rows[0].elements[0].distance.inMeters;
        this.garageToDestinationBuildingDurationText = distanceDuration.rows[0].elements[0].duration.humanReadable;
        this.garageToDestinationBuildingDurationValue = distanceDuration.rows[0].elements[0].duration.inSeconds;
    }

    public Date getArrivalTime() {
        return new Date(new Date().getTime() + (totalDurationValue * 1000));
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "recommendationKey='" + recommendationKey + '\'' +
                ", startingAddress='" + startingAddress + '\'' +
                ", garageKey='" + garage.getGarageKey() + '\'' +
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
