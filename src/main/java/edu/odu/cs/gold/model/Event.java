package edu.odu.cs.gold.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.UUID;
import java.util.Set;

public class Event implements Serializable{

    private String eventKey;
    private String eventName;
    private String eventMessage;
    private DateTime eventDateTime;
    private DateTime scheduledDateTime;
    private Set<String> eventTags;
    private Set<String> locationsEffected;

    public Event() { this.eventKey = UUID.randomUUID().toString(); }

    public Event(String eventKey,
                 String eventName,
                 String eventMessage,
                 DateTime scheduledDateTime) {
        this.eventKey = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.eventMessage = eventMessage;
        this.eventDateTime = DateTime.now();
        this.scheduledDateTime = scheduledDateTime;
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

    public DateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(DateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public DateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(DateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public Set<String> getEventTags() {
        return eventTags;
    }

    public void setEventTags() {
        this.eventTags = eventTags;
    }

    public Set<String> getLocationsEffected() {
        return locationsEffected;
    }

    public void setLocationsEffected(Set<String> locationsEffected) {
        this.locationsEffected = locationsEffected;
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
                ", locationsEffected=" + locationsEffected +
                " }";
    }
}
