package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JSONPoint {

    @JsonProperty("name")
    public String name;

    @JsonProperty("longitude")
    public float longitude;

    @JsonProperty("latitude")
    public float latitude;

}
