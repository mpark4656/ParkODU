package edu.odu.cs.gold.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.UUID;
import java.util.List;

public class Event implements Serializable{

    private String eventKey;
    private String eventName;
    private String eventMessage;
    private String eventUpdatedDateTime;
    private String eventStartDateTime;
    private String eventEndDateTime;
    private List<String> eventTags;
    private List<String> locationsAffected;

    public Event() {
        this.eventKey = UUID.randomUUID().toString();
        this.eventUpdatedDateTime = DateTime.now().toString();
    }

    public Event(String eventKey,
                 String eventName,
                 String eventMessage,
                 String eventStartDateTime,
                 String eventEndDateTime,
                 List<String> locationsAffected,
                 List<String> eventTags) {
        this.eventKey = eventKey;
        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventUpdatedDateTime = DateTime.now().toString();
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
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

    public String getEventUpdatedDateTime() {
        return eventUpdatedDateTime;
    }

    public void setEventUpdatedDateTime(String eventCreatedDateTime) {
        this.eventUpdatedDateTime = eventCreatedDateTime;
    }

    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    public void setEventEndDateTime(String eventEndDateTime) {
        this.eventEndDateTime = eventEndDateTime;
    }

    public String getEventEndDateTime() {
        return eventEndDateTime;
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

    public DateTime getEventStartTimeDateTime() {
        return DateTime.parse(eventStartDateTime);
    }

    @Override
    public String toString() {
        return "Event{" +
                " eventKey='" + eventKey + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventMessage='" + eventMessage + '\'' +
                ", eventUpdatedDateTime=" + eventUpdatedDateTime + '\'' +
                ", eventStartDateTime=" + eventStartDateTime + '\'' +
                ", eventEndDateTime=" + eventEndDateTime + '\'' +
                ", eventEndDateTime=" + eventEndDateTime + '\'' +
                ", eventTags='" + eventTags + '\'' +
                ", locationsAffected=" + locationsAffected +
                " }";
    }
}
