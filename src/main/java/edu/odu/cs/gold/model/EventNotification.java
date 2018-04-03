package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class EventNotification {

    //MARK: - Properties
    private String title;
    private String location;
    private String eventKey;
    private Date startDate;
    private Date endDate;
    private String tag;

    //MARK: - Initialzation
    public EventNotification() {
        eventKey = UUID.randomUUID().toString();
    }

    //MARK: - Setters and Getters
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String newLocation) {
        this.location = newLocation;
    }

    public String getEventKey() {
        return this.eventKey;
    }

    public void setStartDate(Date newStartDate) {
        this.startDate = newStartDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setEndDate(Date newEndDate) {
        this.endDate = newEndDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public String toString() {
        return "EventNotification{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", eventKey=" + eventKey +
                ", startDate=" + startDate +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}

