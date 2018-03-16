package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * These are the current permits issued by ODU Transportation and Parking as of 3/15/2018
 *
 * Commuter
 * Evening
 * Faculty/Staff
 * Perimeter
 */
public class PermitType implements Serializable {
    private String permitTypeKey;
    private String name;
    private String description;

    public PermitType() {
        this.permitTypeKey = UUID.randomUUID().toString();
    }

    public PermitType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getPermitTypeKey() {
        return permitTypeKey;
    }

    public void setPermitTypeKey(String permitTypeKey) {
        this.permitTypeKey = permitTypeKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PermitType{" +
                "permitTypeKey='" + permitTypeKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
