package edu.odu.cs.gold.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs.gold.model.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GoogleMapService {

    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyCK-F1dqhEye7aaQdd9dd69-oMMokv2pVA";

    // Default Travel Mode: WALKING
    public TravelDistanceDuration getTravelDistanceDuration(Garage garage, Building building) {
        DistanceDuration distanceDuration = getDistanceDuration(garage.getLocation(), building.getLocation(), TravelMode.WALKING);
        return new TravelDistanceDuration(garage.getGarageKey(), building.getBuildingKey(), distanceDuration, TravelMode.WALKING.travelMode);
    }

    public TravelDistanceDuration getTravelDistanceDuration(Garage garage, Building building, TravelMode travelMode) {
        DistanceDuration distanceDuration = getDistanceDuration(garage.getLocation(), building.getLocation(), travelMode);
        return new TravelDistanceDuration(garage.getGarageKey(), building.getBuildingKey(), distanceDuration, travelMode.travelMode);
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

    public enum TravelMode {

        DRIVING ("Driving"),
        WALKING ("Walking"),
        BICYCLING ("Bicycling"),
        TRANSIT ("Transit");

        private final String travelMode;

        TravelMode(String travelMode) {
            this.travelMode = travelMode;
        }

        private String getTravelMode() { return travelMode; }

    }
}
