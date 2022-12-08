package uk.ac.ed.inf;

import org.junit.Test;

import java.net.MalformedURLException;

public class DroneTest {

    @Test
    public void testDroneStartDay1() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-01-15");

    }

    @Test
    public void testDroneStartDay2() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-01-30");

    }

    @Test
    public void testDroneStartDay3() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-02-15");

    }

    @Test
    public void testDroneStartDay4() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-02-28");

    }

    @Test
    public void testDroneStartDay5() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-03-20");

    }

    @Test
    public void testDroneStartDay6() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-03-30");

    }

    @Test
    public void testDroneStartDay7() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-04-15");

    }

    @Test
    public void testDroneStartDay8() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-04-30");

    }

    @Test
    public void testDroneStartDay9() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-05-15");

    }

    @Test
    public void testDroneStartDay10() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-05-30");

    }

    @Test
    public void testDroneStartDay11() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-03-15");

    }

    @Test
    public void testDroneStartDay12() throws MalformedURLException {

        RESTUrl.getInstance("https://ilp-rest.azurewebsites.net/");
        Drone.StartDay("2023-01-27");

    }

}
