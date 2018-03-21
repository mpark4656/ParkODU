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

    // Default Travel Mode: WALKING
    public TravelDistanceDuration getTravelDistanceDuration(Garage garage, Building building) {
        DistanceDuration distanceDuration = getDistanceDuration(garage.getLocation(), building.getLocation(), TravelMode.WALKING);
        return new TravelDistanceDuration(garage.getGarageKey(), building.getBuildingKey(), distanceDuration, "Walking");
    }

    public TravelDistanceDuration getTravelDistanceDuration(Garage garage, Building building, TravelMode travelMode) {
        DistanceDuration distanceDuration = getDistanceDuration(garage.getLocation(), building.getLocation(), travelMode);
        return new TravelDistanceDuration(garage.getGarageKey(), building.getBuildingKey(), distanceDuration, "Walking");
    }

    public DistanceDuration getDistanceDuration(Location origin, Location destination, TravelMode travelMode) {

        String targetURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origin.getLatitude() + "%2c" + origin.getLongitude() + "&destinations=" + destination.getLatitude() + "%2c" + destination.getLongitude() + "&mode=" + travelMode + "&key=" + GOOGLE_MAPS_API_KEY;
        String urlParameters = "";

        HttpURLConnection connection = null;

        try {
            // Create Connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send Request
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(urlParameters);
            dataOutputStream.close();

            // Get Response
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // Map Response to Object
            ObjectMapper mapper = new ObjectMapper();
            DistanceDuration distanceDuration = mapper.readValue(response.toString(), DistanceDuration.class);
            if (distanceDuration == null) {
                return null;
            }
            return distanceDuration;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }



/*
    public TravelDistanceDuration calculateDrivingDistanceDurationWithAddress(Garage garage, String startingAddress) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                .origins(startingAddress)
                .destinations(garage.getAddress())
                .departureTime(DateTime.now())
                .mode(TravelMode.DRIVING)
                .units(Unit.valueOf("minutes"))
                .awaitIgnoreError();
        return new TravelDistanceDuration(garage.getGarageKey(),
                startingAddress,
                distanceMatrix.destinationAddresses,
                distanceMatrix.rows[0],
                distanceMatrix.rows[0].elements,
                distanceMatrix.rows[0].elements.length),
                TravelMode.DRIVING.toString();
    }
*//*
    public TravelDistanceDuration calculateWalkingDistanceDurationWithAddress(Garage garage, Building building) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                .origins(garage.getAddress())
                .destinations(building.getAddress())
                .departureTime(DateTime.now())
                .mode(TravelMode.WALKING)
                .units(Unit.valueOf("minutes"))
                .awaitIgnoreError();
        return new TravelDistanceDuration(garage.getGarageKey(),
                building.getBuildingKey(),
                new DistanceDuration(distanceMatrix.originAddresses,
                        distanceMatrix.destinationAddresses,
                        distanceMatrix.rows[0],
                        distanceMatrix.rows[0].elements,
                        distanceMatrix.rows[0].elements.length),
                        TravelMode.WALKING.toString());
    }
*/
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

    public int addDistance(TravelDistanceDuration walking, TravelDistanceDuration driving) {
        return (walking.getDistanceValue() + driving.getDistanceValue());
    }

    public int addDuration(TravelDistanceDuration walking, TravelDistanceDuration driving) {
        return (walking.getDurationValue() + driving.getDurationValue());
    }
}
