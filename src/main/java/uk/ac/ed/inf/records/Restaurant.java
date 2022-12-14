package uk.ac.ed.inf.records;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.jsons.Menu;

import java.net.URL;

/**
 * The Restaurant class that represents a single restaurant that the customer can order pizzas from
 * @param name name of the restaurant
 * @param longitude longitude of the restaurant's position
 * @param latitude latitude of the restaurant's position
 * @param menu the restaurant's list of menu items available to order
 */
public record Restaurant(String name, double longitude, double latitude, Menu[] menu) {

    /**
     * It returns the menu (somewhat obsolete as this is a Record class)
     * @return the menu object
     */
    public Menu[] getMenu(){ return menu; }

    /**
     * A factory method that will obtain all the participating restaurants from the REST server
     * @param url the url that has the list of restaurants
     * @return the list of participating restaurants
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL url){

        ObjectMapper mapper = new ObjectMapper();
        Restaurant[] restaurants = null;

        //obtain restaurants
        try{
            restaurants = mapper.readValue(url, Restaurant[].class);
        } catch(Exception e){
            System.err.println("Error in getRestaurantsFromRestServer, Restaurant.java");
        }

        return restaurants;

    }

    /**
     * Returns the longitude of a restaurant
     * @return the longitude
     */
    public double getLongitude(){ return this.longitude; }

    /**
     * Returns the latitude of a restaurant
     * @return the latitude
     */
    public double getLatitude(){ return this.latitude; }

}
