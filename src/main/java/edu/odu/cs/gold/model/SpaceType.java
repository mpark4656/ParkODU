package edu.odu.cs.gold.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * These are the space types normally available in ODU parking facilities
 *
 * Metered
 * Disability
 * Metered Disability
 * Reserved
 * Visitor Only
 * Motorcycle
 */
public class SpaceType implements Serializable {
    private String spaceTypeKey;
    private String name;
    private String description;

    public SpaceType() {
        this.spaceTypeKey = UUID.randomUUID().toString();
    }

    public SpaceType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getSpaceTypeKey() {
        return spaceTypeKey;
    }

    public void setSpaceTypeKey(String permitTypeKey) {
        this.spaceTypeKey = permitTypeKey;
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
        return "SpaceType{" +
                "spaceTypeKey='" + spaceTypeKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
