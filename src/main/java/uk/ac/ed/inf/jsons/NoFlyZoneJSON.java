package uk.ac.ed.inf.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This helps converts no-fly zone JSONs into POJOs using Jackson
 */
public class NoFlyZoneJSON {


    /**
     * Name of the coordinate
     */
    @JsonProperty("name")
    public String name;

    /**
     * Coordinates array
     */
    @JsonProperty("coordinates")
    public double[][] lnglat;

}
