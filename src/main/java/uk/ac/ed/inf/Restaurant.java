package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public record Restaurant(String name, double longitude, double latitude, Menu[] menu) {



    public Menu[] getMenu(){
        return menu;
    }

    public static Restaurant[] getRestaurantsFromRestServer(URL url){

        ObjectMapper mapper = new ObjectMapper();
        Restaurant[] restaurants = null;

        try{
            restaurants = mapper.readValue(url, Restaurant[].class);

        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return restaurants;

    }

}
