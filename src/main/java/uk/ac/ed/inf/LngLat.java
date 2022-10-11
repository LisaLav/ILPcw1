package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public record LngLat(double longitude, double latitude) {

    private static JSONPoint[] getCentralAreaPoints(String url){
        ObjectMapper mapper = new ObjectMapper();
        JSONPoint[] centralAreaPoints = null;

        try{
            URL jsonUrl = new URL(url + "centralArea");
            centralAreaPoints = mapper.readValue(jsonUrl, JSONPoint[].class);

        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return centralAreaPoints;
    }

    public LngLat{
        if (longitude >= 0 || latitude <= 0){
            throw new IllegalArgumentException(String.format("Invalid coordinates: longitude %f latitude %f", longitude, latitude));
        }
    }

    public boolean inCentralArea(){

        CentralArea centralArea = CentralArea.getInstance(null);
        JSONPoint[] centralAreaPoints = getCentralAreaPoints(centralArea.getUrl());

        if (WindingNumber.isInPolygon(this, centralAreaPoints, centralAreaPoints.length)){
            return true;
        }

        return false;
    }

    public double distanceTo(LngLat coordsTo){
        double pythagoreanDistance = Math.sqrt(Math.pow((longitude - coordsTo.longitude),2) + Math.pow((latitude - coordsTo.latitude),2));
        return pythagoreanDistance;
    }

    public boolean closeTo(LngLat coordsTo){
        if (distanceTo(coordsTo) < 0.00015){
            return true;
        }
        return false;
    }

    public LngLat nextPosition(CompassDirection direction){

        LngLat newPosition;
        double degree;
        double moveDistance = 0.00015;

        //hover motion
        if (direction == null){
            newPosition = this;
        } else{

            degree = direction.getDegree();
            double newLatitude = Math.sin(degree)*moveDistance;
            double newLongitude = Math.cos(degree)*moveDistance;

            newPosition = new LngLat(newLongitude,newLatitude);

        }

        return newPosition;

    }

}
