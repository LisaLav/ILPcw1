package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.algorithms.WindingNumber;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.exceptions.InvalidPizzaCombinationException;
import uk.ac.ed.inf.records.LngLat;
import uk.ac.ed.inf.records.Restaurant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
    public void testLngLatNextPositionFlyEast(){

        LngLat centralPoint = new LngLat(-3.185,55.944);

        LngLat newPoint = centralPoint.nextPosition(CompassDirection.E);

        assertEquals(-3.18485, newPoint.longitude(), 0.000000000001);

    }

    @Test
    public void testLngLatNextPositionHover(){

        LngLat centralPoint = new LngLat(-3.185,55.944);

        LngLat newPoint = centralPoint.nextPosition(null);

        assertEquals(-3.185, newPoint.longitude(), 0.000000000001);

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
    public void testLngLatInCentralAreaCornerPoint(){

        LngLat boundaryPoint = new LngLat(-3.192473,55.946233);

        assertTrue(boundaryPoint.inCentralArea());

    }

    @Test
    public void testRestaurantGetRestaurants() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);

       // System.out.println(things[2].getMenu()[1].getName());

    }

    /*@Test
    public void testSetUpMenuItems() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);

        Order.setUpMenuItems(things);

    }*/

    @Test
    public void testGetDeliveryCost() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        order.add("Meat Lover");
        order.add("Vegan Delight");

        int cost = Order.getDeliveryCost(things,order);

        assertEquals(2600,cost);

    }

    @Test
    public void testGetDeliveryCostOther() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        order.add("Meat Lover");
        order.add("Vegan Delight");
        order.add("Meat Lover");
        order.add("Meat Lover");

        int cost = Order.getDeliveryCost(things,order);

        assertEquals(5400, cost);

    }

    @Test (expected = InvalidPizzaCombinationException.class)
    public void testGetDeliveryCostNullOrder() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = null;

        int cost = Order.getDeliveryCost(things,order);

    }

    @Test (expected = InvalidPizzaCombinationException.class)
    public void testGetDeliveryCostWrongOrder() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        order.add("i dont exist");

        int cost = Order.getDeliveryCost(things,order);

    }

    @Test (expected = InvalidPizzaCombinationException.class)
    public void testGetDeliveryCostOrderBig() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        order.add("Super Cheese");
        order.add("Super Cheese");
        order.add("Super Cheese");
        order.add("Super Cheese");
        order.add("Super Cheese");

        int cost = Order.getDeliveryCost(things,order);

    }

    @Test (expected = InvalidPizzaCombinationException.class)
    public void testGetDeliveryCostOrderSmall() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        int cost = Order.getDeliveryCost(things,order);

        System.out.println(order.isEmpty());

    }

    @Test (expected = InvalidPizzaCombinationException.class)
    public void testGetDeliveryCostMultipleRestaurants() throws MalformedURLException {

        URL url = new URL("https://ilp-rest.azurewebsites.net/" + "restaurants");

        Restaurant[] things = Restaurant.getRestaurantsFromRestServer(url);
        ArrayList<String> order = new ArrayList<String>();

        order.add("Super Cheese");
        order.add("Calzone");

        int cost = Order.getDeliveryCost(things,order);

    }

}
