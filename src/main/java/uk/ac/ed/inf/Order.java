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

import java.io.IOException;
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
    private static HashMap<String,Integer> menuItems = new HashMap<String,Integer>();

    /**
     * A Hashmap that maps all the potential menu items to the restaurant they belong to
     */
    private static HashMap<String, Restaurant> restaurantPizzas = new HashMap<String,Restaurant>();

    public static OrderOutcome validateOrder(OrderStructure order) throws MalformedURLException {

        OrderOutcome isValid = OrderOutcome.ValidButNotDelivered;
        ArrayList<String> cardDetails = new ArrayList<>();
        ArrayList<String> orderItems = new ArrayList<>();
        int cost = 100;

        cardDetails.add(order.creditCardNumber);
        cardDetails.add(order.creditCardExpiry);
        cardDetails.add(order.cvv);
        orderItems.addAll(Arrays.asList(order.orderItems));

        int cardValidate = Card.checkCreditCard(cardDetails);

        if (cardValidate != 0){
            switch (cardValidate){

                case 1:
                    isValid = OrderOutcome.InvalidCvv;
                    break;

                case 2:
                    isValid = OrderOutcome.InvalidExpiryDate;
                    break;

                case 3:
                    isValid = OrderOutcome.InvalidCardNumber;
                    break;
            }
        }

        try{
            cost = getDeliveryCost(orderItems);
        } catch (InvalidPizzaNotDefined e){
            isValid = OrderOutcome.InvalidPizzaNotDefined;
        } catch (InvalidPizzaCount e){
            isValid = OrderOutcome.InvalidPizzaCount;
        } catch (InvalidPizzaCombinationMultipleSuppliers e){
            isValid = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }

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
        for (int i = 0; i < restaurants.length; i++){

            Menu[] menu = restaurants[i].getMenu();
            Restaurant restaurant = restaurants[i];

            //loop through menu array items and add to menuItems hashmap with price
            for (int j = 0; j < menu.length; j++){

                String pizza = menu[j].name;
                Integer priceInPennies = menu[j].priceInPennies;

                menuItems.putIfAbsent(pizza,priceInPennies);
                restaurantPizzas.putIfAbsent(pizza,restaurant);

            }

        }

    }

    public static OrderStructure[] getDayOrders(String date){

        ObjectMapper mapper = new ObjectMapper();
        OrderStructure[] orders = null;

        String url = RESTUrl.getUrl();

        //obtaining the central area points here
        try{
            URL jsonUrl = new URL(url + "/orders/" + date);
            orders = mapper.readValue(jsonUrl, OrderStructure[].class);

        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
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

    public static Restaurant getPizzaRestaurant(String pizza){
        return restaurantPizzas.get(pizza);
    }

}
