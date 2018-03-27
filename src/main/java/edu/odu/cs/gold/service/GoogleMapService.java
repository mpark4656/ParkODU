package edu.odu.cs.gold.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.*;
import edu.odu.cs.gold.model.*;
import edu.odu.cs.gold.repository.BuildingRepository;
import edu.odu.cs.gold.repository.GarageRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.*;

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

    //public enum TravelMode {

    //    DRIVING ("Driving"),
    //    WALKING ("Walking"),
    //    BICYCLING ("Bicycling"),
    //    TRANSIT ("Transit");

    //    private final String travelMode;
    //
    //    TravelMode(String travelMode) {
    //        this.travelMode = travelMode;
    //    }

    //    private String getTravelMode() { return travelMode; }
    //
    //}

    public void buildDirections(String startingLocation,
                                Location destination,
                                TravelMode travelMode) {
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);

        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(startingLocation);
        apiRequest.destination(new LatLng(destination.getLatitude(),destination.getLongitude()));
        apiRequest.mode(TravelMode.DRIVING);

        apiRequest.setCallback(new PendingResult.Callback<DirectionsRoute[]>() {
            @Override
            public void onResult(DirectionsRoute[] result) {
                DirectionsRoute[] routes = result;
                System.out.print(routes.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.print(e.toString());
            }
        });
    }



}
