package uk.ac.ed.inf.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSONPoint exists to help convert JSON coordinates obtained from the REST server
 * into POJOs using Jackson
 */
public class JSONPoint {

    /**
     * Name of the coordinate
     */
    @JsonProperty("name")
    public String name;

    /**
     * Longitude of coordinate
     */
    @JsonProperty("longitude")
    public double longitude;

    /**
     * Latitude of coordinate
     */
    @JsonProperty("latitude")
    public double latitude;

    /**
     * Set the longitude of the JSONPoint
     * @param longitude the longitude to set to
     */
    public void setLongitude(double longitude){ this.longitude = longitude; }

    /**
     * Set the latitude of the JSONPoint
     * @param latitude the latitude to set to
     */
    public void setLatitude(double latitude){ this.latitude = latitude; }
}
