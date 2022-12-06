package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.algorithms.AStar;
import uk.ac.ed.inf.enums.OrderOutcome;
import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.jsons.NoFlyZoneJSON;
import uk.ac.ed.inf.jsons.OrderStructure;
import uk.ac.ed.inf.records.LngLat;
import uk.ac.ed.inf.records.Restaurant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * This controls the movement of the drone according to a day's orders
 */
public class Drone {

    final static LngLat appletonTower = new LngLat(-3.186874, 55.944494);
    private static OrderStructure[] orders;
    private static HashMap<String,OrderOutcome> dayOrderOutcomes;
    private static HashMap<String,LngLat[]> dayOrderMoves;
    private static LngLat currentPosition;
    private static NoFlyZoneJSON[] noFlyZones;
    private static JSONPoint[] centralArea;

    /**
     * getCentralAreaPoints connects to the REST server and obtains the coordinates of the up-to-date central area
     * @param url the base url for the REST server
     * @return the coordinates that create the central area zone as a JSONPoint array
     */
    private static JSONPoint[] setCentralAreaPoints(String url){

        ObjectMapper mapper = new ObjectMapper();
        JSONPoint[] centralAreaPoints = null;

        //obtaining the central area points here
        try{
            URL jsonUrl = new URL(url + "centralArea");
            centralAreaPoints = mapper.readValue(jsonUrl, JSONPoint[].class);

        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return centralAreaPoints;

    }

    private static NoFlyZoneJSON[] setNoFlyZones(String url) {

        ObjectMapper mapper = new ObjectMapper();
        NoFlyZoneJSON[] noFlyZones = null;

        try {
            URL jsonUrl = new URL(url + "noFlyZones");
            noFlyZones = mapper.readValue(jsonUrl, NoFlyZoneJSON[].class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noFlyZones;

    }

    private static LngLat[] calculateMoves(OrderStructure order){

        LngLat[] moves = null;
        //get the restaurant the order is going to
        Restaurant restaurant = Order.getPizzaRestaurant(order.orderItems[0]);

       // LngLat restaurantLocation = new LngLat(restaurant.longitude, restaurant.latitude);

        //moves = AStar.astar(restaurant, currentPosition);

        return moves;

    }

    public static void StartDay(String date) throws MalformedURLException {

        //orders = Order.getDayOrders(date);
        centralArea = setCentralAreaPoints(RESTUrl.getUrl());
        noFlyZones = setNoFlyZones(RESTUrl.getUrl());
        currentPosition = appletonTower;
        OrderStructure currentOrder = null;
        OrderOutcome currentOrderOutcome = null;
        LngLat[] currentMoves = null;

        /*for (int i = 0; i < orders.length; i++){

            currentOrder = orders[i];
            currentOrderOutcome = Order.validateOrder(currentOrder);

            //if the order outcome isn't valid then we put it in the file and move to the next order
            if (currentOrderOutcome != OrderOutcome.ValidButNotDelivered){

                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                continue;

            }

            currentMoves = calculateMoves(currentOrder);

            dayOrderMoves.put(currentOrder.orderNo, currentMoves);

        }*/

    }

    public static JSONPoint[] getCentralArea(){ return centralArea; }
    public static NoFlyZoneJSON[] getNoFlyZones(){ return noFlyZones; }

}
