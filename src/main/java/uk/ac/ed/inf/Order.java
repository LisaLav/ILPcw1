package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {

    private static HashMap<String,Integer> menuItems = new HashMap<String,Integer>();

    private static void setUpMenuItems(Restaurant[] restaurants){

        for (int i = 0; i < restaurants.length; i++){

            Menu[] menu = restaurants[i].getMenu();

            for (int j = 0; j < menu.length; j++){

                String pizza = menu[j].name;
                Integer priceInPennies = menu[j].priceInPennies;

                menuItems.putIfAbsent(pizza,priceInPennies);

            }

        }

    }

    public static int getDeliveryCost(Restaurant[] restaurants, ArrayList<String> order){

        int cost = 100;
        String pizza;

        if (menuItems.isEmpty()){
            setUpMenuItems(restaurants);
        }


        try {for (int i = 0; i < order.size(); i++){

            if (order.size() > 4){
                throw new InvalidPizzaCombinationException("Invalid pizza combination: Invalid number of pizzas in order");
            }

            pizza = order.get(i);

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
