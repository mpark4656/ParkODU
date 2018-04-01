package edu.odu.cs.gold.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.UUID;
import java.util.List;

public class Event implements Serializable{

    private String eventKey;
    private String eventName;
    private String eventMessage;
    private String eventDateTime;
    private String scheduledDateTime;
    private List<String> eventTags;
    private List<String> locationsAffected;

    public Event() {
        this.eventKey = UUID.randomUUID().toString();
        this.eventDateTime = DateTime.now().toString();
    }

    public Event(String eventKey,
                 String eventName,
                 String eventMessage,
                 String scheduledDateTime,
                 List<String> locationsAffected,
                 List<String> eventTags) {
        this.eventKey = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventDateTime = DateTime.now().toString();
        this.scheduledDateTime = scheduledDateTime;
        this.locationsAffected = locationsAffected;
        this.eventTags = eventTags;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(String scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public List<String> getEventTags() {
        return eventTags;
    }

    public void setEventTags(List<String> eventTags) {
        this.eventTags = eventTags;
    }

    public List<String> getLocationsAffected() {
        return locationsAffected;
    }

    public void setLocationsAffected(List<String> locationsAffected) {
        this.locationsAffected = locationsAffected;
    }

    @Override
    public String toString() {
        return "Event{" +
                " eventKey='" + eventKey + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventMessage='" + eventMessage + '\'' +
                ", eventDateTime=" + eventDateTime + '\'' +
                ", scheduledDateTime=" + scheduledDateTime + '\'' +
                ", eventTags='" + eventTags + '\'' +
                ", locationsAffected=" + locationsAffected +
                " }";
    }
}
