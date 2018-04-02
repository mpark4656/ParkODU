package edu.odu.cs.gold.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.maps.*;
import com.hazelcast.com.eclipsesource.json.Json;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.apache.tomcat.jni.Time;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import edu.odu.cs.gold.model.Location;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.maps.*;
import com.google.gson.*;
import com.google.maps.model.*;

@Service
public class GoogleMapService {

    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyCK-F1dqhEye7aaQdd9dd69-oMMokv2pVA";

    public DistanceMatrix calculateDistanceDurationWithAddress(Garage garage, String startingAddress,TravelMode travelMode) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                .origins(startingAddress)
                .destinations(garage.getAddress())
                .departureTime(DateTime.now())
                .mode(travelMode)
                .units(Unit.METRIC)
                .awaitIgnoreError();
        return distanceMatrix;
    }

    public Location convertAddressToLatLng(String address) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        GeocodingResult[] results = GeocodingApi.newRequest(context).address(address).awaitIgnoreError();
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        return new Location(results[0].geometry.location.lat,results[0].geometry.location.lng);
    }

    public String convertLatLonToAddress(Location location) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        GeocodingResult[] results = GeocodingApi.newRequest(context)
                .latlng(new LatLng(location.getLatitude(),location
                        .getLongitude())).awaitIgnoreError();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(results[0].formattedAddress);
    }

    public String buildDirectionsWithLatLng(Location startingLocation, Location destinationLocation) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        DirectionsRoute[] route = DirectionsApi.newRequest(context)
                .destination(new LatLng(
                        destinationLocation.getLatitude(),
                        destinationLocation.getLongitude()))
                .origin(new LatLng(
                        startingLocation.getLatitude(),
                        startingLocation.getLongitude()))
                .mode(TravelMode.DRIVING)
                .optimizeWaypoints(true)
                .awaitIgnoreError();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(route[0].waypointOrder);
    }

    public String buildDirectionsWithAddress(String startingAddress, String destinationAddress) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        DirectionsRoute[] route = DirectionsApi.newRequest(context)
                .destination(destinationAddress)
                .origin(startingAddress)
                .mode(TravelMode.DRIVING)
                .optimizeWaypoints(true)
                .awaitIgnoreError();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(route[0].waypointOrder);
    }

    public long addDistance(TravelDistanceDuration walking, TravelDistanceDuration driving) {
        return (walking.getDistanceValue() + driving.getDistanceValue());
    }

    public long addDuration(TravelDistanceDuration walking, TravelDistanceDuration driving) {
        return (walking.getDurationValue() + driving.getDurationValue());
    }
}
