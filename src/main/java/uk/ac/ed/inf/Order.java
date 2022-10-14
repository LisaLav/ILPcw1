package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static HashMap<String,Restaurant> restaurantPizzas = new HashMap<String,Restaurant>();

    /**
     * This sets up the menuItems and restaurantPizzas Hashmaps using the passed in parameter
     * @param restaurants the list of participating restaurants
     */
    private static void setUpMenuItems(Restaurant[] restaurants){

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

    /**
     * This method calculates the delivery cost for an order, starting from the base delivery cost of £1
     * It adds on the cost for the individual pizzas, and only allows orders to order from one restaurant
     * Checks that the order is also valid
     * @param restaurants list of participating restaurants
     * @param order the order the customer wants
     * @return the price of the total delivery
     * @throws InvalidPizzaCombinationException whenever the order doesn't follow the guidelines for an order
     * @throws NullPointerException if we try to enter the loop and the order is empty
     */
    public static int getDeliveryCost(Restaurant[] restaurants, ArrayList<String> order){

        int cost = 100;
        String pizza;
        //uniqueRestaurant is used to make sure the order only ever orders from 1 restaurant
        Restaurant uniqueRestaurant = null;

        //required so that we only set up the menu items once during the program's run
        if (menuItems.isEmpty()){
            setUpMenuItems(restaurants);
        }

        //we loop through each of the order's pizzas to calculate the cost
        try {for (int i = 0; i < order.size(); i++){

            //check if order is too big
            if (order.size() > 4){
                throw new InvalidPizzaCombinationException("Invalid pizza combination: Invalid number of pizzas in order");
            }

            pizza = order.get(i);
            Restaurant currentRestaurant = restaurantPizzas.get(pizza);

            //set uniqueRestaurant
            if (uniqueRestaurant == null){
                uniqueRestaurant = restaurantPizzas.get(pizza);
            }

            //check if the current restaurant the menu item belongs to is different to the order's allowed restaurant
            if (!currentRestaurant.equals(uniqueRestaurant)){
                throw new InvalidPizzaCombinationException("Invalid pizza combination: Ordering from more than one restaurant");
            }

            cost += menuItems.get(pizza);

        }} catch (NullPointerException err){
            throw new InvalidPizzaCombinationException("Invalid pizza combination: pizza doesn't exist in selection");
        }

        if (order.isEmpty()) {
            throw new InvalidPizzaCombinationException("Invalid pizza combination: Empty order");
        }

        return cost;

    }

}
