package uk.ac.ed.inf.records;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.RESTUrl;
import uk.ac.ed.inf.algorithms.WindingNumber;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * LngLat class represents the coordinates of a point for the drone. Uses (longitude,latitude) for representing the coordinates
 * as to help guide the drone in the real world.
 * @param longitude the longitude of the point in the real world
 * @param latitude the latitude of the point in the real world
 */
public record LngLat(double longitude, double latitude) {

    /**
     * getCentralAreaPoints connects to the REST server and obtains the coordinates of the up-to-date central area
     * @param url the base url for the REST server
     * @return the coordinates that create the central area zone as a JSONPoint array
     */
    private static JSONPoint[] getCentralAreaPoints(String url){

        ObjectMapper mapper = new ObjectMapper();
        JSONPoint[] centralAreaPoints = null;

        //obtaining the central area points here
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

    /**
     * This is here to prevent any coordinates being made that aren't in the rough Edinburgh area
     * @param longitude longitude of coordinate
     * @param latitude latitude of coordinate
     * @throws IllegalArgumentException if either parameter is too far away from Edinburgh
     */
    public LngLat{

        if (longitude >= 0 || latitude <= 0){
            throw new IllegalArgumentException(String.format("Invalid coordinates: longitude %f latitude %f", longitude, latitude));
        }

    }

    /**
     * inCentralArea checks if this LngLat object is in the central area boundary
     * @return if LngLat is in central area
     */
    public boolean inCentralArea(){

        //temporarily have the url = null as there is no base url at the moment
        RESTUrl restUrl = RESTUrl.getInstance(null);
        JSONPoint[] centralAreaPoints = getCentralAreaPoints(restUrl.getUrl());

        //use the WindingNumber class to check if the LngLat is in the central area boundary
        if (WindingNumber.isInPolygon(this, centralAreaPoints, centralAreaPoints.length)){
            return true;
        }

        return false;
    }

    /**
     * This method calculates the distance from the current LngLat to the LngLat that is passed in
     * @param coordsTo is the LngLat that we want to calculate the distance to
     * @return the distance between the two points
     */
    public double distanceTo(LngLat coordsTo){

        //calculate using Pythagoras
        double pythagoreanDistance = Math.sqrt(Math.pow((longitude - coordsTo.longitude),2) + Math.pow((latitude - coordsTo.latitude),2));
        return pythagoreanDistance;

    }

    /**
     * It checks if this LngLat's distance from the LngLat passed in is beneath a threshold (0.00015)
     * @param coordsTo the LngLat to check if this LngLat is close to it
     * @return whether this LngLat is close to that point
     */
    public boolean closeTo(LngLat coordsTo){

        //calculate using distanceTo
        if (distanceTo(coordsTo) < 0.00015){
            return true;
        }

        return false;

    }

    /**
     * This method calculates the new position of this LngLat using trigonometry with the
     * passed in angle
     * @param direction the direction the LngLat wants to move, which is also the angle
     * @return a new LngLat object with the updated coordinates
     */
    public LngLat nextPosition(CompassDirection direction){

        LngLat newPosition;
        double degree;
        double moveDistance = 0.00015;

        //hover motion
        if (direction == null){
            newPosition = this;
        } else{

            degree = direction.getDegree();
            //trigonometry part (latitude = opposite, longitude = adjacent side of triangle)
            double newLatitude = latitude + Math.sin(degree)*moveDistance;
            double newLongitude = longitude + Math.cos(degree)*moveDistance;

            newPosition = new LngLat(newLongitude,newLatitude);

        }

        return newPosition;

    }

}
