package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.ac.ed.inf.algorithms.AStar;
import uk.ac.ed.inf.algorithms.AStarEntry;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.enums.OrderOutcome;
import uk.ac.ed.inf.jsons.JSONPoint;
import uk.ac.ed.inf.jsons.NoFlyZoneJSON;
import uk.ac.ed.inf.jsons.OrderStructure;
import uk.ac.ed.inf.records.LngLat;
import uk.ac.ed.inf.records.Restaurant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.util.*;

/**
 * This controls the movement of the drone according to a day's orders
 */
public class Drone {

    final static LngLat appletonTower = new LngLat(-3.186874, 55.944494);
    private static int battery = 2000;
    private static OrderStructure[] orders;
    private static HashMap<String,OrderOutcome> dayOrderOutcomes = new HashMap<>();
    private static HashMap<String,ArrayList<AStarEntry>> dayOrderMoves = new HashMap<>();
    private static LngLat currentPosition;
    private static NoFlyZoneJSON[] noFlyZones;
    private static JSONPoint[] centralArea;
    private static Clock clock = Clock.systemDefaultZone();
    private static int timeWhenStarted;

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

    private static ArrayList<AStarEntry> calculateMoves(OrderStructure order){

        ArrayList<AStarEntry> moves = null;
        //get the restaurant the order is going to
        Restaurant restaurant = Order.getPizzaRestaurant(order.orderItems[0]);

       LngLat restaurantLocation = new LngLat(restaurant.getLongitude(), restaurant.getLatitude());

        moves = AStar.astar(currentPosition, restaurantLocation, clock);

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

    private static void writeDeliveriesJSONs(String date) throws IOException {

        ArrayList<String> deliveries = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String json;
        String orderNo;
        String outcome;
        int costInPence;

        for (int i = 0; i < orders.length; i++){

            OrderStructure order = orders[i];
            ObjectNode delivery = mapper.createObjectNode();

            orderNo = order.orderNo;
            outcome = String.valueOf(dayOrderOutcomes.get(orderNo));

            if (outcome == "Delivered" || outcome == "ValidButNotDelivered"){
                costInPence = Integer.parseInt(order.priceTotalInPence);
            } else {
                //for invalid orders we can place the costInPence as 0
                costInPence = 0;
            }

            delivery.put("orderNo", orderNo);
            delivery.put("outcome", outcome);
            delivery.put("costInPence", costInPence);
            json = mapper.writeValueAsString(delivery);
            deliveries.add(json);

        }

        FileHandler.writeDeliveries(date, deliveries.toString());

    }

    private static void recordMovements(){

        //loop through each of the orders from today and record each of the movements the drone made
        for (int i = 0; i < orders.length; i++){

            String orderNo = orders[i].orderNo;
            ArrayList<AStarEntry> path = dayOrderMoves.get(orderNo);
            Double angle;
            int ticksFromStart = 0;

            //if the path is null that means that the order wasn't delivered, so we record no movements
            if (path == null){
                continue;
            }

            //System.out.println(path.size());

            //and now loop through all the movements in the path and record it
            for (int j = 0; j < path.size()-1; j = j+2){

                LngLat from = path.get(j).getCoords();
                LngLat to = path.get(j+1).getCoords();

                //if to is null then the drone is hovering, so can't get degree
                if (to != null) {
                    angle = path.get(j+1).getAngle().getDegree();

                    //this checks if we're going back to Appleton Tower or not
                    if (j >= (path.size()/2)-1){
                        //the angle is 180 degrees turned
                        angle = (angle + 180)%360;
                    }

                } else {
                    angle = null;
                }

                //this checks if we're going back to Appleton Tower or not
                if (j >= (path.size()/2)-1){
                    orderNo = "no-order";
                }

                ticksFromStart = path.get(j).getTimeToCompute().getNano() - timeWhenStarted;


                try {
                    FileHandler.writeFlightpathJSONs(orderNo, from, to, angle, ticksFromStart);
                } catch(JsonProcessingException e){
                    System.out.println("Error processing flightpath JSON.");
                }

            }

            try {
                FileHandler.writeFlightPath(orders[0].orderDate);
            } catch(IOException e){
                System.out.println("Error writing flightpath output file.");
            }

        }

        /*LngLat fromLngLat = from.getCoords();
        LngLat toLngLat = null;
        CompassDirection angle = null;
        int ticksSinceStart = 0;
        if (to != null) {
            toLngLat = to.getCoords();
            angle = to.getAngle();
            ticksSinceStart = to.getTimeToCompute().getNano();
        }

        try {
            FileHandler.writeFlightpathJSONs(orderNo, fromLngLat, toLngLat, angle, ticksSinceStart);
        } catch (JsonProcessingException e){
            System.out.println("Error processing JSON in AStar recordMovements.");
        }*/

    }

    public static void StartDay(String date) throws MalformedURLException {

        orders = Order.getDayOrders(date);
        centralArea = setCentralAreaPoints(RESTUrl.getUrl());
        noFlyZones = setNoFlyZones(RESTUrl.getUrl());
        currentPosition = appletonTower;
        OrderOutcome currentOrderOutcome = null;
        ArrayList<AStarEntry> currentMoves = null;
        timeWhenStarted = Clock.tick(clock, Duration.ofMillis(1)).instant().getNano();
        System.out.println(timeWhenStarted);

        orderOrdersByDistance();

        for (OrderStructure currentOrder : orders){

            currentOrderOutcome = Order.validateOrder(currentOrder);

            //if the order outcome isn't valid then we put it in the file and move to the next order, or if the battery has run out then we can't deliver the valid order
            if ((currentOrderOutcome != OrderOutcome.ValidButNotDelivered) || (battery < 0)){

                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                dayOrderMoves.put(currentOrder.orderNo, null);
                continue;

            }

            currentMoves = calculateMoves(currentOrder);

            //the drone must move to the restaurant and back again
            battery = battery - currentMoves.size()*2;

            //if the current order makes the battery go into negative values, then we have to stop as the battery has run out
            if (battery < 0){
                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                dayOrderMoves.put(currentOrder.orderNo, null);
                continue;
            }

            dayOrderOutcomes.put(currentOrder.orderNo, OrderOutcome.Delivered);
            dayOrderMoves.put(currentOrder.orderNo, currentMoves);
        }

        try{
            writeDeliveriesJSONs(date);
            recordMovements();
        } catch (IOException e){
            System.out.println("Error writing deliveries to file.");
        }

    }

    public static JSONPoint[] getCentralArea(){ return centralArea; }
    public static NoFlyZoneJSON[] getNoFlyZones(){ return noFlyZones; }

}
