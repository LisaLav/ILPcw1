package uk.ac.ed.inf.records;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.Drone;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.RESTUrl;
import uk.ac.ed.inf.algorithms.WindingNumber;
import uk.ac.ed.inf.jsons.NoFlyZoneJSON;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * LngLat class represents the coordinates of a point for the drone. Uses (longitude,latitude) for representing the coordinates
 * as to help guide the drone in the real world.
 * @param longitude the longitude of the point in the real world
 * @param latitude the latitude of the point in the real world
 */
public record LngLat(double longitude, double latitude){

    private static double droneMovement = 0.00015;
    private static double distanceTolerance = 0.00015;

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

        JSONPoint[] centralAreaPoints = Drone.getCentralArea();

        //use the WindingNumber class to check if the LngLat is in the central area boundary
        return WindingNumber.isInPolygon(this, centralAreaPoints, centralAreaPoints.length);
    }

    public boolean inNoFlyZone(){

        NoFlyZoneJSON[] noFlyZonePoints = Drone.getNoFlyZones();

        //convert NoFlyZonePoints into JSONPoint[]
        for (NoFlyZoneJSON shape : noFlyZonePoints){

            double[][] coordinates = shape.lnglat;
            JSONPoint[] polygon = new JSONPoint[coordinates.length];

            //setting up the current no-fly zone polygon
            for (int i = 0; i < coordinates.length; i++){

                double[] lnglat = coordinates[i];
                polygon[i] = new JSONPoint();
                polygon[i].setLongitude(lnglat[0]);
                polygon[i].setLatitude(lnglat[1]);

            }

            //if this lnglat is in the polygon then we can stop
            if (WindingNumber.isInPolygon(this, polygon, polygon.length)){
                return true;
            }

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
        if (distanceTo(coordsTo) < distanceTolerance){
            return true;
        }

        return false;

    }

    public LngLat[] getNeighbours(){

        LngLat[] neighbours = new LngLat[16];
        LngLat neighbour = null;

        for (int i = 0; i < neighbours.length; i++){

            neighbour = nextPosition(CompassDirection.values()[i]);
            neighbours[i] = neighbour;

        }

        return neighbours;

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
        double moveDistance = droneMovement;

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

    public double getDroneMovement(){ return droneMovement; }

}
