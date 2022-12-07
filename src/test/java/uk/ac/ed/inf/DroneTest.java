package uk.ac.ed.inf;

import org.junit.Test;

import java.net.MalformedURLException;

public class DroneTest {

    @Test
    public void testDroneStartDay() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-04-30");

    }

}
