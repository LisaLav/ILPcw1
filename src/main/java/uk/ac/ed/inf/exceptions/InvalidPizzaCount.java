package uk.ac.ed.inf.exceptions;

/**
 * Exists for when an order has the incorrect number of pizzas
 */
public class InvalidPizzaCount extends RuntimeException{

    /**
     * The custom error message
     * @param errorMessage error message to display
     */
    public InvalidPizzaCount(String errorMessage){
        super(errorMessage);
    }

}
