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

    public void setLongitude(double longitude){ this.longitude = longitude; }
}
