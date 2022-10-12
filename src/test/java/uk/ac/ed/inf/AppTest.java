package uk.ac.ed.inf;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testWindingNumberisLeftOnLine(){

        JSONPoint point1 = new JSONPoint();
        JSONPoint point2 = new JSONPoint();
        LngLat point3 = new LngLat(-0.1,0.1);

        point1.longitude = -0.2;
        point1.latitude = 0.1;
        point2.longitude = 0;
        point2.latitude = 0.1;

        double number = WindingNumber.isLeft(point1,point2,point3);

        assertTrue(number == 0);

    }

    @Test
    public void testWindingNumberisLeftToLeft(){

        JSONPoint point1 = new JSONPoint();
        JSONPoint point2 = new JSONPoint();
        LngLat point3 = new LngLat(-0.1,0.3);

        point1.longitude = -0.2;
        point1.latitude = 0.1;
        point2.longitude = 0;
        point2.latitude = 0.2;

        double number = WindingNumber.isLeft(point1,point2,point3);

        assertTrue(number > 0);

    }

    @Test
    public void testWindingNumberisLeftToRight(){

        JSONPoint point1 = new JSONPoint();
        JSONPoint point2 = new JSONPoint();
        LngLat point3 = new LngLat(-0.1,0.1);

        point1.longitude = -0.2;
        point1.latitude = 0.1;
        point2.longitude = 0;
        point2.latitude = 0.5;

        double number = WindingNumber.isLeft(point1,point2,point3);

        assertTrue(number < 0);

    }

    @Test
    public void testLngLatInCentralAreaTrue(){

        LngLat centralPoint = new LngLat(-3.185,55.944);

        assertTrue(centralPoint.inCentralArea());

    }

    @Test
    public void testLngLatInCentralAreaFalse(){

        LngLat outsidePoint = new LngLat(-4, 3000);

        assertFalse(outsidePoint.inCentralArea());

    }

    @Test
    public void testLngLatInCentralAreaOnBoundary(){

        LngLat boundaryPoint = new LngLat(-3.192473,55.946233);

        assertTrue(boundaryPoint.inCentralArea());

    }

    @Test
    public void testRestaurantGetRestaurants() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);

       // System.out.println(things[2].getMenu()[1].getName());

    }
}
