package edu.odu.cs.gold.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DistanceDuration {

    @JsonProperty("destination_addresses")
    public List<String> destinationAddresses;

    @JsonProperty("origin_addresses")
    public List<String> originAddresses;

    @JsonProperty("rows")
    public List<Row> rows;

    @JsonProperty("status")
    public String status;

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DistanceTime{" +
                "destinationAddresses=" + destinationAddresses +
                ", originAddresses=" + originAddresses +
                ", rows=" + rows +
                ", status='" + status + '\'' +
                '}';
    }
}

class Row {

    @JsonProperty("elements")
    public List<Element> elements;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "Row{" +
                "elements=" + elements +
                '}';
    }
}

class Element {

    @JsonProperty("distance")
    public Distance distance;

    @JsonProperty("duration")
    public Duration duration;

    @JsonProperty("status")
    public String status;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Element{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                '}';
    }
}

class Distance {

    @JsonProperty("text")
    public String text;

    @JsonProperty("value")
    public String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "text='" + text + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

class Duration {

    @JsonProperty("text")
    public String text;

    @JsonProperty("value")
    public String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "text='" + text + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
