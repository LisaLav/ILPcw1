package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Restaurant {

    private String name;
    private double longitude;
    private double latitude;
    private Menu[] menu;

    public Menu[] getMenu(){
        return menu;
    }

}
