package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.ac.ed.inf.algorithms.AStar;
import uk.ac.ed.inf.algorithms.AStarEntry;
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
 * Drone class which combines other classes to calculate all the orders for a single day
 */
public class Drone {

    //constant for AppletonTower
    final static LngLat appletonTower = new LngLat(-3.186874, 55.944494);
    private static int battery = 2000;
    //holds all the orders for the day
    private static OrderStructure[] orders;
    //holds the outcomes for each order
    final static HashMap<String,OrderOutcome> dayOrderOutcomes = new HashMap<>();
    //holds the flight path for each order
    final static HashMap<String,ArrayList<AStarEntry>> dayOrderMoves = new HashMap<>();
    private static LngLat currentPosition;
    private static NoFlyZoneJSON[] noFlyZones;
    private static JSONPoint[] centralArea;
    //clock to keep track of the calculation time
    final static Clock clock = Clock.systemDefaultZone();
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

        } catch(Exception e){
            System.err.println("Error obtaining central area points");
        }

        return centralAreaPoints;

    }

    /**
     * setNoFlyZones connects to the REST server and obtains the coordinates of the up-to-date no-fly zones
     * @param url the base url for the REST server
     * @return the coordinates that create the no-fly zones as a NoFlyZoneJSON array
     */
    private static NoFlyZoneJSON[] setNoFlyZones(String url) {

        ObjectMapper mapper = new ObjectMapper();
        NoFlyZoneJSON[] noFlyZones = null;

        //try getting no-fly zones
        try {
            URL jsonUrl = new URL(url + "noFlyZones");
            noFlyZones = mapper.readValue(jsonUrl, NoFlyZoneJSON[].class);
        } catch (Exception e) {
            System.err.println("Error obtaining no-fly zones");
        }

        return noFlyZones;

    }

    /**
     * calculateMoves calculates the moves for a single order using A*
     * @param order the order to calculate the moves for
     * @return the calculated moves for the order from Appleton Tower to the restaurant and back to Appleton Tower
     */
    private static ArrayList<AStarEntry> calculateMoves(OrderStructure order){

        ArrayList<AStarEntry> moves;
        //get the restaurant the order is going to
        Restaurant restaurant = Order.getPizzaRestaurant(order.orderItems[0]);

        //set up the restaurant for the A* algorithm
        LngLat restaurantLocation = new LngLat(restaurant.getLongitude(), restaurant.getLatitude());

        //calculate the moves
        moves = AStar.astar(currentPosition, restaurantLocation, clock);

        return moves;

    }

    /**
     * orderOrdersByDistance orders the orders by distance from Appleton Tower to each of the restaurants
     * This helps maximise the profit by completing the orders that take the least moves first
     */
    private static void orderOrdersByDistance(){

        //convert the orders into an arraylist for sorting
        List<OrderStructure> orderedOrders = new ArrayList<>(Arrays.asList(orders));

        //order the orders by the distance from appleton tower to the restaurant
        orderedOrders.sort(Comparator.comparingDouble(o -> appletonTower.distanceTo(new LngLat(Order.getPizzaRestaurant(o.orderItems[0]).getLongitude(), Order.getPizzaRestaurant(o.orderItems[0]).getLatitude()))));

        for (int i = 0; i < orders.length; i++){
            orders[i] = orderedOrders.get(i);
        }

    }

    /**
     * writeDeliveriesJSONs uses FileHandler to write the delivery JSONs to output
     * It creates each JSON for each order, their outcome and their total cost in pence
     * @param date the date for the day for the orders
     */
    private static void writeDeliveriesJSONs(String date) throws IOException {

        ArrayList<String> deliveries = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String json;
        String orderNo;
        String outcome;
        int costInPence;

        //loop through each order and create a JSON for it in form {"orderNo", "outcome", "costInPence"}
        for (OrderStructure order : orders) {

            ObjectNode delivery = mapper.createObjectNode();

            orderNo = order.orderNo;
            outcome = String.valueOf(dayOrderOutcomes.get(orderNo));

            //here if the order is invalid then the cost should be 0 as we don't gain monetary value from it
            if (outcome.equals("Delivered") || outcome.equals("ValidButNotDelivered")) {
                costInPence = Integer.parseInt(order.priceTotalInPence);
            } else {
                costInPence = 0;
            }

            //set up the JSON and then add it to the list of deliveries
            delivery.put("orderNo", orderNo);
            delivery.put("outcome", outcome);
            delivery.put("costInPence", costInPence);
            json = mapper.writeValueAsString(delivery);
            deliveries.add(json);

        }

        FileHandler.writeDeliveries(date, deliveries.toString());

    }

    /**
     * recordMovements helps create the flightpath output file
     * It goes through every order's path and sets up the variables to be put into the JSON
     */
    private static void recordMovements(){

        //this is to help keep track when the drone has made its final movements
        int internalBattery = 2000;

        //loop through each of the orders from today and record each of the movements the drone made
        for (OrderStructure order : orders) {

            String orderNo = order.orderNo;
            ArrayList<AStarEntry> path = dayOrderMoves.get(orderNo);
            Double angle;
            //this holds the calculated ticks from when the move was calculated from the start of the program
            int ticksFromStart;

            //if the path is null that means that the order wasn't delivered, so we record no movements
            if (path == null) {
                continue;
            }

            //lower the internalBattery by how many moves were made
            internalBattery = internalBattery - path.size();

            //and now loop through all the movements in the path and record it
            for (int j = 1; j < path.size(); j++) {

                LngLat from = path.get(j - 1).getCoords();
                LngLat to = path.get(j).getCoords();

                //if getAngle is null then the drone is hovering, so can't get degree
                if (path.get(j).getAngle() != null) {
                    angle = path.get(j).getAngle().getDegree();

                    //this checks if we're going back to Appleton Tower or not
                    if (j >= (path.size() / 2) - 1) {
                        //the angle is 180 degrees turned, %360 to keep it in the degree range
                        angle = (angle + 180) % 360;
                    }

                } else {
                    angle = null;
                }

                //this checks if we're going back to Appleton Tower or not for the final drone flight path
                //which works as the orders are ordered by shortest path to longest, so if reducing it by the current path size again makes it < 0 then it's the final order
                if ((internalBattery - path.size() < 0) && (j >= (path.size() / 2) - 1)) {
                    orderNo = "no-order";
                }

                ticksFromStart = path.get(j - 1).getTimeToCompute().getNano() - timeWhenStarted;

                //store this current movement in FileHandler, which processes it into a JSON
                try {
                    FileHandler.writeFlightpathJSONs(orderNo, from, to, angle, ticksFromStart);
                } catch (JsonProcessingException e) {
                    System.err.println("Error processing flightpath JSON.");
                }

            }

            //now write all the movements into the flightpath output file
            try {
                FileHandler.writeFlightPath(orders[0].orderDate);
            } catch (IOException e) {
                System.err.println("Error writing flightpath output file.");
            }

        }

    }

    /**
     * This method helps create the drone output file
     * It converts all the moves made into a Vector\<LngLat\>
     * @param date the date for the day of the orders
     */
    private static void recordDronePath(String date){

        //put all the LngLats in the moves into a single ArrayList<LngLat>
        ArrayList<LngLat> lngLats = new ArrayList<>();

        //now loop through each path and all its moves and get the LngLat value
        for (ArrayList<AStarEntry> entry : dayOrderMoves.values()){

            if (entry == null){
                continue;
            }

            for (AStarEntry move : entry){
                if (move.getCoords() == null){
                    continue;
                }
                lngLats.add(move.getCoords());
            }

        }

        //convert to Vector<LngLat>
        Vector<LngLat> lngLatVector = new Vector<>(lngLats);

        //write the output drone file
        try {
            FileHandler.writeDronePath(date, lngLatVector);
        } catch (IOException e){
            System.err.println("Error writing drone path output file.");
        }

    }

    /**
     * StartDay sets up the variables required to calculate the moves for the drone for the whole day
     * It loops through each of the orders, validates them, and performs the appropriate actions for each outcome
     * @param date the day of orders that need completing
     * @throws MalformedURLException if there's an issue with getting the url from RESTUrl
     */
    public static void StartDay(String date) throws MalformedURLException {

        //set up initial variables
        orders = Order.getDayOrders(date);
        centralArea = setCentralAreaPoints(RESTUrl.getUrl());
        noFlyZones = setNoFlyZones(RESTUrl.getUrl());
        currentPosition = appletonTower;
        OrderOutcome currentOrderOutcome;
        ArrayList<AStarEntry> currentMoves;
        timeWhenStarted = Clock.tick(clock, Duration.ofMillis(1)).instant().getNano();

        //sort the orders by shortest distance to get the most orders completed in a day
        orderOrdersByDistance();

        //loop through each order and calculate the moves and outcomes for each one
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
            battery = battery - currentMoves.size();

            //if the current order makes the battery go into negative values, then we have to stop as the battery has run out, can't complete the current order
            if (battery < 0){
                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                dayOrderMoves.put(currentOrder.orderNo, null);
                continue;
            }

            //if we reach here then the order has been successfully delivered, and we can record it
            dayOrderOutcomes.put(currentOrder.orderNo, OrderOutcome.Delivered);
            dayOrderMoves.put(currentOrder.orderNo, currentMoves);
        }

        //write all the output files
        try{
            writeDeliveriesJSONs(date);
            recordMovements();
            recordDronePath(date);
        } catch (IOException e){
            System.err.println("Error writing deliveries to file.");
        }

    }

    /**
     * Returns the central area
     * @return the central area
     */
    public static JSONPoint[] getCentralArea(){ return centralArea; }

    /**
     * Returns the no-fly zones
     * @return the no-fly zones
     */
    public static NoFlyZoneJSON[] getNoFlyZones(){ return noFlyZones; }

}
