package edu.odu.cs.gold.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Directions {

    @JsonProperty("destination_addresses")
    public List<String> destinationAddresses;

    @JsonProperty("origin_addresses")
    public List<String> originAddresses;

    @JsonProperty("rows")
    public List<Row> rows;

    @JsonProperty("status")
    public String status;

}
