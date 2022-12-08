package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.enums.OrderOutcome;
import uk.ac.ed.inf.exceptions.InvalidPizzaCombinationException;
import uk.ac.ed.inf.exceptions.InvalidPizzaCombinationMultipleSuppliers;
import uk.ac.ed.inf.exceptions.InvalidPizzaCount;
import uk.ac.ed.inf.exceptions.InvalidPizzaNotDefined;
import uk.ac.ed.inf.jsons.Menu;
import uk.ac.ed.inf.jsons.OrderStructure;
import uk.ac.ed.inf.records.Restaurant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.exit;

/**
 * Order helps deal with orders that the customer may make and figuring out the cost of the order
 */
public class Order {

    /**
     * A Hashmap that maps the names of menu items to the prices that the item it
     */
    final private static HashMap<String,Integer> menuItems = new HashMap<>();

    /**
     * A Hashmap that maps all the potential menu items to the restaurant they belong to
     */
    final private static HashMap<String, Restaurant> restaurantPizzas = new HashMap<>();

    /**
     * validateOrder takes a single order and checks if its invalid in any way
     * It checks the credit card and pizzas ordered to see if they are valid
     * @param order the order to be validated
     * @return the outcome of the order. It is ValidButNotDelivered by default, but changes if it is invalid
     */
    public static OrderOutcome validateOrder(OrderStructure order) {

        OrderOutcome isValid = OrderOutcome.ValidButNotDelivered;
        ArrayList<String> cardDetails = new ArrayList<>();
        int cost;

        //set up the card details and the pizzas to be validated
        cardDetails.add(order.creditCardNumber);
        cardDetails.add(order.creditCardExpiry);
        cardDetails.add(order.cvv);
        ArrayList<String> orderItems = new ArrayList<>(Arrays.asList(order.orderItems));

        //the value of cardValidate shows if the card is invalid, and if so how
        int cardValidate = Card.checkCreditCard(cardDetails);

        //if cardValidate == 0, then the card is valid
        if (cardValidate != 0){
            switch (cardValidate) {

                //it has an invalid cvv
                case 1 -> {
                    isValid = OrderOutcome.InvalidCvv;
                    return isValid;
                }

                //invalid expiry date
                case 2 -> {
                    isValid = OrderOutcome.InvalidExpiryDate;
                    return isValid;
                }

                //invalid card number
                case 3 -> {
                    isValid = OrderOutcome.InvalidCardNumber;
                    return isValid;
                }
            }
        }

        //if getDeliveryCost throws an error, then the ordered items were invalid in some way
        try{
            cost = getDeliveryCost(orderItems);
            //add on 100 for the delivery fee
            cost = cost + 100;
        } catch (InvalidPizzaNotDefined e){ //if a pizza doesn't exist in the menus
            isValid = OrderOutcome.InvalidPizzaNotDefined;
            return isValid;
        } catch (InvalidPizzaCount e){ //if there's too many pizzas or not enough
            isValid = OrderOutcome.InvalidPizzaCount;
            return isValid;
        } catch (InvalidPizzaCombinationMultipleSuppliers e){ //if the pizzas come from more than 1 restaurant
            isValid = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
            return isValid;
        }

        //finally check if the cost calculated is the same as the order's price total
        if (cost != Integer.parseInt(order.priceTotalInPence)){

            isValid = OrderOutcome.InvalidTotal;

        }

        return isValid;

    }

    /**
     * This sets up the menuItems and restaurantPizzas Hashmaps using the passed in parameter
     */
    public static void setUpMenuItems() throws MalformedURLException {

        URL restaurantUrl = new URL(RESTUrl.getUrl() + "restaurants");
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(restaurantUrl);

        //loop through the array of restaurants to obtain the relevant menu list & restaurant
        for (Restaurant value : restaurants) {

            Menu[] menu = value.getMenu();

            //loop through menu array items and add to menuItems hashmap with price
            for (Menu item : menu) {

                String pizza = item.name;
                Integer priceInPennies = item.priceInPennies;

                menuItems.putIfAbsent(pizza, priceInPennies);
                restaurantPizzas.putIfAbsent(pizza, value);

            }

        }

    }

    /**
     * This returns all the orders for a specified day
     * @param date the day to obtain the orders from
     * @return an array of all the orders
     */
    public static OrderStructure[] getDayOrders(String date){

        ObjectMapper mapper = new ObjectMapper();
        OrderStructure[] orders = null;

        String url = RESTUrl.getUrl();

        //obtaining the central area points here
        try{
            URL jsonUrl = new URL(url + "/orders/" + date);
            orders = mapper.readValue(jsonUrl, OrderStructure[].class);

        } catch(Exception e){
            System.err.println("Error in getDayOrders, Order.java");
        }

        return orders;

    }

    /**
     * This method calculates the delivery cost for an order, starting from the base delivery cost of £1
     * It adds on the cost for the individual pizzas, and only allows orders to order from one restaurant
     * Checks that the order is also valid
     * @param order the order of pizzas the customer wants
     * @return the price of the total delivery
     * @throws InvalidPizzaCombinationException whenever the order doesn't follow the guidelines for an order
     * @throws NullPointerException if we try to enter the loop and the order is empty
     */
    public static int getDeliveryCost(ArrayList<String> order){

        int cost = 100;
        String pizza;
        //uniqueRestaurant is used to make sure the order only ever orders from 1 restaurant
        Restaurant uniqueRestaurant = null;

        //required so that we only set up the menu items once during the program's run
        if (menuItems.isEmpty()){

            try{
                setUpMenuItems();
            } catch(MalformedURLException e){
                System.out.println("MalformedURLException while setting up menu in Order.java");
                exit(1);
            }
        }

        //we loop through each of the order's pizzas to calculate the cost
        try {for (int i = 0; i < order.size(); i++){

            //check if order is too big
            if (order.size() > 4){
                throw new InvalidPizzaCount("Invalid pizza combination: Invalid number of pizzas in order");
            }

            pizza = order.get(i);
            Restaurant currentRestaurant = restaurantPizzas.get(pizza);

            //set uniqueRestaurant
            if (uniqueRestaurant == null){
                uniqueRestaurant = restaurantPizzas.get(pizza);
            }

            //check if the current restaurant the menu item belongs to is different to the order's allowed restaurant
            if (!currentRestaurant.equals(uniqueRestaurant)){
                throw new InvalidPizzaCombinationMultipleSuppliers("Invalid pizza combination: Ordering from more than one restaurant");
            }

            cost += menuItems.get(pizza);

        }} catch (NullPointerException err){
            throw new InvalidPizzaNotDefined("Invalid pizza combination: pizza doesn't exist in selection");
        }

        if (order.isEmpty()) {
            throw new InvalidPizzaNotDefined("Invalid pizza combination: Empty order");
        }

        return cost;

    }

    /**
     * getPizzaRestaurant returns the restaurant that the specified pizza belongs to
     * @param pizza the pizza to find the restaurant of
     * @return the restaurant it belongs to, if any
     */
    public static Restaurant getPizzaRestaurant(String pizza){

        //in case we try to call getPizzaRestaurant when it's still empty
        if (menuItems.isEmpty()){

            try{
                setUpMenuItems();
            } catch(MalformedURLException e){
                System.out.println("MalformedURLException while setting up menu in Order.java");
                exit(1);
            }
        }

        return restaurantPizzas.get(pizza);
    }

}
