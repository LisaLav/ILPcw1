package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Menu represents a single menu item that a Restaurant has available
 * It also helps with converting JSONs into Menu objects
 */
public class Menu {

    /**
     * Menu item name
     */
    @JsonProperty("name")
    public String name;

    /**
     * Menu item price in pennies
     */
    @JsonProperty("priceInPence")
    public int priceInPennies;

    /**
     * Get name of menu item
     * @return name of menu item
     */
    public String getName(){ return name; }

}
