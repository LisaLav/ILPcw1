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
import java.util.*;

/**
 * This controls the movement of the drone according to a day's orders
 */
public class Drone {

    final static LngLat appletonTower = new LngLat(-3.186874, 55.944494);
    private static int battery = 2000;
    private static OrderStructure[] orders;
    private static HashMap<String,OrderOutcome> dayOrderOutcomes = new HashMap<>();
    private static HashMap<String,ArrayList<LngLat>> dayOrderMoves = new HashMap<>();
    private static LngLat currentPosition;
    private static NoFlyZoneJSON[] noFlyZones;
    private static JSONPoint[] centralArea;
    //stores the paths already calculated to speed up processing time
    private static ArrayList<ArrayList<LngLat>> calculatedPaths = new ArrayList<>();

    /**
     * setCentralAreaPoints connects to the REST server and obtains the coordinates of the up-to-date central area
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

    private static ArrayList<LngLat> calculateMoves(OrderStructure order){

        ArrayList<LngLat> moves = null;
        //get the restaurant the order is going to
        Restaurant restaurant = Order.getPizzaRestaurant(order.orderItems[0]);

       LngLat restaurantLocation = new LngLat(restaurant.getLongitude(), restaurant.getLatitude());

        moves = AStar.astar(currentPosition, restaurantLocation);

        return moves;

    }

    private static void orderOrdersByDistance(){

        List<OrderStructure> orderedOrders = new ArrayList<>();

        //get all the restaurants for the current day
        for (OrderStructure order : orders){
            orderedOrders.add(order);
        }

        //order the orders by the distance from appleton tower to the restaurant
        orderedOrders.sort(Comparator.comparingDouble(o -> appletonTower.distanceTo(new LngLat(Order.getPizzaRestaurant(o.orderItems[0]).getLongitude(), Order.getPizzaRestaurant(o.orderItems[0]).getLatitude()))));

        for (int i = 0; i < orders.length; i++){
            orders[i] = orderedOrders.get(i);
        }

    }

    public static void StartDay(String date) throws MalformedURLException {

        orders = Order.getDayOrders(date);
        centralArea = setCentralAreaPoints(RESTUrl.getUrl());
        noFlyZones = setNoFlyZones(RESTUrl.getUrl());
        currentPosition = appletonTower;
        OrderOutcome currentOrderOutcome = null;
        ArrayList<LngLat> currentMoves = null;

        orderOrdersByDistance();
        int i = 0;

        for (OrderStructure currentOrder : orders){

            currentOrderOutcome = Order.validateOrder(currentOrder);

            //if the order outcome isn't valid then we put it in the file and move to the next order, or if the battery has run out then we can't deliver the valid order
            if ((currentOrderOutcome != OrderOutcome.ValidButNotDelivered) || (battery < 0)){

                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                continue;

            }

            currentMoves = calculateMoves(currentOrder);

            //the drone must move to the restaurant and back again
            battery = battery - currentMoves.size()*2;

            //if the current order makes the battery go into negative values, then we have to stop as the battery has run out
            if (battery < 0){
                continue;
            }

            dayOrderMoves.put(currentOrder.orderNo, currentMoves);
            System.out.println(i);
            i++;
        }

    }

    public static JSONPoint[] getCentralArea(){ return centralArea; }
    public static NoFlyZoneJSON[] getNoFlyZones(){ return noFlyZones; }

}
